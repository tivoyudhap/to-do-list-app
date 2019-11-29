package com.mastip.personaltodolist.utilities;

import android.content.SharedPreferences;

/**
 * Created by HateLogcatError on 8/29/2017.
 */

public class CustomSharedPreferences {

    private static SharedPreferences sharedPreferences;

    public static void setSharedPreferences(SharedPreferences sharedPreferences) {
        CustomSharedPreferences.sharedPreferences = sharedPreferences;
    }

    public String getDataString(String key) {
        if(sharedPreferences == null) {
            return "";
        }
        else {
            return sharedPreferences.getString(key, "");
        }
    }

    public int getDataInt(String key) {
        if(sharedPreferences == null) {
            return 0;
        }
        else {
            return sharedPreferences.getInt(key, 0);
        }
    }

    public void putDataString(String key, String value) {
        if(sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public void deleteDataString(String key) {
        if(sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.commit();
        }
    }
}
