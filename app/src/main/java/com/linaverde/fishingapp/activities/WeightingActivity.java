package com.linaverde.fishingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.RegisterTeamListFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.fragments.WeightingTeamListFragment;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TeamListClickListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.models.Team;
import com.linaverde.fishingapp.services.RequestHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class WeightingActivity extends AppCompatActivity implements TopMenuEventListener, TeamListClickListener {

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    FragmentContainerView bottomFragmentContainer;
    ContentLoadingProgressBar progressBar;

    RequestHelper requestHelper;

    String matchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_fragments);

        Bundle b = getIntent().getExtras();

        progressBar = findViewById(R.id.progress_bar);
        progressBar.show();

        bottomFragmentContainer = findViewById(R.id.bottom_fragment);

        TopMenuFragment menuFragment = TopMenuFragment.newInstance(null);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.top_menu_fragment, menuFragment);
        fragmentTransaction.commit();

        try {
            matchId = (new JSONObject(b.getString("info"))).getString("matchId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestHelper = new RequestHelper(this);
        requestHelper.executeGet("queue", new String[]{"match"}, new String[]{matchId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                WeightingTeamListFragment WTLragment = null;
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")){
                        progressBar.hide();
                        WTLragment = WeightingTeamListFragment.newInstance(json.toString(),
                                (new JSONObject(b.getString("info"))).getString("matchName"), matchId);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(R.id.content_fragment, WTLragment);
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
    public void onTeamClicked(Team selectedTeam) {

    }

    @Override
    public void onMenuClick() {

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

    }

    @Override
    public void onSyncClick() {

    }

}