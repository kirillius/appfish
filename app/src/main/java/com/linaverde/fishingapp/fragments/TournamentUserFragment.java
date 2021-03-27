package com.linaverde.fishingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.activities.MapActivity;
import com.linaverde.fishingapp.activities.RodsSettingsActivity;
import com.linaverde.fishingapp.activities.StatisticActivity;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class TournamentUserFragment extends Fragment {

    private static final String ARG_PARAM = "param";


    public TournamentUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TextView tvTournamentName;
    UserInfo userInfo;
    RelativeLayout rlOnlineProtocol, rlMap, rlRodsWork, rlRodsSpod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournament_user, container, false);

        userInfo = new UserInfo(getContext());

        tvTournamentName = view.findViewById(R.id.tv_tournament_name);
        tvTournamentName.setText(userInfo.getMatchName());

        rlOnlineProtocol = view.findViewById(R.id.list_online_protocol);
        rlMap = view.findViewById(R.id.list_map_sector);
        rlRodsWork = view.findViewById(R.id.list_work_rods);
        rlRodsSpod = view.findViewById(R.id.list_spod_rods);

        rlOnlineProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StatisticActivity.class);
                Bundle args = new Bundle();
                args.putString("matchId", userInfo.getMatchId());
                args.putString("teamId", "");
                args.putString("matchName", userInfo.getMatchName());
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        rlRodsWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RodsSettingsActivity.class);
                Bundle args = new Bundle();
                args.putBoolean("spod", false);
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        rlRodsSpod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RodsSettingsActivity.class);
                Bundle args = new Bundle();
                args.putBoolean("spod", true);
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        rlMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                Bundle args = new Bundle();
                args.putString("matchId", userInfo.getMatchId());
                args.putString("teamId", userInfo.getTeamId());
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        return view;
    }
}