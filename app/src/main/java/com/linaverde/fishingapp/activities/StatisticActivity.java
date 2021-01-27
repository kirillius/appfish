package com.linaverde.fishingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.DetailedStatsFragment;
import com.linaverde.fishingapp.fragments.LogoTopMenuFragment;
import com.linaverde.fishingapp.fragments.RegisterOneTeamFragment;
import com.linaverde.fishingapp.fragments.StatisticsFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.fragments.ViolationsFragment;
import com.linaverde.fishingapp.interfaces.OneTeamClickListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.StatisticTeamNameClicked;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.models.Team;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.NavigationHelper;
import com.linaverde.fishingapp.services.ProtocolHelper;
import com.linaverde.fishingapp.services.RequestHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class StatisticActivity extends AppCompatActivity implements TopMenuEventListener, StatisticTeamNameClicked {

    FragmentTransaction fragmentTransaction;
    DrawerLayout drawer;
    String matchId, teamId, matchName;
    ContentLoadingProgressBar progressBar;
    RequestHelper requestHelper;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_fragments);

        Bundle b = getIntent().getExtras();
        drawer = findViewById(R.id.drawer_layout);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.show();
        requestHelper = new RequestHelper(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationHelper.onMenuItemClicked(getApplicationContext(), item.getItemId(), drawer);
                return false;
            }
        });

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        TopMenuFragment menuFragment = TopMenuFragment.newInstance(true);
        fragmentTransaction.add(R.id.top_menu_fragment, menuFragment);
        fragmentTransaction.commit();

        matchId = b.getString("matchId");
        teamId = b.getString("teamId");
        matchName = b.getString("matchName");

        if (teamId.equals("")) {
            RequestHelper requestHelper = new RequestHelper(this);
            requestHelper.executeGet("stats", new String[]{"match"}, new String[]{matchId}, new RequestListener() {
                @Override
                public void onComplete(JSONObject json) {
                    Log.d("Test auth", "Request fine");
                    StatisticsFragment statisticsFragment = null;

                    statisticsFragment = StatisticsFragment.newInstance(json.toString(), matchName, true);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.content_fragment, statisticsFragment);
                    if (!fragmentManager.isDestroyed())
                        fragmentTransaction.commit();
                    progressBar.hide();
                }

                @Override
                public void onError(int responseCode) {
                    DialogBuilder.createDefaultDialog(StatisticActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
                }
            });
        } else {
            requestHelper.executeGet("stats", new String[]{"match", "team"}, new String[]{matchId, teamId}, new RequestListener() {
                @Override
                public void onComplete(JSONObject json) {
                    Log.d("Test auth", "Request fine");
                    StatisticsFragment statisticsFragment = null;

                    statisticsFragment = StatisticsFragment.newInstance(json.toString(), matchName, false);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.content_fragment, statisticsFragment);
                    if (!fragmentManager.isDestroyed())
                        fragmentTransaction.commit();
                    progressBar.hide();
                }

                @Override
                public void onError(int responseCode) {
                    progressBar.hide();
                    DialogBuilder.createDefaultDialog(StatisticActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
                }
            });
        }
    }

    @Override
    public void teamClicked(String teamId, String teamName) {
        progressBar.show();
        requestHelper.executeGet("statsdetail", new String[]{"match", "team"}, new String[]{matchId, teamId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                DetailedStatsFragment DSFragment = DetailedStatsFragment.newInstance(json.toString(), teamName);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.content_fragment, DSFragment);
                fragmentTransaction.addToBackStack("StatisticFragment");
                if (!fragmentManager.isDestroyed())
                    fragmentTransaction.commit();
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
            }
        });
    }

    @Override
    public void onMenuClick() {
        drawer.openDrawer(GravityCompat.START);
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
        ProtocolHelper.sendProtocols(this, matchId, progressBar);
    }

    @Override
    public void onSyncClick() {
        ProtocolHelper.getProtocol(this, matchId, progressBar);
    }

    @Override
    public void onBackPressed() {
        progressBar.hide();
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}