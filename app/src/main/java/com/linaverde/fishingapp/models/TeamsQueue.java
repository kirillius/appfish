package com.linaverde.fishingapp.models;

import org.json.JSONException;
import org.json.JSONObject;

public class TeamsQueue{
    
    private String teamId;
    private String teamName;
    private String logo;
    private int queue;
    private int sector;
    private String pin;
    private String pin2;
    
    public TeamsQueue(JSONObject obj) {
        if (obj != null) {
            try {
                this.teamId = obj.getString("teamId");
                this.teamName = obj.getString("teamName");
                this.queue = obj.getInt("queue");
                this.sector = obj.getInt("sector");
                this.logo = obj.getString("logo");
                this.pin = obj.getString("pin");
                this.pin2 = obj.getString("pin2");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    public String getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getLogo() {
        return logo;
    }

    public int getQueue() {
        return queue;
    }

    public int getSector() {
        return sector;
    }

    public String getPin() {
        return pin;
    }

    public String getPin2() {
        return pin2;
    }
}
