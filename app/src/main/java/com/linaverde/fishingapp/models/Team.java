package com.linaverde.fishingapp.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;

public class Team {

    private String id;
    private String name;
    private String logo;
    private String pin;
    private String captainId;
    private String captainName;
    private String captainPhoto;
    private String assistantId;
    private String assistantName;
    private String assistantPhoto;

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

                if (obj.getString("captainPhoto").equals("")){
                    captainPhoto = null;
                } else {
                    captainPhoto = obj.getString("captainPhoto");
                }

                if (obj.getString("assistantPhoto").equals("")){
                    assistantPhoto = null;
                } else {
                    assistantPhoto = obj.getString("assistantPhoto");
                }

                captainDocuments = obj.getJSONObject("captainDocs");
                assistantDocuments = obj.getJSONObject("assistantDocs");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    public String getCaptainPhoto() {
        return captainPhoto;
    }

    public String getAssistantId() {
        return assistantId;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public String getAssistantPhoto() {
        return assistantPhoto;
    }

    public JSONObject getCaptainDocuments() {
        return captainDocuments;
    }

    public JSONObject getAssistantDocuments() {
        return assistantDocuments;
    }
}
