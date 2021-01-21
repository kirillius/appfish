package com.linaverde.fishingapp.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Rod {
    private String id, distance, depth, attachment, rod, reel, line,
            mounting, wire, bottom, press, landmark, time, date;

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
                date = time.substring(0, time.indexOf("T"));
                time = time.substring(time.indexOf("T") + 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getRodsAsStrings(){
        String[] rods = new String[12];
        rods[0] = distance;
        rods[1] = depth;
        rods[2] = attachment;
        rods[3] = rod;
        rods[4] = reel;
        rods[5] = line;
        rods[6] = mounting;
        rods[7] = wire;
        rods[8] = bottom;
        rods[9] = press;
        rods[10] = landmark;
        rods[11] = time.substring(0, 5);
        return rods;
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
