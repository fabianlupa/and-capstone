package com.flaiker.sc2profiler.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.flaiker.sc2profiler.BuildConfig;
import com.flaiker.sc2profiler.persistence.LadderContract;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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


    synchronized public static void syncLadder(Context context) {
        Log.d(TAG, "Starting ladder sync");
        try {
            URL ladderRequestUrl = buildUrl(SC2_LADDER_API.buildUpon()
                    .appendPath("191177") // TODO: Remove hard coded ladder id
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

    synchronized public static void syncProfiles(Context context) {
        // TODO: Implement
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
