package com.linaverde.fishingapp.fragments;

import android.content.Context;
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
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TeamListClickListener;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class TournamentFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private static final String LINKS = "links";

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
    UserInfo userInfo;
    ImageView net1, net2, net3, net4;
    ImageView checkInStatus, queueStatus, sectorStatus;

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


        rlRegisterTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterTeamActivity.class);
                Bundle args = new Bundle();
                args.putBoolean("showButtons", true);
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

        rlStatistics = view.findViewById(R.id.button_statistics);
        rlStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StatisticActivity.class);
                Bundle args = new Bundle();
                try {
                    args.putString("matchId", mStartParam.getString("matchId"));
                    args.putString("teamId", "");
                    args.putString("matchName", mStartParam.getString("matchName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        checkInStatus = view.findViewById(R.id.register_complete_icon);
        queueStatus = view.findViewById(R.id.queue_complete_icon);
        sectorStatus = view.findViewById(R.id.sector_complete_icon);

        if (userInfo.getCheckInStatus())
            checkInStatus.setImageDrawable(getContext().getDrawable(R.drawable.team_check));
        if (userInfo.getQueueStatus())
            queueStatus.setImageDrawable(getContext().getDrawable(R.drawable.team_check));
        if (userInfo.getSectorStatus())
            sectorStatus.setImageDrawable(getContext().getDrawable(R.drawable.team_check));

        setLinks(view);

        return view;
    }

    private void setLinks(View view){
        net1 = view.findViewById(R.id.play);
        net2 = view.findViewById(R.id.network);
        net3 = view.findViewById(R.id.instagram);
        net4 = view.findViewById(R.id.facebook);

        RequestHelper requestHelper = new RequestHelper(getContext());
        requestHelper.getLinks(new RequestListener() {
            @Override
            public void onComplete(JSONObject links) {
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
            }

            @Override
            public void onError(int responseCode) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        UserInfo userInfo = new UserInfo(getContext());
        if (userInfo.getCheckInStatus()) {
            checkInStatus.setImageDrawable(getContext().getDrawable(R.drawable.team_check));
        } else {
            checkInStatus.setImageDrawable(getContext().getDrawable(R.drawable.team_unckeck));
        }
        if (userInfo.getQueueStatus()) {
            queueStatus.setImageDrawable(getContext().getDrawable(R.drawable.team_check));
        } else {
            queueStatus.setImageDrawable(getContext().getDrawable(R.drawable.team_unckeck));
        }
        if (userInfo.getSectorStatus()) {
            sectorStatus.setImageDrawable(getContext().getDrawable(R.drawable.team_check));
        } else {
            sectorStatus.setImageDrawable(getContext().getDrawable(R.drawable.team_unckeck));
        }
    }

}