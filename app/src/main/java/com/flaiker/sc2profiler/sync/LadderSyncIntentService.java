package com.flaiker.sc2profiler.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * IntentService for {@link LadderSyncTask}
 */
public class LadderSyncIntentService extends IntentService {
    public LadderSyncIntentService() {
        super(LadderSyncIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LadderSyncTask.syncLadder(this);
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Intent intent = new Intent(context, LadderSyncIntentService.class);
        context.startService(intent);
    }
}
