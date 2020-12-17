package com.linaverde.fishingapp.services;

import android.content.Context;

import com.linaverde.fishingapp.interfaces.RequestListener;



public class RequestHelper {
    Context context;
    RequestListener listener;

    public RequestHelper(Context context) {
        this.context = context;
    }
}
