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
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.TeamListener;
import com.linaverde.fishingapp.models.TeamsQueue;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.SectorAdapter;
import com.linaverde.fishingapp.models.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TeamListFragment extends Fragment {

    private static final String ARG_PARAM = "json";
    private static final String MATCH = "match";


    private String tournamentName;
    private String matchId;
    ContentLoadingProgressBar progressBar;
    TeamListener listener;
    UserInfo userInfo;
    JSONObject mStartParam;

    public TeamListFragment() {
        // Required empty public constructor
    }

    public static TeamListFragment newInstance(String json) {
        TeamListFragment fragment = new TeamListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, json);
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
            userInfo = new UserInfo(getContext());
            tournamentName = userInfo.getMatchName();
            matchId = userInfo.getMatchId();

        }
    }

    SectorAdapter adapter;
    ListView teamsList;
    TeamsQueue[] teams;
    RequestHelper requestHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_list, container, false);
        TextView tvTournamentName = view.findViewById(R.id.tv_tournament_name);
        tvTournamentName.setText(tournamentName);
        teamsList = view.findViewById(R.id.lv_stats);
        progressBar = view.findViewById(R.id.progress_bar);
        requestHelper = new RequestHelper(getContext());
        progressBar.show();

        try {
            JSONArray arr = mStartParam.getJSONArray("teams");
            int len = arr.length();
            teams = new TeamsQueue[len];
            for (int i = 0; i < len; i++) {
                teams[i] = new TeamsQueue(arr.getJSONObject(i));

            }
            adapter = new SectorAdapter(getContext(), teams);
            teamsList.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.hide();

        teamsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.teamClicked(adapter.getItem(position).getTeamId());
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TeamListener) {
            //init the listener
            listener = (TeamListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TeamListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}