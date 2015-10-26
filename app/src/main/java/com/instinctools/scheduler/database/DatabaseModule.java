package com.instinctools.scheduler.database;

import android.content.Context;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.File;
import java.util.List;

/**
 * Created by Alexandr Golovach on 23.10.2015.
 */
public class DatabaseModule {

    public static void init(@NonNull Context context) {
        FlowManager.init(context.getApplicationContext());
    }

    public static void destroy(@NonNull Context context) {
        FlowManager.destroy();
    }

    public static void clearTable(Class cl) {
        Delete.table(cl);
    }

    public static boolean exists(@NonNull Context context) {
        try {
            File dbFile = context.getApplicationContext().getDatabasePath(getDatabaseFullName());
            return dbFile.exists();
        } catch (Exception exception) {
            return false;
        }
    }

    @NonNull
    private static String getDatabaseFullName() {
        return Database.NAME + ".db";
    }

    public static List<AlarmSyncInfo> getAlarmSyncTableInfo() {
        return new Select().from(AlarmSyncInfo.class).queryList();
    }

    public static List<GCMSyncInfo> getGCMSyncTableInfo() {
        return new Select().from(GCMSyncInfo.class).queryList();
    }
}
