package com.linaverde.fishingapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Fish implements Comparable<Fish> {

    private String id;
    private String fishId;
    //private String name;
    private String time;
    private String date;
    private int weight;

    public Fish(JSONObject obj) {
        if (obj != null) {
            try {
                id = obj.getString("id");
                fishId = obj.getString("fishId");
                //name = obj.getString("fishName");
                time = obj.getString("time");
                weight = obj.getInt("weight");
                date = time.substring(0, time.indexOf("T"));
                time = time.substring(time.indexOf("T") + 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Fish(String fishId) {
        this.fishId = fishId;
        //name = "";
        time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        weight = 0;
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        id = "";
    }

//    public String getName() {
//        return name;
//    }

    public String getFishId() {
        return fishId;
    }

    public String getTime() {
        return time;
    }

    public int getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    public String getDateTime(){
        return date+"T"+time;
    }

    public void setFishId(String fishId) {
        this.fishId = fishId;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }


    public String toString(){
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
            object.put("fishId", fishId);
            object.put("time", this.getDateTime());
            object.put("weight", weight);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }

    @Override
    public int compareTo(Fish another) {
        return this.time.compareTo(another.time);
    }
}
