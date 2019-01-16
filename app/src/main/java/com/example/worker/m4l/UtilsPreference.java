package com.example.worker.m4l;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UtilsPreference {

    SharedPreferences sharedPreferences;
    Context context;

    public UtilsPreference(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
    }

    public String getSettingEmail() {
        return sharedPreferences.getString("setting_email", "");
    }

    public void setSettingEmail(String str) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("setting_email", str);
        editor.commit();
    }

    public String getSettingMove() {
        String str = "";
        str = sharedPreferences.getString("setting_move", "");
        return str;
    }

    public void setSettingMove(String str) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("setting_move", str);
        editor.commit();
    }

    public String getSettingActive() {
        return sharedPreferences.getString("setting_active", "");
    }

    public void setSettingActive(String str) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("setting_active", str);
        editor.commit();
    }
}
