package com.linaverde.fishingapp.services;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfo {
    private static final String APP_PREFERENCES = "fishing_app_settings";
    public static final String APP_PREFERENCES_USER_LOGIN = "login";
    public static final String APP_PREFERENCES_USER_PASSWORD = "password";
    public static final String APP_PREFERENCES_USER_NAME = "name";
    public static final String APP_PREFERENCES_USER_TYPE = "0";

    private SharedPreferences mSettings;
    private Context context;

    public UserInfo(Context context) {
        this.context = context;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void clearUserInfo() {
        mSettings.edit().remove(APP_PREFERENCES_USER_LOGIN).commit();
        mSettings.edit().remove(APP_PREFERENCES_USER_NAME).commit();
        mSettings.edit().remove(APP_PREFERENCES_USER_PASSWORD).commit();
        mSettings.edit().remove(APP_PREFERENCES_USER_TYPE).commit();
    }

    public void saveUser(String login, String password, String name, int type){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_USER_LOGIN, login);
        editor.putString(APP_PREFERENCES_USER_PASSWORD, password);
        editor.putString(APP_PREFERENCES_USER_NAME, name);
        editor.putString(APP_PREFERENCES_USER_TYPE, String.valueOf(type));
        editor.apply();
    }
}
