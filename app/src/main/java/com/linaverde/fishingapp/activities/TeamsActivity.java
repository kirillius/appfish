package com.linaverde.fishingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.LogoTopMenuFragment;
import com.linaverde.fishingapp.fragments.RegisterOneTeamFragment;
import com.linaverde.fishingapp.fragments.StatisticsFragment;
import com.linaverde.fishingapp.fragments.TeamListFragment;
import com.linaverde.fishingapp.fragments.TimeFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.fragments.ViolationsFragment;
import com.linaverde.fishingapp.interfaces.IOnBackPressed;
import com.linaverde.fishingapp.interfaces.OneTeamClickListener;
import com.linaverde.fishingapp.interfaces.QueueUpdateListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TeamListClickListener;
import com.linaverde.fishingapp.interfaces.TeamListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.models.Team;
import com.linaverde.fishingapp.models.TeamsQueue;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.NavigationHelper;
import com.linaverde.fishingapp.services.ProtocolHelper;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class TeamsActivity extends AppCompatActivity implements TopMenuEventListener, TeamListener, OneTeamClickListener {

    DrawerLayout drawer;
    ContentLoadingProgressBar progressBar;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    RequestHelper requestHelper;
    UserInfo userInfo;
    String matchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_fragments);
        userInfo = new UserInfo(this);
        progressBar = findViewById(R.id.progress_bar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationHelper.onMenuItemClicked(getApplicationContext(), item.getItemId(), drawer);
                return false;
            }
        });

        TopMenuFragment menuFragment = TopMenuFragment.newInstance(false);
        TimeFragment timeFragment = new TimeFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.top_menu_fragment, menuFragment);
        fragmentTransaction.add(R.id.bottom_fragment, timeFragment);
        if (!fragmentManager.isDestroyed())
            fragmentTransaction.commit();

        matchId = userInfo.getMatchId();

        requestHelper = new RequestHelper(this);

        progressBar.show();
        requestHelper.executeGet("queue", new String[]{"match"}, new String[]{matchId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                TeamListFragment teamListFragment = TeamListFragment.newInstance(json.toString());
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.content_fragment, teamListFragment);
                if (!fragmentManager.isDestroyed())
                    fragmentTransaction.commit();
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(TeamsActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        });

    }

    @Override
    public void teamClicked(String teamId) {
        progressBar.show();
        requestHelper.executeGet("teams", new String[]{"match", "team"}, new String[]{matchId, teamId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                try {
                    JSONObject selectedTeam = json.getJSONObject("teams");
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        RegisterOneTeamFragment ROTFragment = RegisterOneTeamFragment.newInstance(teamId, json.toString(), false, false);
                        Log.d("On team clicked", selectedTeam.getString("name"));
                        LogoTopMenuFragment LTMFragment = LogoTopMenuFragment.newInstance(selectedTeam.getString("logo"), selectedTeam.getString("name"));
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.content_fragment, ROTFragment);
                        fragmentTransaction.addToBackStack("RegisterTeamFragment");
                        fragmentTransaction.replace(R.id.top_menu_fragment, LTMFragment);
                        fragmentTransaction.addToBackStack("LogoFragment");
                        if (!fragmentManager.isDestroyed())
                            fragmentTransaction.commit();
                    } else {
                        DialogBuilder.createDefaultDialog(TeamsActivity.this, getLayoutInflater(), json.getString("error"), null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(TeamsActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
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

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_fragment);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            //super.onBackPressed();
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                finish();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public void onViolationClicked(String teamId) {
        progressBar.show();
        requestHelper.executeGet("fouls", new String[]{"match", "team"}, new String[]{matchId, teamId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                // UserInfo userInfo = new UserInfo(RegisterTeamActivity.this);
                try {
                    //boolean edit = userInfo.getUserType() == 1;
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        ViolationsFragment VFragment = ViolationsFragment.newInstance(json.getJSONArray("fouls").toString(), json.getJSONArray("dictionary").toString(),
                                null, teamId, -1, false);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.content_fragment, VFragment);
                        fragmentTransaction.addToBackStack("WeightingStages");
                        if (!fragmentManager.isDestroyed())
                            fragmentTransaction.commit();
                    } else {
                        DialogBuilder.createDefaultDialog(TeamsActivity.this, getLayoutInflater(), json.getString("error"), null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(TeamsActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        });
    }

    @Override
    public void onStatisticsClicked(String teamId) {
        progressBar.show();
        requestHelper.executeGet("stats", new String[]{"match", "team"}, new String[]{matchId, teamId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                Log.d("Test auth", "Request fine");
                StatisticsFragment statisticsFragment = null;

                statisticsFragment = StatisticsFragment.newInstance(json.toString(), userInfo.getMatchName(), false);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_fragment, statisticsFragment);
                fragmentTransaction.addToBackStack("OneTeamFragment");
                if (!fragmentManager.isDestroyed())
                    fragmentTransaction.commit();
                progressBar.hide();
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(TeamsActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        });
    }

    @Override
    public void teamRegistered(String teamId, boolean register) {
        //в этой активности регистрация не проводится
    }
}