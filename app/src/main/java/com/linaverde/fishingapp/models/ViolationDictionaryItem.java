package com.linaverde.fishingapp.models;

import org.json.JSONException;
import org.json.JSONObject;

public class ViolationDictionaryItem {

    private String id;
    private String name;
    private int sendOff;

    public ViolationDictionaryItem(JSONObject obj) {
        if (obj != null) {
            try {
                id = obj.getString("foulId");
                name = obj.getString("foulName");
                sendOff = obj.getInt("sendOff");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getSendOff() {
        return sendOff;
    }
}
