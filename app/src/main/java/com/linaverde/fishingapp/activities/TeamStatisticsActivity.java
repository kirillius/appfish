package com.linaverde.fishingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.DetailedDayStatsFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.interfaces.DetailedStatisticDayClicked;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.NavigationHelper;
import com.linaverde.fishingapp.services.ProtocolHelper;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.models.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class TeamStatisticsActivity extends AppCompatActivity implements TopMenuEventListener, DetailedStatisticDayClicked {

    FragmentTransaction fragmentTransaction;
    DrawerLayout drawer;
    String matchId, teamId, matchName;
    ContentLoadingProgressBar progressBar;
    RequestHelper requestHelper;
    FragmentManager fragmentManager;
    String selectedDay;

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
        UserInfo userInfo = new UserInfo(this);
        if(userInfo.getUserType() == 1 || userInfo.getUserType() == 4) {
            navigationView.inflateMenu(R.menu.nav_menu_judge);
        } else {
            navigationView.inflateMenu(R.menu.nav_menu_user);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationHelper.onMenuItemClicked(getApplicationContext(), item.getItemId(), drawer);
                return false;
            }
        });

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        TopMenuFragment menuFragment = TopMenuFragment.newInstance(false, false);
        fragmentTransaction.add(R.id.top_menu_fragment, menuFragment);
        fragmentTransaction.commit();

        matchId = b.getString("matchId");
        teamId = b.getString("teamId");
        matchName = b.getString("matchName");

        requestHelper.executeGet("statsdetail", new String[]{"match", "team"}, new String[]{matchId, teamId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                Log.d("Test auth", "Request fine");
                DetailedDayStatsFragment statisticsFragment = DetailedDayStatsFragment.newInstance(json.toString(), teamId);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.content_fragment, statisticsFragment);
                if (!fragmentManager.isDestroyed())
                    fragmentTransaction.commit();
                progressBar.hide();
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(TeamStatisticsActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        });
    }

    @Override
    public void dayOneClicked(String teamId) {
        selectedDay = "1";
        updateDay(teamId);
    }

    @Override
    public void dayTwoClicked(String teamId) {
        selectedDay = "2";
        updateDay(teamId);
    }

    @Override
    public void dayThreeClicked(String teamId) {
        selectedDay = "3";
        updateDay(teamId);
    }

    @Override
    public void dayFourClicked(String teamId) {
        selectedDay = "4";
        updateDay(teamId);
    }

    @Override
    public void onlineClicked(String teamId) {
        selectedDay = null;
        updateDay(teamId);
    }

    private void updateDay(String teamId) {
        progressBar.show();
        RequestListener listener = new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                String error = null;
                try {
                    error = json.getString("error");
                    if (error.equals("") || error == null || error.equals("null")) {
                        DetailedDayStatsFragment statisticsFragment = DetailedDayStatsFragment.newInstance(json.toString(), teamId);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_fragment, statisticsFragment);
                        if (!fragmentManager.isDestroyed())
                            fragmentTransaction.commit();
                    } else {
                        DialogBuilder.createDefaultDialog(TeamStatisticsActivity.this, getLayoutInflater(), error, null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int responseCode) {
                DialogBuilder.createDefaultDialog(TeamStatisticsActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        };

        if (selectedDay == null) {
            requestHelper.executeGet("statsdetail", new String[]{"match", "team"}, new String[]{matchId, teamId}, listener);
        } else {
            requestHelper.executeGet("statsdetail", new String[]{"match", "team", "day"}, new String[]{matchId, teamId, selectedDay}, listener);
        }
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
}