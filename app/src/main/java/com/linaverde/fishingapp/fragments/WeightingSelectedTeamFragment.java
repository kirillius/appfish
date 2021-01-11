package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.se.omapi.SEService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.WeightFishesClickListener;
import com.linaverde.fishingapp.services.UserInfo;

public class WeightingSelectedTeamFragment extends Fragment {


    private static final String SECTOR = "sector";
    private static final String TEAM = "team";
    private static final String STAGE = "stage";
    private static final String PIN = "pin";

    private int sector;
    private String teamId;
    private String stageId;
    private String pin;

    WeightFishesClickListener listener;

    public WeightingSelectedTeamFragment() {
        // Required empty public constructor
    }

    public static WeightingSelectedTeamFragment newInstance(int sector, String teamId, String stageId, String pin) {
        WeightingSelectedTeamFragment fragment = new WeightingSelectedTeamFragment();
        Bundle args = new Bundle();
        args.putInt(SECTOR, sector);
        args.putString(TEAM, teamId);
        args.putString(STAGE, stageId);
        args.putString(PIN, pin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sector = getArguments().getInt(SECTOR);
            teamId = getArguments().getString(TEAM);
            stageId = getArguments().getString(STAGE);
            pin = getArguments().getString(PIN);
        }

    }

    TextView tvPond, tvSector, tvMatch;
    UserInfo userInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weighting_selected_team, container, false);

        userInfo = new UserInfo(getContext());

        tvSector = view.findViewById(R.id.tv_sector);
        tvPond = view.findViewById(R.id.tv_pond_name);
        tvMatch = view.findViewById(R.id.tv_tournament_name);

        tvPond.setText(userInfo.getPond());
        tvSector.setText("Сектор " + Integer.toString(sector));
        tvMatch.setText(userInfo.getMatchName());

        RelativeLayout fish, violation;

        fish = view.findViewById(R.id.rl_weighting_fish);

        fish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.fishClicked(teamId, stageId, pin, sector);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WeightFishesClickListener) {
            //init the listener
            listener = (WeightFishesClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement WeightFishesClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}