package com.linaverde.fishingapp.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;

public class Team implements Comparable<Team> {

    private String id;
    private String name;
    private String logo;
    private String pin;
    private boolean checkIn;

    public Team(JSONObject obj){
        if (obj != null){
            try {
                id = obj.getString("id");
                name = obj.getString("name");
                pin = obj.getString("pin");
                if (obj.getString("logo").equals("")){
                    logo = null;
                } else {
                    logo = obj.getString("logo");
                }
                checkIn = obj.getBoolean("checkin");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int compareTo(Team another) {
        return this.name.compareTo(another.name);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public String getPin() {
        return pin;
    }

    public boolean getCheckIn(){
        return checkIn;
    }
}
