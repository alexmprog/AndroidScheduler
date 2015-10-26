package com.instinctools.scheduler.database;

/**
 * Created by Alexandr Golovach on 23.10.2015.
 */
@com.raizlabs.android.dbflow.annotation.Database(name = Database.NAME, version = Database.VERSION,
        foreignKeysSupported = true)
public class Database {

    /**
     * Database name.
     */
    public static final String NAME = "Scheduler";

    /**
     * Database version.
     */
    public static final int VERSION = 1;
}
