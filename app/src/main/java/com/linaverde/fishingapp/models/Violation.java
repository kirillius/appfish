package com.linaverde.fishingapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Violation {

    private String id;
    private String violationId;
    private String name;
    private String time;
    private String date;


    public Violation(JSONObject obj) {
        if (obj != null) {
            try {
                id = obj.getString("id");
                violationId = obj.getString("foulId");
                name = obj.getString("foulName");
                time = obj.getString("time");
                date = time.substring(0, time.indexOf("T"));
                time = time.substring(time.indexOf("T") + 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Violation(String violationId) {
        this.violationId = violationId;
        //name = "";
        time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        id = "";
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getViolationId() {
        return violationId;
    }

    public void setViolationId(String violationId) {
        this.violationId = violationId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateTime(){
        return date+"T"+time;
    }

    public String toString(){
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
            object.put("foulName", name);
            object.put("foulId", violationId);
            object.put("time", this.getDateTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }
}
