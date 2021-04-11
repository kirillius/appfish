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
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.MapFragment;
import com.linaverde.fishingapp.fragments.RodTopFragment;
import com.linaverde.fishingapp.fragments.RodsDetailFragment;
import com.linaverde.fishingapp.fragments.RodsMainFragment;
import com.linaverde.fishingapp.fragments.TimeFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.RodPositionChangedListener;
import com.linaverde.fishingapp.interfaces.RodsSettingsListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.NavigationHelper;
import com.linaverde.fishingapp.services.ProtocolHelper;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONObject;

public class RodsSettingsActivity extends AppCompatActivity implements TopMenuEventListener, RodsSettingsListener, RodPositionChangedListener {

    DrawerLayout drawer;
    ContentLoadingProgressBar progressBar;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    RequestHelper requestHelper;
    UserInfo userInfo;
    String matchId;
    boolean spod = true;
    boolean needUpdate = false;
    RodsDetailFragment currentDetailedFragment;
    String rodType;
    boolean mainFirst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_fragments);
        userInfo = new UserInfo(this);
        progressBar = findViewById(R.id.progress_bar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationHelper.onMenuItemClicked(getApplicationContext(), item.getItemId(), drawer);
                return false;
            }
        });

        TopMenuFragment menuFragment = TopMenuFragment.newInstance(true, false);
        TimeFragment timeFragment = new TimeFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.top_menu_fragment, menuFragment);
        fragmentTransaction.add(R.id.bottom_fragment, timeFragment);
        if (!fragmentManager.isDestroyed())
            fragmentTransaction.commit();

        matchId = userInfo.getMatchId();

        requestHelper = new RequestHelper(this);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            spod = b.getBoolean("spod");
        } else {
            spod = true;
        }

        if (spod) {
            rodType = "spod";
        } else {
            rodType = "carp";
        }

        if (b != null && b.containsKey("rodId")) {
            mainFirst = false;
            int rodId = b.getInt("rodId");
            boolean cast = b.getBoolean("cast");
            rodsDetailedReqired(rodType, rodId, cast);
        } else {
            mainFirst = true;
            setMainFragment();
        }

    }

    public void updateMainFragment(){
        progressBar.show();
        RequestListener listener = new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                RodsMainFragment rodsMainFragment = RodsMainFragment.newInstance(spod, json.toString());
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_fragment, rodsMainFragment);
                if (!fragmentManager.isDestroyed())
                    fragmentTransaction.commit();
                progressBar.hide();
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
            }
        };

        requestHelper.executeGet("rods", new String[]{"team", "rodType", "allParams"},
                new String[]{userInfo.getTeamId(), rodType, "false"}, listener);
    }

    public void setMainFragment() {
        progressBar.show();
        RequestListener listener = new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                RodsMainFragment rodsMainFragment = RodsMainFragment.newInstance(spod, json.toString());
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.content_fragment, rodsMainFragment);
                if (!fragmentManager.isDestroyed())
                    fragmentTransaction.commit();
                progressBar.hide();
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
            }
        };

        requestHelper.executeGet("rods", new String[]{"team", "rodType", "allParams"},
                new String[]{userInfo.getTeamId(), rodType, "false"}, listener);
    }

    @Override
    public void rodsDetailedReqired(String rodType, int rodID, boolean cast) {
        progressBar.show();
        requestHelper.executeGet("rods", new String[]{"team", "rodType", "rod", "allParams"},
                new String[]{userInfo.getTeamId(), rodType, Integer.toString(rodID), "true"}, new RequestListener() {
                    @Override
                    public void onComplete(JSONObject json) {
                        needUpdate = true;
                        currentDetailedFragment = RodsDetailFragment.newInstance(json.toString(), rodType, cast);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.replace(R.id.content_fragment, currentDetailedFragment);
                        fragmentTransaction.replace(R.id.top_menu_fragment, new RodTopFragment());
                        if (mainFirst)
                            fragmentTransaction.addToBackStack("RodsMain");
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
    public void updateDetailedFragment(String rodType, int rodID, boolean cast) {
        if (!mainFirst) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        } else {
            progressBar.show();
            requestHelper.executeGet("rods", new String[]{"team", "rodType", "rod", "allParams"},
                    new String[]{userInfo.getTeamId(), rodType, Integer.toString(rodID), "true"}, new RequestListener() {
                        @Override
                        public void onComplete(JSONObject json) {
                            currentDetailedFragment = RodsDetailFragment.newInstance(json.toString(), rodType, cast);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentManager.popBackStack();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.replace(R.id.content_fragment, currentDetailedFragment);
                            fragmentTransaction.replace(R.id.top_menu_fragment, new RodTopFragment());
                            if (mainFirst)
                                fragmentTransaction.addToBackStack("RodsMain");
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
    }

    @Override
    public void sendRodsSettings(String rodType, int rodID, String newParams) {
        progressBar.show();
        requestHelper.executePost("rods", new String[]{"team", "rod", "rodType"}, new String[]{userInfo.getTeamId(), Integer.toString(rodID), rodType},
                newParams, new RequestListener() {
                    @Override
                    public void onComplete(JSONObject json) {
                        fragmentManager.popBackStack();
                        progressBar.hide();
                        if (mainFirst) {
                            setMainFragment();
                        } else {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                            //updateDetailedFragment(rodType, rodID);
                        }
                    }

                    @Override
                    public void onError(int responseCode) {
                        progressBar.hide();
                        DialogBuilder.createDefaultDialog(RodsSettingsActivity.this, getLayoutInflater(), getString(R.string.request_error),
                                new CompleteActionListener() {
                                    @Override
                                    public void onOk(String input) {
                                        Intent intent = new Intent();
                                        setResult(RESULT_CANCELED, intent);
                                        finish();
                                        //updateDetailedFragment(rodType, rodID);
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                    }
                });
    }

    @Override
    public void openMapFragment(MapFragment MapFragment, RodPositionChangedListener listener) {
        MapFragment.setPositionChangedListener(listener);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.content_fragment, MapFragment);
        fragmentTransaction.addToBackStack("Map");
        if (!fragmentManager.isDestroyed())
            fragmentTransaction.commit();
    }

    @Override
    public void rodPositionChanged(int rodId, String landmark, double distance) {
        fragmentManager.popBackStack();
        currentDetailedFragment.paramChanged("LANDMARK", landmark);
        currentDetailedFragment.paramChanged("DISTANCE", Double.toString(distance));
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
            if (count == 1 && mainFirst) {
                getSupportFragmentManager().popBackStack();
                updateMainFragment();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }
}