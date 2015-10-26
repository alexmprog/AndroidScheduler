package com.instinctools.scheduler.service;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.instinctools.scheduler.database.DatabaseModule;
import com.instinctools.scheduler.database.GCMSyncInfo;
import com.instinctools.scheduler.prefs.Prefs;

/**
 * Created by Alexandr Golovach on 23.10.2015.
 */
public class GCMHelper {

    private static final String TAG = GCMHelper.class.getSimpleName();

    public static void startSyncSchedule(Context context) {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int errorCheck = api.isGooglePlayServicesAvailable(context);
        if (errorCheck != ConnectionResult.SUCCESS) {
            Log.d(TAG, "GCMHelper - GooglePlayServices isn't available startSyncSchedule");
            return;
        }

        Prefs.putStringValue(context, Prefs.ACTIVE_SYNC_APPROACH, SyncService.SyncType.GCM.getType());
        DatabaseModule.clearTable(GCMSyncInfo.class);

        try {
            PeriodicTask periodic = new PeriodicTask.Builder()
                    .setService(GCMSyncService.class)
                    .setPeriod(SyncService.SYNC_INTERVAL_SECONDS)
                    .setFlex(10)
                    .setTag(GCMSyncService.GCM_REPEAT_TASK_TAG)
                    .setPersisted(true)
                    .setUpdateCurrent(true)
                    .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                    .setRequiresCharging(false)
                    .build();
            GcmNetworkManager.getInstance(context).schedule(periodic);
            Log.d(TAG, "GCMHelper repeating task scheduled");
        } catch (Exception e) {
            Log.e(TAG, "GCMHelper - repeating task scheduling failed");
        }
    }

    public static void stopSyncSchedule(Context context) {
        Prefs.putStringValue(context, Prefs.ACTIVE_SYNC_APPROACH, null);
        Prefs.putLongValue(context, Prefs.LAST_SYNC_TIMESTAMP, 0);
        GcmNetworkManager
                .getInstance(context)
                .cancelTask(GCMSyncService.GCM_REPEAT_TASK_TAG, GCMSyncService.class);
    }
}
