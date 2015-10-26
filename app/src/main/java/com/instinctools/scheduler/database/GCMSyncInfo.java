package com.instinctools.scheduler.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Alexandr Golovach on 23.10.2015.
 */
@Table(databaseName = Database.NAME, tableName = "GCMSyncTable", insertConflict = ConflictAction.REPLACE)
public class GCMSyncInfo extends BaseModel implements SyncInfo {

    @Column
    @PrimaryKey(autoincrement = true)
    public long long1;

    @Column
    public long timeFromLastSync;

    @Column
    public long currentTime;

    @Column
    public String batteryLevel;

    @Override
    public long getTimeSincePreviousSync() {
        return timeFromLastSync;
    }

    @Override
    public String getDeviceInfo() {
        return batteryLevel;
    }

    @Override
    public long getCurrentTime() {
        return currentTime;
    }
}
