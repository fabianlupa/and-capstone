package com.flaiker.sc2profiler.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import com.flaiker.sc2profiler.R;
import com.flaiker.sc2profiler.models.Profile;
import com.flaiker.sc2profiler.persistence.LadderContract;

public class ProfileWidget extends AppWidgetProvider {
    private static final String TAG = ProfileWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        ProfileWidgetConfigureActivity.ProfileKey key =
                ProfileWidgetConfigureActivity.loadProfilePref(context, appWidgetId);

        Intent intent = new Intent(context, ProfileDetailActivity.class);
        intent.putExtra(ProfileDetailActivity.EXTRA_PROFILE_NAME, key.name);
        intent.putExtra(ProfileDetailActivity.EXTRA_PROFILE_ID, key.id);
        intent.putExtra(ProfileDetailActivity.EXTRA_REALM, key.realm);
        intent.setAction("dummy");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.profile_widget);

        Cursor cursor = context.getContentResolver().query(
                LadderContract.ProfileEntry.CONTENT_URI,
                null,
                LadderContract.ProfileEntry.COLUMN_CHARACTER_ID + " = ? AND " +
                        LadderContract.ProfileEntry.COLUMN_DISPLAY_NAME + " = ? AND " +
                        LadderContract.ProfileEntry.COLUMN_REALM + " = ?",
                new String[]{String.valueOf(key.id), key.name, String.valueOf(key.realm)},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            Profile profile = Profile.ofCursor(cursor);
            views.setImageViewResource(R.id.profile_race_icon, profile.race.iconId);
            views.setImageViewResource(R.id.profile_league_icon, profile.league.iconId);
            views.setTextViewText(R.id.profile_name, profile.name);
            views.setTextViewText(R.id.profile_league_rank_text, profile.getFormattedRankingText());
            cursor.close();
        }

        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "Updating widgets");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            ProfileWidgetConfigureActivity.deleteProfilePref(context, appWidgetId);
        }
    }
}

