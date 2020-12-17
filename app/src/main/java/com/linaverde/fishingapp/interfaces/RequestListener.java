package com.linaverde.fishingapp.interfaces;

public interface RequestListener {

    void onComplete(String json);

    void onError(int err);
}

