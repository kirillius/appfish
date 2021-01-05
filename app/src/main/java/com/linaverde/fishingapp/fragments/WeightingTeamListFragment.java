package com.linaverde.fishingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.TeamsQueue;
import com.linaverde.fishingapp.services.QueueAdapter;
import com.linaverde.fishingapp.services.WeightingTeamListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeightingTeamListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeightingTeamListFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private static final String TOURNAMENT_NAME = "name";
    private static final String MATCH = "match";

    private JSONObject mStartParam;
    private String tournamentName;
    private String matchId;

    public WeightingTeamListFragment() {
        // Required empty public constructor
    }

    public static WeightingTeamListFragment newInstance(String json, String tournamentName, String matchId) {
        WeightingTeamListFragment fragment = new WeightingTeamListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, json);
        args.putString(TOURNAMENT_NAME, tournamentName);
        args.putString(MATCH, matchId);
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
            matchId = getArguments().getString(MATCH);
        }
    }

    WeightingTeamListAdapter adapter;
    ListView teamsList;
    RelativeLayout endWeighting;
    TeamsQueue[] teams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weighting_team_list, container, false);
        TextView tvTournamentName = view.findViewById(R.id.tv_tournament_name);
        tvTournamentName.setText(tournamentName);

        teamsList = view.findViewById(R.id.lv_stats);

        teamsList = view.findViewById(R.id.lv_sectors);
        endWeighting = view.findViewById(R.id.button_end_draw);

        try {
            JSONArray arr = mStartParam.getJSONArray("teams");
            int len = arr.length();
            teams = new TeamsQueue[len];
            for (int i = 0; i < len; i++) {
                teams[i] = new TeamsQueue(arr.getJSONObject(i));

            }

            adapter = new WeightingTeamListAdapter(getContext(), teams);
            teamsList.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }
}