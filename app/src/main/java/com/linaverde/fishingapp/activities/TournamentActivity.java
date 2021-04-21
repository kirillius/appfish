package com.linaverde.fishingapp.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.fragments.TournamentFragment;
import com.linaverde.fishingapp.fragments.TournamentUserFragment;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.models.CastTimerAccumulator;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.NavigationHelper;
import com.linaverde.fishingapp.services.ProtocolHelper;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.models.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class TournamentActivity extends FragmentActivity implements TopMenuEventListener {

    DrawerLayout drawer;
    String matchId;
    ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_fragments);

        drawer = findViewById(R.id.drawer_layout);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.show();
        UserInfo userInfo = new UserInfo(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
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


        TopMenuFragment menuFragment = TopMenuFragment.newInstance(true, false);
        matchId = userInfo.getMatchId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.top_menu_fragment, menuFragment);


        if (userInfo.getUserType() == 1 || userInfo.getUserType() == 4) {
            progressBar.hide();
            TournamentFragment JTFragment = new TournamentFragment();
            fragmentTransaction.add(R.id.content_fragment, JTFragment);
            if (!fragmentManager.isDestroyed())
                fragmentTransaction.commit();
        } else {
            TournamentUserFragment JTFragment = new TournamentUserFragment();
            fragmentTransaction.add(R.id.content_fragment, JTFragment);
            if (!fragmentManager.isDestroyed())
                fragmentTransaction.commit();

            CastTimerAccumulator accumulator = CastTimerAccumulator.getInstance();
            if (!accumulator.isTimersAlreadyCreated()) {

                RequestHelper requestHelper = new RequestHelper(this);
                requestHelper.executeGet("catching", new String[]{"match", "team"}, new String[]{matchId, userInfo.getTeamId()}, new RequestListener() {
                    @Override
                    public void onComplete(JSONObject json) {
                        progressBar.hide();
                        try {
                            accumulator.createTimers(TournamentActivity.this, json.getJSONArray("rods"));
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(int responseCode) {
                        progressBar.hide();
                        DialogBuilder.createDefaultDialog(TournamentActivity.this, getLayoutInflater(),
                                TournamentActivity.this.getString(R.string.request_error), null);
                    }
                });
            } else {
                progressBar.hide();
            }
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