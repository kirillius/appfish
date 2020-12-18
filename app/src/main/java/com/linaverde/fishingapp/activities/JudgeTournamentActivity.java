package com.linaverde.fishingapp.activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.services.RequestHelper;

import org.json.JSONObject;

public class JudgeTournamentActivity extends FragmentActivity implements TopMenuEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);
        /*RequestHelper requestHelper = new RequestHelper(this);
        requestHelper.executeGet("session", new String[]{"username", "password"}, new String[]{"username", "password"}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                Log.d("Test request","Request fine");
            }

            @Override
            public void onError(JSONObject json) {
                Log.d("Test request","Request error");
            }
        });*/
    }

    @Override
    public void onMenuClick() {

    }

    @Override
    public void onSettingsClick() {

    }

    @Override
    public void onGPSClick() {

    }

    @Override
    public void onChatClick() {

    }

    @Override
    public void onMessageClick() {

    }

    @Override
    public void onSyncClick() {

    }
}