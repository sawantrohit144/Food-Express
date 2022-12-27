package com.yummy.foodonmytrain;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesManager {
    public static final String IRCTCFOODEXPRESS_APP_PREFERENCES = "IRCTCFOODEXPRESS_APP_PREFERENCES";

    public static final String IS_USER_LOGIN = "IS_USER_LOGIN";
    public static final String IS_USER_CUSTOMER = "IS_USER_CUSTOMER";
    public static final String IS_PROFILE_FILLED="IS_PROFILE_FILLED";
    public static final String USER_NUMBER="USER_NUMBER";

    private static SharedPreferencesManager mainSharedPreferencesManager;

    private SharedPreferencesManager() {
    }

    private static SharedPreferences getSharedPreferences() {
        return IRCTCFOODEXPRESSApplication.getIRCTCFOODEXPRESSApplicationContext().getSharedPreferences(IRCTCFOODEXPRESS_APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesManager getInstance() {
        if (mainSharedPreferencesManager == null)
            mainSharedPreferencesManager = new SharedPreferencesManager();

        return mainSharedPreferencesManager;
    }

    public static void removePreference(String key) {
        SharedPreferences prefs = getSharedPreferences();
        prefs.edit().remove(key);
    }

    public static void clearPreference() {
        SharedPreferences prefs = getSharedPreferences();
        SharedPreferences.Editor e = prefs.edit();
        e.clear();
        e.commit();
    }

    public static void store(String key, String value) {
        SharedPreferences prefs = getSharedPreferences();
        SharedPreferences.Editor e = prefs.edit();
        e.putString(key, value);
        e.commit();
    }


    public static String get(String key, String defaultValue) {
        SharedPreferences prefs = getSharedPreferences();
        String s = prefs.getString(key, defaultValue);
        return s;
    }

    public static void store(String key, boolean value) {
        SharedPreferences prefs = getSharedPreferences();
        SharedPreferences.Editor e = prefs.edit();
        e.putBoolean(key, value);
        e.commit();
    }


    public static boolean get(String key, boolean defaultValue) {
        SharedPreferences prefs = getSharedPreferences();
        boolean s = prefs.getBoolean(key, defaultValue);
        return s;
    }

    public static void delete(String key) {
        SharedPreferences prefs = getSharedPreferences();
        SharedPreferences.Editor e = prefs.edit();
        e.remove(key);
        e.apply();
    }
}

