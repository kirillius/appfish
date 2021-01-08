package com.linaverde.fishingapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class Stage {

    private String id;
    private String name;
    private int status;

    public Stage (JSONObject obj){
        if (obj != null) {
            try {
                id = obj.getString("stageId");
                name = obj.getString("stageName");
                status = obj.getInt("isClosed");
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

    public int getStatus() {
        return status;
    }
}
