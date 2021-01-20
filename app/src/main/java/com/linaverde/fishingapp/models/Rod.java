package com.linaverde.fishingapp.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Rod {
    private String id, distance, depth, attachment, rod, reel, line,
            mounting, wire, bottom, press, landmark, time;

    public Rod(JSONObject obj) {
        if (obj != null) {
            try {
                id = obj.getString("id");
                distance = obj.getString("distance");
                depth = obj.getString("depth");
                attachment = obj.getString("attachment");
                rod = obj.getString("rod");
                reel = obj.getString("reel");
                line = obj.getString("line");
                mounting = obj.getString("mounting");
                wire = obj.getString("wire");
                bottom = obj.getString("bottom");
                press = obj.getString("press");
                landmark = obj.getString("landmark");
                time = obj.getString("time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getAttachment() {
        return attachment;
    }

    public String getBottom() {
        return bottom;
    }

    public String getDepth() {
        return depth;
    }

    public String getDistance() {
        return distance;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getLine() {
        return line;
    }

    public String getMounting() {
        return mounting;
    }

    public String getPress() {
        return press;
    }

    public String getReel() {
        return reel;
    }

    public String getRod() {
        return rod;
    }

    public String getWire() {
        return wire;
    }
}
