package com.instinctools.scheduler.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.instinctools.scheduler.database.AlarmSyncInfo;
import com.instinctools.scheduler.database.GCMSyncInfo;
import com.instinctools.scheduler.prefs.Prefs;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Alexandr Golovach on 23.10.2015.
 */
public class SyncService extends Service {

    public static final long SYNC_INTERVAL_MILLIS = 1000 * 60 * 15;
    public static final long SYNC_INTERVAL_SECONDS = 60 * 15;

    private static final long DEFAULT_WORK_TIME = 1000 * 60 * 3;
    private static final long SLEEP_TIME = 1000 * 15;

    private static final String TAG = SyncService.class.getSimpleName();
    private static final String HTTP_URL = "http://developer.android.com/index.html";
    private static final String HTTP_GET = "GET";
    private static final int HTTP_READ_TIMEOUT = 10000;
    private static final int HTTP_CONNECTION_TIMEOUT = 15000;

    static final String ARG_SYNC_TYPE = "SyncType";

    private AtomicBoolean mIsSyncRunning = new AtomicBoolean(false);

    private ExecutorService mSyncExecutor;

    private SyncType mSyncType;

    public enum SyncType {
        ALARM("Alarm Sync"), GCM("Gcm Sync");

        private String type;

        SyncType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static SyncType getSyncType(String type) {
            for (SyncType syncType : values()) {
                if (TextUtils.equals(syncType.getType(), type)) {
                    return syncType;
                }
            }
            return null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mSyncExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "SyncService onStartCommand");

        if (intent == null) {
            return START_STICKY_COMPATIBILITY;
        }

        SyncType syncType = (SyncType) intent.getSerializableExtra(ARG_SYNC_TYPE);
        if (syncType == null) {
            return START_STICKY_COMPATIBILITY;
        }

        mSyncType = syncType;

        if (!mIsSyncRunning.get()) {
            mIsSyncRunning.set(true);
            mSyncExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    saveInfoInSyncTable();

                    long beforeStart = System.currentTimeMillis();

                    while (System.currentTimeMillis() - beforeStart < DEFAULT_WORK_TIME) {

                        try {
                            URL url = new URL(HTTP_URL);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout(HTTP_READ_TIMEOUT);
                            conn.setConnectTimeout(HTTP_CONNECTION_TIMEOUT);
                            conn.setRequestMethod(HTTP_GET);
                            conn.setDoInput(true);
                            conn.connect();
                            int response = conn.getResponseCode();
                            Log.d(TAG, "The response is: " + response);
                        } catch (Exception ex) {
                            Log.e(TAG, "Sync service - problem with data loading", ex);
                        }

                        try {
                            Thread.sleep(SLEEP_TIME);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "Sync service - problem with task suspending", e);
                        }
                    }

                    mIsSyncRunning.set(false);
                    stopSelf();
                }
            });
        }

        return START_STICKY_COMPATIBILITY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSyncExecutor != null) {
            mSyncExecutor.shutdown();
            mSyncExecutor = null;
            mSyncType = null;
            mIsSyncRunning.set(false);
        }
    }

    private void saveInfoInSyncTable() {
        long previousTime = Prefs.getLongValue(this, Prefs.LAST_SYNC_TIMESTAMP);
        long currentTime = System.currentTimeMillis();
        if (mSyncType == SyncType.ALARM) {
            AlarmSyncInfo info = new AlarmSyncInfo();
            if (previousTime == 0) {
                info.timeFromLastSync = 0;
            } else {
                info.timeFromLastSync = currentTime - previousTime;
            }
            info.currentTime = currentTime;
            info.batteryLevel = getBatteryState();
            info.save();
        } else if (mSyncType == SyncType.GCM) {
            GCMSyncInfo info = new GCMSyncInfo();
            if (previousTime == 0) {
                info.timeFromLastSync = 0;
            } else {
                info.timeFromLastSync = currentTime - previousTime;
            }
            info.currentTime = currentTime;
            info.batteryLevel = getBatteryState();
            info.save();
        }
        Prefs.putLongValue(this, Prefs.LAST_SYNC_TIMESTAMP, currentTime);
    }

    private String getBatteryState() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        StringBuilder builder = new StringBuilder();
        if (level == -1 || scale == -1) {
            builder.append("50%");
        } else {
            builder.append("Battery level = ").append(((float) level / (float) scale) * 100.0f).append("%");
        }

        builder.append(", IsCharging = ").append(isCharging);
        return builder.toString();
    }
}
