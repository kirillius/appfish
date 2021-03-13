package com.linaverde.fishingapp.services;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class RodsSettingsStorage {

    private static final String APP_PREFERENCES = "rods_params";

    private SharedPreferences mSettings;
    private Context context;

    public RodsSettingsStorage(Context context) {
        this.context = context;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void wipeData(){
        context.deleteSharedPreferences(APP_PREFERENCES);
    }

    public void saveParam(String paramId, String json){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(paramId, json);
        editor.apply();
    }

    public JSONObject getParams(String paramId){
        if (mSettings.contains(paramId)) {
            try {
                return new JSONObject(mSettings.getString(paramId, ""));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
