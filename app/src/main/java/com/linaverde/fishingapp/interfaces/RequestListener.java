package com.linaverde.fishingapp.interfaces;

import org.json.JSONObject;

public interface RequestListener {

    void onComplete(JSONObject json);

    void onError(JSONObject json);
}

