package com.skripsi.sistemrumah.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesProvider {
    private static SharedPreferencesProvider instance = new SharedPreferencesProvider();
    private static SharedPreferences preferences;

    public static SharedPreferencesProvider getInstance() {
        return instance;
    }
    SharedPreferences.Editor editor;

    private final String pref_id_user = "pref_id_user";
    private final String pref_is_admin = "pref_is_admin";
    private final String pref_user_name = "pref_user_name";
    private final String pref_fcm_token = "pref_fcm_token";
    private final String pref_controller = "pref_controller";
    private final String pref_type = "pref_type";

    public void clearSession(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public void set_pref_id_user(Context context, String datas) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putString(pref_id_user, datas);
        editor.commit();
    }
    public String get_pref_id_user(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(pref_id_user, "");
    }

    public void set_pref_is_admin(Context context, Boolean datas) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putBoolean(pref_is_admin, datas);
        editor.commit();
    }
    public Boolean get_pref_is_admin(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(pref_is_admin, false);
    }

    public void set_pref_user_name(Context context, String datas) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putString(pref_user_name, datas);
        editor.commit();
    }
    public String get_pref_user_name(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(pref_user_name, "");
    }

    public void set_pref_fcm_token(Context context, String datas) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putString(pref_fcm_token, datas);
        editor.commit();
    }

    public String get_pref_fcm_token(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(pref_fcm_token, "");
    }

    public void set_pref_controller(Context context, boolean datas) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putBoolean(pref_controller, datas);
        editor.commit();
    }

    public boolean get_pref_controller(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(pref_controller, false);
    }

    public void set_pref_type(Context context, String datas) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putString(pref_type, datas);
        editor.commit();
    }

    public String get_pref_type(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(pref_type, "");
    }

}