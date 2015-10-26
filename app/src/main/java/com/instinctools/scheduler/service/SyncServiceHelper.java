package com.instinctools.scheduler.service;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Alexandr Golovach on 23.10.2015.
 */
public class SyncServiceHelper {

    public static Intent generateServiceIntent(Context context, SyncService.SyncType syncType) {
        if (context != null) {
            Intent intent = new Intent(context, SyncService.class);
            intent.putExtra(SyncService.ARG_SYNC_TYPE, syncType);
            return intent;
        }
        return null;
    }

    public static void startSyncService(Context context, SyncService.SyncType syncType) {
        Intent intent = generateServiceIntent(context, syncType);
        if (intent != null) {
            context.startService(intent);
        }
    }
}
