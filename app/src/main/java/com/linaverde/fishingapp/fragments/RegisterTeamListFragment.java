package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.activities.AuthActivity;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TeamListClickListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.models.Team;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.StatisticAdapter;
import com.linaverde.fishingapp.services.TeamsAdapter;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterTeamListFragment extends Fragment {

    TeamListClickListener listener;

    private static final String ARG_PARAM = "param";
    private static final String TOURNAMENT_NAME = "name";
    private static final String MATCH_ID = "ID";

    private JSONObject mStartParam;
    private String tournamentName;
    private String matchId;

    public RegisterTeamListFragment() {
        // Required empty public constructor
    }

    public static RegisterTeamListFragment newInstance(String json, String tournamentName, String matchId) {
        RegisterTeamListFragment fragment = new RegisterTeamListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, json);
        args.putString(TOURNAMENT_NAME, tournamentName);
        args.putString(MATCH_ID, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                mStartParam = new JSONObject(getArguments().getString(ARG_PARAM));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tournamentName = getArguments().getString(TOURNAMENT_NAME);
            matchId = getArguments().getString(MATCH_ID);
        }
    }

    TeamsAdapter adapter;
    ListView teamsList;
    ContentLoadingProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_team_list, container, false);

        RelativeLayout buttonEndReg = view.findViewById(R.id.button_end_reg);
        TextView tvTournamentName = view.findViewById(R.id.tv_tournament_name);
        tvTournamentName.setText(tournamentName);

        teamsList = view.findViewById(R.id.lv_stats);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.hide();

        try {
            JSONArray arr = mStartParam.getJSONArray("teams");
            int len = arr.length();
            Team[] teams = new Team[len];
            for (int i = 0; i < len; i ++){
                teams[i] = new Team(arr.getJSONObject(i));

            }
            adapter = new TeamsAdapter(getContext(), teams);
            teamsList.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        teamsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onTeamClicked(adapter.getItem(position));
            }
        });

        UserInfo userInfo = new UserInfo(getContext());
        if (userInfo.getUserType() != 1){
            buttonEndReg.setVisibility(View.GONE);
        }

        buttonEndReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogBuilder.createTwoButtons(getContext(), getLayoutInflater(), getString(R.string.end_reg_match_question), new CompleteActionListener() {
                    @Override
                    public void onOk(String input) {
                        progressBar.show();
                        RequestHelper requestHelper = new RequestHelper(getContext());
                        requestHelper.executePost("matchcheckin", new String[]{"match"}, new String[]{matchId}, null, new RequestListener() {
                            @Override
                            public void onComplete(JSONObject json) {
                                progressBar.hide();
                                try {
                                    String error = json.getString("error");
                                    if (!error.equals("") && !error.equals("null")){
                                        DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.error) + error, null);
                                    } else {
                                        DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.end_reg_match), null);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(int responseCode) {
                                progressBar.hide();
                                DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.request_error), null);
                            }
                        });
                    }

                    @Override
                    public void onCancel() {

                    }
                });


            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TeamListClickListener) {
            //init the listener
            listener = (TeamListClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TeamListClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}