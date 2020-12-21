package com.linaverde.fishingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.StatisticsFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.fragments.TournamentFragment;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.services.RequestHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class StatisticActivity extends AppCompatActivity implements TopMenuEventListener {

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        Bundle b = getIntent().getExtras();

        ContentLoadingProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.show();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        TopMenuFragment menuFragment = TopMenuFragment.newInstance(b.getString("info"));
        fragmentTransaction.add(R.id.top_menu_fragment, menuFragment);
        fragmentTransaction.commit();

        String matchId = "";
        try {
            matchId = (new JSONObject(b.getString("info"))).getString("matchId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestHelper requestHelper = new RequestHelper(this);
        requestHelper.executeGet("stats", new String[]{"matchId"}, new String[]{matchId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                Log.d("Test auth", "Request fine");
                StatisticsFragment statisticsFragment = StatisticsFragment.newInstance(json.toString());
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.content_fragment, statisticsFragment);
                fragmentTransaction.commit();
                progressBar.hide();
            }

            @Override
            public void onError(int responseCode) {
                Log.d("Test auth", "Request error with code" + responseCode);
            }
        });

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

    @Override
    public void onBackPressed() {
        finish();
    }
    
}