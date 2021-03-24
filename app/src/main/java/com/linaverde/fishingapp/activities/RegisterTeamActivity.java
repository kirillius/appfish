package com.linaverde.fishingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.DetailedStatsFragment;
import com.linaverde.fishingapp.fragments.LogoTopMenuFragment;
import com.linaverde.fishingapp.fragments.RegisterOneTeamFragment;
import com.linaverde.fishingapp.fragments.RegisterTeamListFragment;
import com.linaverde.fishingapp.fragments.StatisticsFragment;
import com.linaverde.fishingapp.fragments.TimeFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.fragments.ViolationsFragment;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.IOnBackPressed;
import com.linaverde.fishingapp.interfaces.OneTeamClickListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.StatisticTeamNameClicked;
import com.linaverde.fishingapp.interfaces.TeamListClickListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.models.Team;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.NavigationHelper;
import com.linaverde.fishingapp.services.ProtocolHelper;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterTeamActivity extends AppCompatActivity implements TopMenuEventListener, TeamListClickListener, OneTeamClickListener, StatisticTeamNameClicked {

    DrawerLayout drawer;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    FragmentContainerView bottomFragmentContainer;
    ContentLoadingProgressBar progressBar;

    RequestHelper requestHelper;

    String matchId, matchName;
    Boolean showButtons = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_fragments);

        Bundle b = getIntent().getExtras();
        showButtons = b.getBoolean("showButtons");
        drawer = findViewById(R.id.drawer_layout);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.show();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationHelper.onMenuItemClicked(getApplicationContext(), item.getItemId(), drawer);
                return false;
            }
        });

        bottomFragmentContainer = findViewById(R.id.bottom_fragment);

        TopMenuFragment menuFragment = TopMenuFragment.newInstance(true);
        TimeFragment timeFragment = new TimeFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.top_menu_fragment, menuFragment);
        fragmentTransaction.add(R.id.bottom_fragment, timeFragment);
        if (!fragmentManager.isDestroyed())
            fragmentTransaction.commit();

        UserInfo userInfo = new UserInfo(RegisterTeamActivity.this);
        matchId = userInfo.getMatchId();
        matchName = userInfo.getMatchName();

        requestHelper = new RequestHelper(this);
        requestHelper.executeGet("teams", new String[]{"match"}, new String[]{matchId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                RegisterTeamListFragment RTFragment = RegisterTeamListFragment.newInstance(json.toString(), showButtons);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.content_fragment, RTFragment);
                if (!fragmentManager.isDestroyed())
                    fragmentTransaction.commit();
                progressBar.hide();
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
            }
        });
    }

    private void updateTeamList() {
        progressBar.show();
        requestHelper.executeGet("teams", new String[]{"match"}, new String[]{matchId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                RegisterTeamListFragment RTFragment = null;
                RTFragment = RegisterTeamListFragment.newInstance(json.toString(), showButtons);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_fragment, RTFragment);
                if (!fragmentManager.isDestroyed())
                    fragmentTransaction.commit();
                progressBar.hide();
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
            }
        });
    }

    @Override
    public void onTeamClicked(Team selectedTeam) {
        progressBar.show();
        requestHelper.executeGet("teams", new String[]{"match", "team"}, new String[]{matchId, selectedTeam.getId()}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        RegisterOneTeamFragment ROTFragment = RegisterOneTeamFragment.newInstance( selectedTeam.getId(), json.toString(), selectedTeam.getCheckIn(), showButtons);
                        Log.d("On team clicked", selectedTeam.getName());
                        LogoTopMenuFragment LTMFragment = LogoTopMenuFragment.newInstance(selectedTeam.getLogo(), selectedTeam.getName());
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.content_fragment, ROTFragment);
                        fragmentTransaction.addToBackStack("RegisterTeamFragment");
                        fragmentTransaction.replace(R.id.top_menu_fragment, LTMFragment);
                        fragmentTransaction.addToBackStack("LogoFragment");
                        if (!fragmentManager.isDestroyed())
                            fragmentTransaction.commit();
                        bottomFragmentContainer.setVisibility(View.GONE);
                    } else {
                        DialogBuilder.createDefaultDialog(RegisterTeamActivity.this, getLayoutInflater(), json.getString("error"), null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(RegisterTeamActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        });
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
                        DialogBuilder.createDefaultDialog(RegisterTeamActivity.this, getLayoutInflater(), json.getString("error"), null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(RegisterTeamActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
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

                statisticsFragment = StatisticsFragment.newInstance(json.toString(), matchName, false);
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
                DialogBuilder.createDefaultDialog(RegisterTeamActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        });
    }

    @Override
    public void teamRegistered(String teamId, int newStatus) {
        progressBar.show();

            requestHelper.executePost("teamcheckin", new String[]{"match", "team", "status"}, new String[]{matchId, teamId, Integer.toString(newStatus)}, null, new RequestListener() {
                @Override
                public void onComplete(JSONObject json) {
                    progressBar.hide();
                    try {
                        String error = json.getString("error");
                        if (!error.equals("") && !error.equals("null")) {
                            DialogBuilder.createDefaultDialog(RegisterTeamActivity.this, getLayoutInflater(), error, null);
                        } else {
                            DialogBuilder.createDefaultDialog(RegisterTeamActivity.this, getLayoutInflater(), getString(R.string.reg_status_updated), new CompleteActionListener() {
                                @Override
                                public void onOk(String input) {
                                    fragmentManager.popBackStack();
                                    updateTeamList();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(int responseCode) {
                    progressBar.hide();
                    DialogBuilder.createDefaultDialog(RegisterTeamActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
                }
            });
    }

    @Override
    public void teamClicked(String teamId, String teamName) {
//        progressBar.show();
//        requestHelper.executeGet("statsdetail", new String[]{"match", "team"}, new String[]{matchId, teamId}, new RequestListener() {
//            @Override
//            public void onComplete(JSONObject json) {
//                progressBar.hide();
//                DetailedStatsFragment DSFragment = DetailedStatsFragment.newInstance(json.toString(), teamName);
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                fragmentTransaction.replace(R.id.content_fragment, DSFragment);
//                fragmentTransaction.addToBackStack("StatisticFragment");
//                if (!fragmentManager.isDestroyed())
//                    fragmentTransaction.commit();
//            }
//
//            @Override
//            public void onError(int responseCode) {
//                progressBar.hide();
//                DialogBuilder.createDefaultDialog(RegisterTeamActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
//            }
//        });
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
                if (count == 1) {
                    bottomFragmentContainer.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}