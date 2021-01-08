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
    private Date time;
    private int weight;

    public Fish(JSONObject obj) {
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        if (obj != null) {
            try {
                id = obj.getString("fishId");
                name = obj.getString("fishName");
                time =  format.parse(obj.getString("time"));
                weight = obj.getInt("weight");
            } catch (JSONException | ParseException e) {
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

    public Date getTime() {
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
