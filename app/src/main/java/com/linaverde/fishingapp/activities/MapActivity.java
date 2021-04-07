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
import com.linaverde.fishingapp.fragments.RodsFragment;
import com.linaverde.fishingapp.fragments.TimeFragment;
import com.linaverde.fishingapp.fragments.TopMapFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.interfaces.MapRodClickedListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.RodPositionChangedListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.NavigationHelper;
import com.linaverde.fishingapp.services.ProtocolHelper;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends AppCompatActivity implements TopMenuEventListener, RodPositionChangedListener,
        MapRodClickedListener {

    ContentLoadingProgressBar progressBar;
    DrawerLayout drawer;
    RequestHelper requestHelper;
    String matchId, teamId;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_fragments);
        UserInfo userInfo = new UserInfo(this);

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

        matchId = userInfo.getMatchId();
        teamId = userInfo.getTeamId();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        RequestHelper requestHelper = new RequestHelper(this);
        requestHelper.executeGet("map", new String[]{"match", "team"}, new String[]{userInfo.getMatchId(), userInfo.getTeamId()}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        TopMapFragment TopFragment = TopMapFragment.newInstance(Integer.toString(json.getInt("sector")));
                        TimeFragment timeFragment = new TimeFragment();
                        String rods = json.getJSONArray("rods").toString();
                        MapFragment mapFragment = MapFragment.newInstance(json.toString(), rods, -1);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.add(R.id.top_menu_fragment, TopFragment);
                        fragmentTransaction.add(R.id.content_fragment, mapFragment);
                        fragmentTransaction.add(R.id.bottom_fragment, timeFragment);
                        if (!fragmentManager.isDestroyed())
                            fragmentTransaction.commit();
                    } else {
                        //DialogBuilder.createDefaultDialog(RodsActivity.this, getLayoutInflater(), json.getString("error"), null);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int responseCode) {

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
    public void rodPositionChanged(int rodId, String landmark, double distance) {
        //эта активность используется только для просмотра
    }

    @Override
    public void openSettingsList(int rodId) {
        Intent intent = new Intent(MapActivity.this, RodsSettingsActivity.class);
        Bundle args = new Bundle();
        args.putBoolean("spod", false);
        args.putInt("rodId", rodId);
        intent.putExtras(args);
        startActivity(intent);
    }

}