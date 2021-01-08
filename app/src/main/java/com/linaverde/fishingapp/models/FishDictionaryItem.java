package com.linaverde.fishingapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class FishDictionaryItem implements Comparable<FishDictionaryItem> {
    private String id;
    private String name;

    public FishDictionaryItem(JSONObject obj) {
        if (obj != null) {
            try {
                id = obj.getString("fishId");
                name = obj.getString("fishName");
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

    @Override
    public int compareTo(FishDictionaryItem another) {
        return this.name.compareTo(another.name);
    }
}
