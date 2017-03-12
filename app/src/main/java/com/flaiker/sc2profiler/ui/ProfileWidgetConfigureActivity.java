package com.flaiker.sc2profiler.ui;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.flaiker.sc2profiler.R;
import com.flaiker.sc2profiler.models.Profile;
import com.flaiker.sc2profiler.persistence.LadderContract;

import java.util.ArrayList;

public class ProfileWidgetConfigureActivity extends Activity {
    private static final String PREFS_NAME = "com.flaiker.sc2profiler.ui.ProfileWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREFS_PLAYER_ID = "id";
    private static final String PREFS_PLAYER_NAME = "name";
    private static final String PREFS_PLAYER_REALM = "realm";
    private Spinner mSpinner;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = ProfileWidgetConfigureActivity.this;

            Profile selectedProfile = (Profile) mSpinner.getSelectedItem();
            saveProfilePref(context, mAppWidgetId, selectedProfile);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ProfileWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public ProfileWidgetConfigureActivity() {
        super();
    }

    static void saveProfilePref(Context context, int appWidgetId, Profile profile) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId + PREFS_PLAYER_ID, profile.id);
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + PREFS_PLAYER_NAME, profile.name);
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId + PREFS_PLAYER_REALM, profile.realm);
        prefs.apply();
    }

    static ProfileKey loadProfilePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int id = prefs.getInt(PREF_PREFIX_KEY + appWidgetId + PREFS_PLAYER_ID, 0);
        String name = prefs.getString(PREF_PREFIX_KEY + appWidgetId + PREFS_PLAYER_NAME, "");
        int realm = prefs.getInt(PREF_PREFIX_KEY + appWidgetId + PREFS_PLAYER_REALM, 1);

        return new ProfileKey(id, name, realm);
    }

    static void deleteProfilePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId + PREFS_PLAYER_NAME);
        prefs.remove(PREF_PREFIX_KEY + appWidgetId + PREFS_PLAYER_ID);
        prefs.remove(PREF_PREFIX_KEY + appWidgetId + PREFS_PLAYER_REALM);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.profile_widget_configure);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        mSpinner = (Spinner) findViewById(R.id.widget_spinner);
        Cursor cursor = getContentResolver().query(
                LadderContract.ProfileEntry.CONTENT_URI,
                null,
                LadderContract.ProfileEntry.COLUMN_FAVORITE + " = ?",
                new String[]{"1"},
                null);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Profile> favorites = new ArrayList<>();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                favorites.add(Profile.ofCursor(cursor));
            }
            cursor.close();
            mSpinner.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, favorites));
        }


        // If this activity was started with an intent without an app widget ID,
        // finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    static class ProfileKey {
        public final int id;
        public final String name;
        public final int realm;

        ProfileKey(int id, String name, int realm) {
            this.id = id;
            this.name = name;
            this.realm = realm;
        }
    }
}

