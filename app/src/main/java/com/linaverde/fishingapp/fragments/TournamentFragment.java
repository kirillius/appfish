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
import com.linaverde.fishingapp.activities.RegisterTeamActivity;
import com.linaverde.fishingapp.activities.StatisticActivity;
import com.linaverde.fishingapp.services.SmallCapsBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class TournamentFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";

    private JSONObject mStartParam;

    public TournamentFragment() {
        // Required empty public constructor
    }

    public static TournamentFragment newInstance(String json) {
        TournamentFragment fragment = new TournamentFragment();
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
        }
    }

    TextView tvTournamentName;
    RelativeLayout rlRegisterTeam, rlDrawQueue, rlDrawSector, rlWeighting;
    RelativeLayout rlExchange, rlStatistics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournament, container, false);

        tvTournamentName = view.findViewById(R.id.tv_tournament_name);
        try {
            tvTournamentName.setText(mStartParam.getString("matchName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rlRegisterTeam = view.findViewById(R.id.list_register_team);
        rlDrawQueue = view.findViewById(R.id.list_draw_queue);
        rlDrawSector = view.findViewById(R.id.list_draw_sector);
        rlWeighting = view.findViewById(R.id.list_weigh);

        try {
            if (mStartParam.getInt("userType") == 2) {
                rlDrawQueue.setVisibility(View.GONE);
                rlDrawSector.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rlRegisterTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterTeamActivity.class);
                Bundle args = new Bundle();
                args.putString("info", mStartParam.toString());
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        rlStatistics = view.findViewById(R.id.button_statistics);
        rlStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StatisticActivity.class);
                Bundle args = new Bundle();
                args.putString("info", mStartParam.toString());
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        return view;

    }
}