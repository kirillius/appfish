package com.linaverde.fishingapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Fish implements Comparable<Fish> {

    private String id;
    private String name;
    private String time;
    private int weight;

    public Fish(JSONObject obj) {
        if (obj != null) {
            try {
                id = obj.getString("fishId");
                name = obj.getString("fishName");
                time =  obj.getString("time");
                weight = obj.getInt("weight");
                time = time.substring(time.indexOf("T")+1);
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

    public String getTime() {
        return time;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Fish another) {
        return this.time.compareTo(another.time);
    }
}
