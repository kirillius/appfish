package com.linaverde.fishingapp.models;

import android.content.Context;
import android.content.SharedPreferences;

public class TimerPauseValue {

    private static final String APP_PREFERENCES = "timer_pause_value";

    private SharedPreferences mSettings;
    private Context context;

    public TimerPauseValue(Context context) {
        this.context = context;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void removeTimerValue(String rodId) {
        mSettings.edit().remove(rodId).apply();
    }

    public void saveValue(String rodId, String pausedTime){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(rodId, pausedTime);
        editor.apply();
    }

    public String getPausedTime(String rodId){
        if (mSettings.contains(rodId)) {
            return mSettings.getString(rodId, "00:00");
        } else {
            return null;
        }
    }
}
