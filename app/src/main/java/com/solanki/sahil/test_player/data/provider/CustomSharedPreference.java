package com.solanki.sahil.test_player.data.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CustomSharedPreference {
    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static void setLastPosition(Context context, String lock, long key){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putLong(lock, key);
        editor.apply();
    }

    public static long getLastPosition(Context context, String lock){
        return getPreferences(context).getLong(lock,0);
    }



}
