package com.linaverde.fishingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.EditFishFragment;
import com.linaverde.fishingapp.fragments.LogoTopMenuFragment;
import com.linaverde.fishingapp.fragments.RodTopFragment;
import com.linaverde.fishingapp.fragments.RodsFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.fragments.ViolationsFragment;
import com.linaverde.fishingapp.fragments.WeightingFishFragment;
import com.linaverde.fishingapp.fragments.WeightingSelectedTeamFragment;
import com.linaverde.fishingapp.fragments.WeightingStagesFragment;
import com.linaverde.fishingapp.fragments.WeightingTeamListFragment;
import com.linaverde.fishingapp.interfaces.FishChangedRequestListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.interfaces.ViolationChangedRequestListener;
import com.linaverde.fishingapp.interfaces.WeightingSelectedTeamClickListener;
import com.linaverde.fishingapp.interfaces.WeightStageClickedListener;
import com.linaverde.fishingapp.interfaces.WeightTeamClickListener;
import com.linaverde.fishingapp.models.TeamsQueue;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.NavigationHelper;
import com.linaverde.fishingapp.services.ProtocolHelper;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class WeightingActivity extends AppCompatActivity implements TopMenuEventListener, WeightTeamClickListener, WeightingSelectedTeamClickListener,
        WeightStageClickedListener, FishChangedRequestListener, ViolationChangedRequestListener {

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    FragmentContainerView bottomFragmentContainer;
    ContentLoadingProgressBar progressBar;

    RequestHelper requestHelper;

    String matchId;
    String matchName;

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_fragments);

        Bundle b = getIntent().getExtras();
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

        TopMenuFragment menuFragment = TopMenuFragment.newInstance(false);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.top_menu_fragment, menuFragment);
        if (!fragmentManager.isDestroyed())
            fragmentTransaction.commit();

        try {
            matchId = (new JSONObject(b.getString("info"))).getString("matchId");
            matchName = (new JSONObject(b.getString("info"))).getString("matchName");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestHelper = new RequestHelper(this);

        requestHelper.executeGet("stages", new String[]{"match"}, new String[]{matchId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        WeightingStagesFragment WSFragment = WeightingStagesFragment.newInstance(json.getJSONArray("stages").toString(), matchName);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(R.id.content_fragment, WSFragment);
                        if (!fragmentManager.isDestroyed())
                            fragmentTransaction.commit();
                    } else {
                        DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), json.getString("error"), null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        });

    }

    @Override
    public void stageClicked(String stageId) {
        progressBar.show();
        requestHelper.executeGet("queue", new String[]{"match"}, new String[]{matchId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                WeightingTeamListFragment WTLragment = null;
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        progressBar.hide();
                        WTLragment = WeightingTeamListFragment.newInstance(json.toString(), matchName, matchId, stageId);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.content_fragment, WTLragment);
                        fragmentTransaction.addToBackStack("WeightingStage");
                        if (!fragmentManager.isDestroyed())
                            fragmentTransaction.commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
            }
        });
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

    @Override
    public void onTeamClicked(TeamsQueue selectedTeam, String stageId) {
        WeightingSelectedTeamFragment WSTFragment = WeightingSelectedTeamFragment.newInstance(selectedTeam.getSector(), selectedTeam.getTeamId(), stageId, selectedTeam.getPin());
        LogoTopMenuFragment LTMFragment = LogoTopMenuFragment.newInstance(selectedTeam.getLogo(), selectedTeam.getTeamName());
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.content_fragment, WSTFragment);
        fragmentTransaction.addToBackStack("WeightingSelected");
        fragmentTransaction.replace(R.id.top_menu_fragment, LTMFragment);
        fragmentTransaction.addToBackStack("LogoFragment");
        if (!fragmentManager.isDestroyed())
            fragmentTransaction.commit();
    }

    @Override
    public void fishClicked(String teamId, String stageId, String pin, int sector) {
        progressBar.show();
        requestHelper.executeGet("weighing", new String[]{"stage", "team"}, new String[]{stageId, teamId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        WeightingFishFragment WSFragment = WeightingFishFragment.newInstance(json.getJSONArray("weighing").toString(), json.getJSONArray("dictionary").toString(),
                                stageId, teamId, pin, sector);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.content_fragment, WSFragment);
                        fragmentTransaction.addToBackStack("WeightingStages");
                        if (!fragmentManager.isDestroyed())
                            fragmentTransaction.commit();
                    } else {
                        DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), json.getString("error"), null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        });
    }

    @Override
    public void violationClicked(String teamId, String stageId, String pin, int sector) {
        progressBar.show();
        requestHelper.executeGet("fouls", new String[]{"stage", "team"}, new String[]{stageId, teamId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                UserInfo userInfo = new UserInfo(WeightingActivity.this);
                try {
                    boolean edit = userInfo.getUserType() == 1;
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        ViolationsFragment VFragment = ViolationsFragment.newInstance(json.getJSONArray("fouls").toString(), json.getJSONArray("dictionary").toString(),
                                stageId, teamId, sector, edit);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.content_fragment, VFragment);
                        fragmentTransaction.addToBackStack("WeightingStages");
                        if (!fragmentManager.isDestroyed())
                            fragmentTransaction.commit();
                    } else {
                        DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), json.getString("error"), null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        });
    }



    @Override
    public void fishChangedRequest(String stageId, String teamId, String pin, String fish, int sector) {
        progressBar.show();
        requestHelper.executePost("weighing", new String[]{"stage", "team"}, new String[]{stageId, teamId}, fish, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        updateFishList(teamId, stageId, pin, sector);
                    } else {
                        progressBar.hide();
                        DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), json.getString("error"), null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        });
    }

    @Override
    public void fishAdded(String dict, String pin, String teamId, String stageId, int sector) {
        EditFishFragment EFFragment = EditFishFragment.newInstance(null, dict, pin, teamId, stageId, sector);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.content_fragment, EFFragment);
        fragmentTransaction.addToBackStack("WeightingFish");
        if (!fragmentManager.isDestroyed())
            fragmentTransaction.commit();
    }

    @Override
    public void fishChanged(String fish, String dict, String pin, String teamId, String stageId, int sector) {
        EditFishFragment EFFragment = EditFishFragment.newInstance(fish, dict, pin, teamId, stageId, sector);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.content_fragment, EFFragment);
        fragmentTransaction.addToBackStack("WeightingFish");
        if (!fragmentManager.isDestroyed())
            fragmentTransaction.commit();
    }


    @Override
    public void violationChangedRequest(String stageId, String teamId, String violation, int sector) {
        progressBar.show();
        requestHelper.executePost("fouls", new String[]{"stage", "team"}, new String[]{stageId, teamId}, violation, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        updateViolationList(teamId, stageId, sector);
                    } else {
                        progressBar.hide();
                        DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), json.getString("error"), null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        });
    }

    private void updateFishList(String teamId, String stageId, String pin, int sector) {
        progressBar.show();
        requestHelper.executeGet("weighing", new String[]{"stage", "team"}, new String[]{stageId, teamId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        WeightingFishFragment WSFragment = WeightingFishFragment.newInstance(json.getJSONArray("weighing").toString(), json.getJSONArray("dictionary").toString(),
                                stageId, teamId, pin, sector);
                        fragmentManager.popBackStack();
                        fragmentManager.popBackStack();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.content_fragment, WSFragment);
                        fragmentTransaction.addToBackStack("WeightingStages");
                        if (!fragmentManager.isDestroyed())
                            fragmentTransaction.commit();
                    } else {
                        DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), json.getString("error"), null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        });
    }

    private void updateViolationList(String teamId, String stageId, int sector) {
        progressBar.show();
        requestHelper.executeGet("fouls", new String[]{"stage", "team"}, new String[]{stageId, teamId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        ViolationsFragment VFragment = ViolationsFragment.newInstance(json.getJSONArray("fouls").toString(), json.getJSONArray("dictionary").toString(),
                                stageId, teamId, sector, true);
                        getSupportFragmentManager().popBackStack();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.content_fragment, VFragment);
                        fragmentTransaction.addToBackStack("WeightingStages");
                        if (!fragmentManager.isDestroyed())
                            fragmentTransaction.commit();
                    } else {
                        DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), json.getString("error"), null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(WeightingActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
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


}