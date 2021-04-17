package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.DetailedStatisticDayClicked;
import com.linaverde.fishingapp.interfaces.StatisticDayClicked;
import com.linaverde.fishingapp.services.DetailedStatisticAdapter;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DetailedDayStatsFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private static final String TEAM = "team";

    private JSONObject mStartParam;
    private String teamId;
    DetailedStatisticDayClicked dayListener;

    public DetailedDayStatsFragment() {
        // Required empty public constructor
    }


    public static DetailedDayStatsFragment newInstance(String json, String teamId) {
        DetailedDayStatsFragment fragment = new DetailedDayStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, json);
        args.putString(TEAM, teamId);
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
            teamId = getArguments().getString(TEAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_day_stats, container, false);
        //Log.d("Fragment", "DETAILED DAY STATS FRAGMENT");
        TextView tvTournamentName = view.findViewById(R.id.tv_team_name);
        UserInfo userInfo = new UserInfo(getContext());
        tvTournamentName.setText(userInfo.getMatchName());

        ListView statList = view.findViewById(R.id.lv_stats);
        try {
            JSONArray arr = mStartParam.getJSONArray("stats");
            int len = arr.length();
            JSONObject[] stats = new JSONObject[len];
            for (int i = 0; i < len; i++) {
                stats[i] = arr.getJSONObject(i);
            }
            DetailedStatisticAdapter adapter = new DetailedStatisticAdapter(getContext(), stats);
            statList.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            JSONObject total = mStartParam.getJSONObject("total");
            ((TextView) view.findViewById(R.id.tv_result_weight)).setText(Integer.toString(total.getInt("weight")));
            ((TextView) view.findViewById(R.id.tv_result_avr_weight)).setText(Integer.toString(total.getInt("avgWeight")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dayButtons(view);

        return view;
    }

    private void dayButtons(View view) {
        ImageView day1, day2, day3, day4;
        day1 = view.findViewById(R.id.iv_day_1);
        day2 = view.findViewById(R.id.iv_day_2);
        day3 = view.findViewById(R.id.iv_day_3);
        day4 = view.findViewById(R.id.iv_day_4);
        try {
            JSONObject days = mStartParam.getJSONObject("days");
            if (days.getBoolean("d1")){
                day1.setImageDrawable(getContext().getDrawable(R.drawable.day_green));
            }
            if (days.getBoolean("d2")){
                day2.setImageDrawable(getContext().getDrawable(R.drawable.day_green));
            }
            if (days.getBoolean("d3")){
                day3.setImageDrawable(getContext().getDrawable(R.drawable.day_green));
            }
            if (days.getBoolean("d4")){
                day4.setImageDrawable(getContext().getDrawable(R.drawable.day_green));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayListener.dayOneClicked(teamId);
            }
        });

        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayListener.dayTwoClicked(teamId);
            }
        });

        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayListener.dayThreeClicked(teamId);
            }
        });

        day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayListener.dayFourClicked(teamId);
            }
        });

        ((ImageView) view.findViewById(R.id.iv_online)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayListener.onlineClicked(teamId);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DetailedStatisticDayClicked) {
            //init the listener
            dayListener = (DetailedStatisticDayClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DetailedStatisticDayClicked");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dayListener = null;
    }
}