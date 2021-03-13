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
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.DrawQueueFragment;
import com.linaverde.fishingapp.fragments.RodTopFragment;
import com.linaverde.fishingapp.fragments.RodsDetailFragment;
import com.linaverde.fishingapp.fragments.RodsMainFragment;
import com.linaverde.fishingapp.fragments.TimeFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.IOnBackPressed;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.RodsSettingsListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.NavigationHelper;
import com.linaverde.fishingapp.services.ProtocolHelper;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONObject;

public class RodsSettingsActivity extends AppCompatActivity implements TopMenuEventListener, RodsSettingsListener {

    DrawerLayout drawer;
    ContentLoadingProgressBar progressBar;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    RequestHelper requestHelper;
    UserInfo userInfo;
    String matchId;
    boolean spod = true;

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

        TopMenuFragment menuFragment = TopMenuFragment.newInstance(true);
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

        if (!b.isEmpty())
            spod = b.getBoolean("spod");

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


        if (spod) {
            requestHelper.executeGet("rods", new String[]{"team", "rodType", "allParams"},
                    new String[]{userInfo.getTeamId(), "spod", "false"}, listener);
        } else {
            requestHelper.executeGet("rods", new String[]{"team", "rodType", "allParams"},
                    new String[]{userInfo.getTeamId(), "carp", "false"}, listener);
        }
    }

    @Override
    public void rodsDetailedReqired(String rodType, int rodID) {
        progressBar.show();
        requestHelper.executeGet("rods", new String[]{"team", "rodType", "rod", "allParams"},
                new String[]{userInfo.getTeamId(), rodType, Integer.toString(rodID), "true"}, new RequestListener() {
                    @Override
                    public void onComplete(JSONObject json) {
                        RodsDetailFragment rodsDetailFragment = RodsDetailFragment.newInstance(json.toString(), rodType);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_fragment, rodsDetailFragment);
                        fragmentTransaction.replace(R.id.top_menu_fragment, new RodTopFragment());
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
    public void updateDetailedFragment(String rodType, int rodID) {
        progressBar.show();
        requestHelper.executeGet("rods", new String[]{"team", "rodType", "rod", "allParams"},
                new String[]{userInfo.getTeamId(), rodType, Integer.toString(rodID), "true"}, new RequestListener() {
                    @Override
                    public void onComplete(JSONObject json) {
                        RodsDetailFragment rodsDetailFragment = RodsDetailFragment.newInstance(json.toString(), rodType);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_fragment, rodsDetailFragment);
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
    public void sendRodsSettings(String rodType, int rodID, String newParams) {
        progressBar.show();
        requestHelper.executePost("rods", new String[]{"team", "rod", "rodType"}, new String[]{userInfo.getTeamId(), Integer.toString(rodID), rodType},
                newParams, new RequestListener() {
                    @Override
                    public void onComplete(JSONObject json) {
//                        RodsDetailFragment rodsDetailFragment = RodsDetailFragment.newInstance(json.toString(), rodType);
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.content_fragment, rodsDetailFragment);
//                        if (!fragmentManager.isDestroyed())
//                            fragmentTransaction.commit();
//                        progressBar.hide();
                        updateDetailedFragment(rodType, rodID);
                    }

                    @Override
                    public void onError(int responseCode) {
                        progressBar.hide();
                        DialogBuilder.createDefaultDialog(RodsSettingsActivity.this, getLayoutInflater(), getString(R.string.request_error),
                                new CompleteActionListener() {
                                    @Override
                                    public void onOk(String input) {
                                        updateDetailedFragment(rodType, rodID);
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
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
}