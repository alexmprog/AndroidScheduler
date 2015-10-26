package com.instinctools.scheduler.database;

/**
 * Created by Alexandr Golovach on 23.10.2015.
 */
public interface SyncInfo {

    long getTimeSincePreviousSync();

    String getDeviceInfo();

    long getCurrentTime();
}
