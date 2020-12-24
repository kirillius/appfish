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
    private Bitmap logo;
    private String pin;
    private String captainId;
    private String captainName;
    private Bitmap captainPhoto;
    private String assistantId;
    private String assistantName;
    private Bitmap assistantPhoto;

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
                    logo = decodeToImage(obj.getString("logo"));
                }

                if (obj.getString("captainPhoto").equals("")){
                    captainPhoto = null;
                } else {
                    captainPhoto = decodeToImage(obj.getString("captainPhoto"));
                }

                if (obj.getString("assistantPhoto").equals("")){
                    assistantPhoto = null;
                } else {
                    assistantPhoto = decodeToImage(obj.getString("assistantPhoto"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static Bitmap decodeToImage(String encodedImage) {

        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Bitmap getLogo() {
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

    public Bitmap getCaptainPhoto() {
        return captainPhoto;
    }

    public String getAssistantId() {
        return assistantId;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public Bitmap getAssistantPhoto() {
        return assistantPhoto;
    }
}
