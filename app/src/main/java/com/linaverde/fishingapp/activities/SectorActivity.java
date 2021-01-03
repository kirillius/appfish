package com.linaverde.fishingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.DrawQueueFragment;
import com.linaverde.fishingapp.fragments.DrawSectorFragment;
import com.linaverde.fishingapp.fragments.RegisterTeamListFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.interfaces.QueueUpdateListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TeamListClickListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.models.Team;
import com.linaverde.fishingapp.models.TeamsQueue;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.RequestHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class SectorActivity extends AppCompatActivity implements TopMenuEventListener, QueueUpdateListener {

    ContentLoadingProgressBar progressBar;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    RequestHelper requestHelper;

    String matchId;
    Bundle b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_fragments);
        b = getIntent().getExtras();

        progressBar = findViewById(R.id.progress_bar);
        progressBar.show();

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
        setNewQueueFragment();

    }

    public void setNewQueueFragment(){

        requestHelper.executeGet("queue", new String[]{"match"}, new String[]{matchId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                DrawSectorFragment DSFragment = null;
                try {
                    DSFragment = DrawSectorFragment.newInstance(json.toString(),
                            (new JSONObject(b.getString("info"))).getString("matchName"), (new JSONObject(b.getString("info"))).getString("matchId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_fragment, DSFragment);
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
    public void update(TeamsQueue team, String input) {
        progressBar.show();
        RequestHelper requestHelper = new RequestHelper(this);
        JSONObject object = new JSONObject();
        try {
            object.put("teamId", team.getTeamId());
            object.put("teamName", team.getTeamName());
            object.put("sector", Integer.parseInt(input));
            object.put("queue", team.getQueue());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        requestHelper.executePost("queue", new String[]{"match"}, new String[]{matchId},
                object.toString(), new RequestListener() {
                    @Override
                    public void onComplete(JSONObject json) {
                        try {
                            if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")){
                                setNewQueueFragment();
                            } else {
                                DialogBuilder.createDefaultDialog(SectorActivity.this, getLayoutInflater(), json.getString("error"), null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(int responseCode) {
                        DialogBuilder.createDefaultDialog(SectorActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
                    }
                });

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