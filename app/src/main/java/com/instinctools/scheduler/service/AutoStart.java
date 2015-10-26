package com.instinctools.scheduler.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.instinctools.scheduler.database.AlarmSyncInfo;
import com.instinctools.scheduler.database.DatabaseModule;
import com.instinctools.scheduler.prefs.Prefs;

/**
 * Created by Alexandr Golovach on 23.10.2015.
 */
public class AutoStart extends BroadcastReceiver {

    private static final String TAG = AutoStart.class.getSimpleName();

    public void onReceive(Context context, Intent arg1) {
        Log.d(TAG, "AutoStart boot completed");

        final String value = Prefs.getStringValue(context, Prefs.ACTIVE_SYNC_APPROACH);
        if (!TextUtils.isEmpty(value)) {
            SyncService.SyncType type = SyncService.SyncType.getSyncType(value);
            if (type == SyncService.SyncType.ALARM) {
                startSyncSchedule(context);
            }
        }
    }

    public static void startSyncSchedule(Context context) {

        Prefs.putStringValue(context, Prefs.ACTIVE_SYNC_APPROACH, SyncService.SyncType.ALARM.getType());
        DatabaseModule.clearTable(AlarmSyncInfo.class);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, SyncServiceHelper.generateServiceIntent(context, SyncService.SyncType.ALARM), 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
                SyncService.SYNC_INTERVAL_MILLIS, pendingIntent);
    }

    public static void stopSyncSchedule(Context context) {
        Prefs.putStringValue(context, Prefs.ACTIVE_SYNC_APPROACH, null);
        Prefs.putLongValue(context, Prefs.LAST_SYNC_TIMESTAMP, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, SyncServiceHelper.generateServiceIntent(context, SyncService.SyncType.ALARM), 0);
        alarmManager.cancel(pendingIntent);
    }
}
