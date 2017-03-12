package com.flaiker.sc2profiler.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.flaiker.sc2profiler.BuildConfig;
import com.flaiker.sc2profiler.R;
import com.flaiker.sc2profiler.persistence.LadderContract;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

/**
 * Class to synchronize local database with battlenet API
 */
public class LadderSyncTask {
    private static final String TAG = LadderSyncTask.class.getSimpleName();

    private static final String BATTLENET_API_BASE_URL = "https://eu.api.battle.net";
    private static final String SC2_API_PATH = "sc2";
    private static final String ENDPOINT_PROFILE = "profile";
    private static final String ENDPOINT_LADDER = "ladder";
    private static final String ENDPOINT_PROFILE_LADDERS = "ladders";

    private static final Uri BATTLENET_API_BASE_URI = Uri.parse(BATTLENET_API_BASE_URL);
    private static final Uri BATTLENET_SC2_URI = BATTLENET_API_BASE_URI.buildUpon()
            .appendPath(SC2_API_PATH)
            .build();
    private static final Uri SC2_PROFILE_API = BATTLENET_SC2_URI.buildUpon()
            .appendPath(ENDPOINT_PROFILE)
            .build();
    private static final Uri SC2_LADDER_API = BATTLENET_SC2_URI.buildUpon()
            .appendPath(ENDPOINT_LADDER)
            .build();

    private static final String APIKEY_KEY = "apikey";
    private static final String LOCALE_KEY = "locale";
    private static final String LOCALE_VALUE = "en_GB";

    private static FirebaseAnalytics sFirebaseAnalytics;

    synchronized public static void syncLadder(Context context) {
        if (sFirebaseAnalytics == null) sFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Log.d(TAG, "Starting ladder sync");
        sFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, null);

        try {
            URL ladderRequestUrl = buildUrl(SC2_LADDER_API.buildUpon()
                    /* It is currently not possible to retrieve the grandmaster ladder ids of the
                       current season from the battlenet api so this has to be hard coded until
                       there is a rest endpoint for this */
                    .appendPath("191177")
                    .build());
            String response = getResponseFromUrl(ladderRequestUrl);
            //Log.d(TAG, "Response: \n" + response);
            ContentValues[] ladderValues
                    = BattlenetApiJsonParser.getContentValuesFromLadderJson(response);

            if (ladderValues != null && ladderValues.length != 0) {
                ContentResolver resolver = context.getContentResolver();
                resolver.delete(
                        LadderContract.LadderEntry.CONTENT_URI,
                        null,
                        null);
                resolver.bulkInsert(LadderContract.LadderEntry.CONTENT_URI, ladderValues);
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    synchronized public static void fetchNewProfile(Context context, int profileId, int realm,
                                                    String profileName) {
        if (sFirebaseAnalytics == null) sFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Log.d(TAG, String.format("Fetching data for profile %s - %s", profileId, profileName));

        try {
            URL profileRequestUrl = buildUrl(SC2_PROFILE_API.buildUpon()
                    .appendPath(String.valueOf(profileId))
                    .appendPath(String.valueOf(realm))
                    .appendPath(profileName)
                    .appendPath("")
                    .build());
            String response = getResponseFromUrl(profileRequestUrl);
            Log.d(TAG, response);

            URL ladderRequestUrl = buildUrl(SC2_PROFILE_API.buildUpon()
                    .appendPath(String.valueOf(profileId))
                    .appendPath(String.valueOf(realm))
                    .appendPath(profileName)
                    .appendPath(ENDPOINT_PROFILE_LADDERS)
                    .build());
            String responseLadder = getResponseFromUrl(ladderRequestUrl);
            Log.d(TAG, responseLadder);

            ContentValues profileValues =
                    BattlenetApiJsonParser.getContentValuesFromProfileJson(response,
                            responseLadder);

            if (profileValues != null) {
                ContentResolver resolver = context.getContentResolver();
                resolver.insert(LadderContract.ProfileEntry.CONTENT_URI, profileValues);
            }

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(
                    String.format(Locale.getDefault(),
                            context.getString(R.string.profile_not_found),
                            profileId,
                            profileName));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    synchronized public static void setProfileFavorite(Context context, boolean isFavorite,
                                                       int profileId, int realm,
                                                       String profileName) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                LadderContract.ProfileEntry.CONTENT_URI,
                new String[]{LadderContract.ProfileEntry._ID},
                LadderContract.ProfileEntry.COLUMN_CHARACTER_ID + " = ? AND " +
                        LadderContract.ProfileEntry.COLUMN_REALM + " = ? AND " +
                        LadderContract.ProfileEntry.COLUMN_DISPLAY_NAME + " = ?",
                new String[]{String.valueOf(profileId), String.valueOf(realm), profileName},
                null);

        if (cursor != null && cursor.getCount() == 1) {
            cursor.moveToFirst();
            int internalId = cursor.getInt(0);
            cursor.close();

            ContentValues values = new ContentValues();
            values.put(LadderContract.ProfileEntry.COLUMN_FAVORITE, isFavorite);
            resolver.update(
                    LadderContract.ProfileEntry.CONTENT_URI,
                    values,
                    LadderContract.ProfileEntry._ID + " = ?",
                    new String[]{String.valueOf(internalId)});
        } else {
            throw new IllegalArgumentException("Profile could not be found");
        }
    }

    private static URL buildUrl(Uri baseUri) throws MalformedURLException {
        return new URL(baseUri.buildUpon()
                .appendQueryParameter(APIKEY_KEY, BuildConfig.BATTLENET_API_KEY)
                .appendQueryParameter(LOCALE_KEY, LOCALE_VALUE)
                .build()
                .toString());
    }

    private static String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = connection.getInputStream();

            Scanner scanner = new Scanner((in));
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) response = scanner.next();
            scanner.close();

            return response;
        } finally {
            connection.disconnect();
        }
    }
}
