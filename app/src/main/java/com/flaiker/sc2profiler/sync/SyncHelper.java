package com.flaiker.sc2profiler.sync;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.flaiker.sc2profiler.persistence.LadderContract;

/**
 * Helper class to do immediate synchronization with battle.net if local database is empty or data
 * is too old
 */
public class SyncHelper {
    private static boolean sInitialized = false;

    public static void init(final Context context) {
        if (sInitialized) return;

        // Check if data is present
        Thread checkEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri ladderUri = LadderContract.LadderEntry.CONTENT_URI;
                String[] projection = {LadderContract.LadderEntry._ID};
                Cursor cursor = context.getContentResolver().query(
                        ladderUri,
                        projection,
                        null,
                        null,
                        null);
                // TODO: Check if data is too old

                if (cursor == null || cursor.getCount() == 0) {
                    // This is already in a new thread so synchronization can be called directly
                    LadderSyncTask.syncLadder(context);
                }

                if (cursor != null) cursor.close();
            }
        });

        checkEmpty.start();

        sInitialized = true;
    }
}
