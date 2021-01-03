package com.linaverde.fishingapp.activities;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.TournamentFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;

public class TournamentActivity extends FragmentActivity implements TopMenuEventListener {

    TournamentFragment tournamentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_fragments);

        Bundle b = getIntent().getExtras();
        TournamentFragment JTFragment = TournamentFragment.newInstance(b.getString("info"));
        TopMenuFragment menuFragment = TopMenuFragment.newInstance(b.getString("info"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_fragment, JTFragment);
        fragmentTransaction.add(R.id.top_menu_fragment, menuFragment);
        fragmentTransaction.commit();

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