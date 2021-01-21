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
    private String captainId;
    private String captainName;
    private String assistantId;
    private String assistantName;
    private String [] captainLinks;
    private String [] assistantLinks;

    private JSONObject captainDocuments;
    private JSONObject assistantDocuments;

    public Team(JSONObject obj){
        if (obj != null){
            try {
                id = obj.getString("id");
                name = obj.getString("name");
                pin = obj.getString("pin");
                captainId = obj.getString("captainId");
                captainName = obj.getString("captainName");
                assistantId = obj.getString("assistantId");
                assistantName = obj.getString("assistantName");
                if (obj.getString("logo").equals("")){
                    logo = null;
                } else {
                    logo = obj.getString("logo");
                }
                captainDocuments = obj.getJSONObject("captainDocs");
                assistantDocuments = obj.getJSONObject("assistantDocs");

                captainLinks = new String[4];
                captainLinks[0] = obj.getString("captainLink1");
                captainLinks[1] = obj.getString("captainLink2");
                captainLinks[2] = obj.getString("captainLink3");
                captainLinks[3] = obj.getString("captainLink4");

                assistantLinks = new String[4];
                assistantLinks[0] = obj.getString("assistantLink1");
                assistantLinks[1] = obj.getString("assistantLink2");
                assistantLinks[2] = obj.getString("assistantLink3");
                assistantLinks[3] = obj.getString("assistantLink4");

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

    public String getCaptainId() {
        return captainId;
    }

    public String getCaptainName() {
        return captainName;
    }

    public String getAssistantId() {
        return assistantId;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public JSONObject getCaptainDocuments() {
        return captainDocuments;
    }

    public JSONObject getAssistantDocuments() {
        return assistantDocuments;
    }

    public String[] getCaptainLinks() {
        return captainLinks;
    }

    public String[] getAssistantLinks() {
        return assistantLinks;
    }
}
