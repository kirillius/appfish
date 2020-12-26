package com.linaverde.fishingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.fragments.LogoTopMenuFragment;
import com.linaverde.fishingapp.fragments.RegisterOneTeamFragment;
import com.linaverde.fishingapp.fragments.RegisterTeamListFragment;
import com.linaverde.fishingapp.fragments.StatisticsFragment;
import com.linaverde.fishingapp.fragments.TopMenuFragment;
import com.linaverde.fishingapp.fragments.TournamentFragment;
import com.linaverde.fishingapp.interfaces.DocumentClickListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TeamListClickListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.models.Team;
import com.linaverde.fishingapp.services.RequestHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterTeamActivity extends AppCompatActivity implements TopMenuEventListener, TeamListClickListener, DocumentClickListener {

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    FragmentContainerView bottomFragmentContainer;
    ContentLoadingProgressBar progressBar;

    RequestHelper requestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_team);

        Bundle b = getIntent().getExtras();

        progressBar = findViewById(R.id.progress_bar);
        progressBar.show();

        bottomFragmentContainer = findViewById(R.id.bottom_fragment);

        TopMenuFragment menuFragment = TopMenuFragment.newInstance(null);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.top_menu_fragment, menuFragment);
        fragmentTransaction.commit();

        String matchId = "";
        try {
            matchId = (new JSONObject(b.getString("info"))).getString("matchId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestHelper = new RequestHelper(this);
        requestHelper.executeGet("teams", new String[]{"match"}, new String[]{matchId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                RegisterTeamListFragment RTFragment = null;
                try {
                    RTFragment = RegisterTeamListFragment.newInstance(json.toString(),
                            (new JSONObject(b.getString("info"))).getString("matchName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.content_fragment, RTFragment);
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
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
            bottomFragmentContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTeamClicked(Team selectedTeam) {

        RegisterOneTeamFragment ROTFragment = RegisterOneTeamFragment.newInstance(selectedTeam.getCaptainName(), selectedTeam.getCaptainId(), selectedTeam.getCaptainPhoto(),
                selectedTeam.getAssistantName(), selectedTeam.getAssistantId(), selectedTeam.getAssistantPhoto(),
                selectedTeam.getCaptainDocuments().toString(), selectedTeam.getAssistantDocuments().toString());
        LogoTopMenuFragment LTMFragment = LogoTopMenuFragment.newInstance(selectedTeam.getLogo());
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.content_fragment, ROTFragment);
        fragmentTransaction.addToBackStack("RegisterTeamFragment");
        fragmentTransaction.replace(R.id.top_menu_fragment, LTMFragment);
        fragmentTransaction.addToBackStack("LogoFragment");
        fragmentTransaction.commit();
        bottomFragmentContainer.setVisibility(View.GONE);

    }

    @Override
    public void onDocumentClicked(String userId, int doc) {
        progressBar.show();
        requestHelper.getDocument(userId, doc, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                Intent intent = new Intent(RegisterTeamActivity.this, DocumentActivity.class);
                Bundle args = new Bundle();
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        args.putString("image", json.getString("doc"));
                        intent.putExtras(args);
                        progressBar.hide();
                        startActivity(intent);
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