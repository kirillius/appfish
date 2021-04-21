package com.linaverde.fishingapp.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
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

    public void wipeData() {
        context.deleteSharedPreferences(APP_PREFERENCES);
    }

    public void saveParam(String paramId, String json, String time) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(paramId, json);
        editor.putString(paramId + "time", time);
        editor.apply();
        Log.d("Param Storage", paramId + " saved to storage");
    }

    public JSONArray getParams(String paramId) {
        if (mSettings.contains(paramId)) {
            try {
                Log.d("Param Storage", paramId + " get from storage");
                return new JSONArray(mSettings.getString(paramId, "[]"));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public String getTime(String paramId) {
        Log.d("Param Storage", paramId + " time get from storage");
        if (mSettings.contains(paramId + "time")) {
            return mSettings.getString(paramId + "time", "");
        } else {
            return null;
        }
    }

}
