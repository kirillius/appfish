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
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.DrawQueueFragment;
import com.linaverde.fishingapp.fragments.RegisterTeamListFragment;
import com.linaverde.fishingapp.fragments.TimeFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.QueueUpdateListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TeamListClickListener;
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

public class QueueActivity extends AppCompatActivity implements TopMenuEventListener, QueueUpdateListener {

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
        drawer = findViewById(R.id.drawer_layout);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.show();
        userInfo = new UserInfo(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationHelper.onMenuItemClicked(getApplicationContext(), item.getItemId(), drawer);
                return false;
            }
        });

        TopMenuFragment menuFragment = TopMenuFragment.newInstance(false, false);
        TimeFragment timeFragment = new TimeFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.top_menu_fragment, menuFragment);
        fragmentTransaction.add(R.id.bottom_fragment, timeFragment);
        if (!fragmentManager.isDestroyed())
            fragmentTransaction.commit();

        matchId = userInfo.getMatchId();

        requestHelper = new RequestHelper(this);
        setNewQueueFragment();

    }

    public void setNewQueueFragment() {
        progressBar.show();
        requestHelper.executeGet("queue", new String[]{"match"}, new String[]{matchId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                DrawQueueFragment DQFragment = null;

                DQFragment = DrawQueueFragment.newInstance(json.toString(),
                        userInfo.getMatchName(), matchId);

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_fragment, DQFragment);
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

    CompleteActionListener confirmListener;
    RequestListener requestListener;

    @Override
    public void update(TeamsQueue team, String input) {
        progressBar.show();
        RequestHelper requestHelper = new RequestHelper(this);
        JSONObject object = new JSONObject();
        try {
            object.put("teamId", team.getTeamId());
            object.put("teamName", team.getTeamName());
            object.put("sector", team.getSector());
            object.put("queue", Integer.parseInt(input));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        confirmListener = new CompleteActionListener() {
            @Override
            public void onOk(String input) {
                progressBar.show();
                requestHelper.executePost("queue", new String[]{"match", "confirm"}, new String[]{matchId, "true"},
                        object.toString(), requestListener);
            }

            @Override
            public void onCancel() {

            }
        };

        requestListener = new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        progressBar.hide();
                        setNewQueueFragment();
                    } else {
                        progressBar.hide();
                        if (json.getBoolean("needConfirm")) {
                            DialogBuilder.createTwoButtons(QueueActivity.this, getLayoutInflater(), json.getString("error") + ". " + getString(R.string.draw_confirm), confirmListener);
                        } else {
                            DialogBuilder.createDefaultDialog(QueueActivity.this, getLayoutInflater(), json.getString("error"), null);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(QueueActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
            }
        };

        requestHelper.executePost("queue", new String[]{"match"}, new String[]{matchId},
                object.toString(), requestListener);

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