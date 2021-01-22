package com.linaverde.fishingapp.services;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfo {
    private static final String APP_PREFERENCES = "fishing_app_settings";
    public static final String APP_PREFERENCES_USER_LOGIN = "login";
    public static final String APP_PREFERENCES_USER_PASSWORD = "password";
    public static final String APP_PREFERENCES_USER_NAME = "userName";
    public static final String APP_PREFERENCES_USER_TYPE = "0";
    public static final String APP_PREFERENCES_MATCH_ID = "mathcId";
    public static final String APP_PREFERENCES_MATCH_NAME = "mathcName";
    public static final String APP_PREFERENCES_POND = "pond";
    public static final String APP_PREFERENCES_TEAM_ID = "teamId";
    public static final String APP_PREFERENCES_USER_CAPTION = "userCaption";

    private SharedPreferences mSettings;
    private Context context;

    public UserInfo(Context context) {
        this.context = context;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void clearUserInfo() {
        mSettings.edit().remove(APP_PREFERENCES_USER_LOGIN).apply();
        mSettings.edit().remove(APP_PREFERENCES_USER_NAME).apply();
        mSettings.edit().remove(APP_PREFERENCES_USER_PASSWORD).apply();
        mSettings.edit().remove(APP_PREFERENCES_USER_TYPE).apply();
        mSettings.edit().remove(APP_PREFERENCES_POND).apply();
        mSettings.edit().remove(APP_PREFERENCES_TEAM_ID).apply();
        mSettings.edit().remove(APP_PREFERENCES_MATCH_ID).apply();
        mSettings.edit().remove(APP_PREFERENCES_MATCH_NAME).apply();
        mSettings.edit().remove(APP_PREFERENCES_USER_CAPTION).apply();
    }

    public void saveUser(String login, String password, String userName, int type, String pond,
                         String matchId, String matchName, String teamId, String caption){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_USER_LOGIN, login);
        editor.putString(APP_PREFERENCES_USER_PASSWORD, password);
        editor.putString(APP_PREFERENCES_USER_NAME, userName);
        editor.putInt(APP_PREFERENCES_USER_TYPE, type);
        editor.putString(APP_PREFERENCES_POND, pond);
        editor.putString(APP_PREFERENCES_MATCH_ID, matchId);
        editor.putString(APP_PREFERENCES_MATCH_NAME, matchName);
        editor.putString(APP_PREFERENCES_TEAM_ID, teamId);
        editor.putString(APP_PREFERENCES_USER_CAPTION, caption);
        editor.apply();
    }

    public String getCaption() {
        if (mSettings.contains(APP_PREFERENCES_USER_CAPTION)) {
            return mSettings.getString(APP_PREFERENCES_USER_CAPTION, "");
        } else {
            return null;
        }
    }

    public String getLogin() {
        if (mSettings.contains(APP_PREFERENCES_USER_LOGIN)) {
            return mSettings.getString(APP_PREFERENCES_USER_LOGIN, "");
        } else {
            return null;
        }
    }

    public String getPassword() {
        if (mSettings.contains(APP_PREFERENCES_USER_PASSWORD)) {
            return mSettings.getString(APP_PREFERENCES_USER_PASSWORD, "");
        } else {
            return null;
        }
    }

    public String getUserName() {
        if (mSettings.contains(APP_PREFERENCES_USER_NAME)) {
            return mSettings.getString(APP_PREFERENCES_USER_NAME, "");
        } else {
            return null;
        }
    }
    public int getUserType() {
        if (mSettings.contains(APP_PREFERENCES_USER_TYPE)) {
            return mSettings.getInt(APP_PREFERENCES_USER_TYPE, -1);
        } else {
            return -1;
        }
    }


    public String getPond() {
        if (mSettings.contains(APP_PREFERENCES_POND)) {
            return mSettings.getString(APP_PREFERENCES_POND, "");
        } else {
            return null;
        }
    }

    public String getMatchId() {
        if (mSettings.contains(APP_PREFERENCES_MATCH_ID)) {
            return mSettings.getString(APP_PREFERENCES_MATCH_ID, "");
        } else {
            return null;
        }
    }

    public String getMatchName() {
        if (mSettings.contains(APP_PREFERENCES_MATCH_NAME)) {
            return mSettings.getString(APP_PREFERENCES_MATCH_NAME, "");
        } else {
            return null;
        }
    }

    public String getTeamId() {
        if (mSettings.contains(APP_PREFERENCES_TEAM_ID)) {
            return mSettings.getString(APP_PREFERENCES_TEAM_ID, "");
        } else {
            return null;
        }
    }


}
