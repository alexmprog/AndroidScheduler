package com.instinctools.scheduler;

import android.app.Application;

import com.instinctools.scheduler.database.DatabaseModule;

/**
 * Created by Alexandr Golovach on 23.10.2015.
 */
public class App extends Application {

    private static App sInstanse;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstanse = this;
        DatabaseModule.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sInstanse = null;
        DatabaseModule.destroy(this);
    }

    public static App get() {
        return sInstanse;
    }
}
