package com.linaverde.fishingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;

public class PlayerTournamentActivity extends FragmentActivity implements TopMenuEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_tournament);
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