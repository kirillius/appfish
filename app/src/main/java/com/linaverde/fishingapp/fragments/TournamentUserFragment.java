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
import com.linaverde.fishingapp.activities.StatisticActivity;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TournamentUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TournamentUserFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private JSONObject mStartParam;

    public TournamentUserFragment() {
        // Required empty public constructor
    }


    public static TournamentUserFragment newInstance(String json) {
        TournamentUserFragment fragment = new TournamentUserFragment();
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
    UserInfo userInfo;
    RelativeLayout rlOnlineProtocol, rlMap, rlRodsWork, rlRodsSpod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournament_user, container, false);

        userInfo = new UserInfo(getContext());

        tvTournamentName = view.findViewById(R.id.tv_tournament_name);
        try {
            tvTournamentName.setText(mStartParam.getString("matchName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rlOnlineProtocol = view.findViewById(R.id.list_online_protocol);
        rlMap = view.findViewById(R.id.list_map_sector);
        rlRodsWork = view.findViewById(R.id.list_work_rods);
        rlRodsSpod = view.findViewById(R.id.list_spod_rods);

        rlOnlineProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StatisticActivity.class);
                Bundle args = new Bundle();
                try {
                    args.putString("matchId", mStartParam.getString("matchId"));
                    args.putString("teamId", "");
                    args.putString("matchName",mStartParam.getString("matchName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        return view;
    }
}