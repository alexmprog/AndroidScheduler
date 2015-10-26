package com.instinctools.scheduler.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alexandr Golovach on 23.10.2015.
 */
public class Prefs {

    public static final String LAST_SYNC_TIMESTAMP = "LAST_SYNC_TIMESTAMP";
    public static final String ACTIVE_SYNC_APPROACH = "ACTIVE_SYNC_APPROACH";

    public static void putLongValue(Context context, String key, long value) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                Prefs.class.getSimpleName(), Context.MODE_PRIVATE);

        if (sharedPref != null) {
            sharedPref.edit().putLong(key, value).apply();
        }
    }

    public static long getLongValue(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                Prefs.class.getSimpleName(), Context.MODE_PRIVATE);

        if (sharedPref != null) {
            return sharedPref.getLong(key, 0);
        }

        return 0;
    }

    public static void putStringValue(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                Prefs.class.getSimpleName(), Context.MODE_PRIVATE);

        if (sharedPref != null) {
            sharedPref.edit().putString(key, value).apply();
        }
    }

    public static String getStringValue(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                Prefs.class.getSimpleName(), Context.MODE_PRIVATE);

        if (sharedPref != null) {
            return sharedPref.getString(key, null);
        }

        return null;
    }
}
