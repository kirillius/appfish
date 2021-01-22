package com.linaverde.fishingapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.activities.QueueActivity;
import com.linaverde.fishingapp.activities.RegisterTeamActivity;
import com.linaverde.fishingapp.activities.RodsActivity;
import com.linaverde.fishingapp.activities.SectorActivity;
import com.linaverde.fishingapp.activities.StatisticActivity;
import com.linaverde.fishingapp.activities.WeightingActivity;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class TournamentFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";
    private static final String LINKS = "links";

    private JSONObject mStartParam;
    private JSONObject links;

    public TournamentFragment() {
        // Required empty public constructor
    }

    public static TournamentFragment newInstance(String json, String links) {
        TournamentFragment fragment = new TournamentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, json);
        args.putString(LINKS, links);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                mStartParam = new JSONObject(getArguments().getString(ARG_PARAM));
                links = new JSONObject(getArguments().getString(LINKS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    TextView tvTournamentName;
    RelativeLayout rlRegisterTeam, rlDrawQueue, rlDrawSector, rlWeighting, rlRods;
    RelativeLayout rlExchange, rlStatistics;
    UserInfo userInfo;
    ImageView net1, net2, net3, net4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournament, container, false);

        userInfo = new UserInfo(getContext());

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
        rlRods = view.findViewById(R.id.list_rods);

        if (userInfo.getUserType() == 1) {
            rlRods.setVisibility(View.GONE);
        } else {
            rlRegisterTeam.setVisibility(View.GONE);
            rlDrawQueue.setVisibility(View.GONE);
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

        rlDrawQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QueueActivity.class);
                Bundle args = new Bundle();
                args.putString("info", mStartParam.toString());
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        rlWeighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeightingActivity.class);
                Bundle args = new Bundle();
                args.putString("info", mStartParam.toString());
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        rlDrawSector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SectorActivity.class);
                Bundle args = new Bundle();
                args.putString("info", mStartParam.toString());
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        rlRods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RodsActivity.class);
                startActivity(intent);
            }
        });

        rlStatistics = view.findViewById(R.id.button_statistics);
        rlStatistics.setOnClickListener(new View.OnClickListener() {
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

        net1 = view.findViewById(R.id.play);
        net2 = view.findViewById(R.id.network);
        net3 = view.findViewById(R.id.instagram);
        net4 = view.findViewById(R.id.facebook);

        net1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.getString("Link1")));
                    startActivity(browserIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        net2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.getString("Link2")));
                    startActivity(browserIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        net3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.getString("Link3")));
                    startActivity(browserIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        net4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.getString("Link4")));
                    startActivity(browserIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return view;

    }
}