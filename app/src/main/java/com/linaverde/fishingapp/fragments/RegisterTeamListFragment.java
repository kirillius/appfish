package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.TeamListClickListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.models.Team;
import com.linaverde.fishingapp.services.StatisticAdapter;
import com.linaverde.fishingapp.services.TeamsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterTeamListFragment extends Fragment {

    TeamListClickListener listener;

    private static final String ARG_PARAM = "param";
    private static final String TOURNAMENT_NAME = "name";

    private JSONObject mStartParam;
    private String tournamentName;

    public RegisterTeamListFragment() {
        // Required empty public constructor
    }

    public static RegisterTeamListFragment newInstance(String json, String tournamentName) {
        RegisterTeamListFragment fragment = new RegisterTeamListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, json);
        args.putString(TOURNAMENT_NAME, tournamentName);
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
        }
    }

    TeamsAdapter adapter;
    ListView teamsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_team_list, container, false);

        TextView tvTournamentName = view.findViewById(R.id.tv_tournament_name);
        tvTournamentName.setText(tournamentName);

        teamsList = view.findViewById(R.id.lv_stats);

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