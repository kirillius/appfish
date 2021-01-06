package com.linaverde.fishingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.se.omapi.SEService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.services.UserInfo;

public class WeightingSelectedTeamFragment extends Fragment {


    private static final String SECTOR = "sector";


    // TODO: Rename and change types of parameters
    private int sector;

    public WeightingSelectedTeamFragment() {
        // Required empty public constructor
    }

    public static WeightingSelectedTeamFragment newInstance(int sector) {
        WeightingSelectedTeamFragment fragment = new WeightingSelectedTeamFragment();
        Bundle args = new Bundle();
        args.putInt(SECTOR, sector);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sector = getArguments().getInt(SECTOR);
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

        return view;
    }
}