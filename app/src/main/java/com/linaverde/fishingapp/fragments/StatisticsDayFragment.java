package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.StatisticTeamNameClicked;
import com.linaverde.fishingapp.interfaces.StatisticDayClicked;
import com.linaverde.fishingapp.services.StatisticAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StatisticsDayFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private static final String TOURNAMENT_NAME = "name";
    private static final String RESULT = "result";

    private JSONObject mStartParam;
    private String tournamentName;
    private boolean result;

    StatisticTeamNameClicked teamListener;
    StatisticDayClicked dayListener;

    public StatisticsDayFragment() {
        // Required empty public constructor
    }

    public static StatisticsDayFragment newInstance(String json, String tournamentName, boolean result) {
        StatisticsDayFragment fragment = new StatisticsDayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, json);
        args.putString(TOURNAMENT_NAME, tournamentName);
        args.putBoolean(RESULT, result);
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
            result = getArguments().getBoolean(RESULT);
        }
    }

    StatisticAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics_day, container, false);

        TextView tvTournamentName = view.findViewById(R.id.tv_tournament_name);
        tvTournamentName.setText(tournamentName);

//        TextView tvDate = view.findViewById(R.id.tv_date);
//        try {
//            String [] sdate = mStartParam.getString("date").substring(0, mStartParam.getString("date").indexOf("T")).split("-");
//            tvDate.setText(sdate[2]+"-"+sdate[1]+"-"+sdate[0]);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        ListView statList = view.findViewById(R.id.lv_stats);
        try {
            JSONArray arr = mStartParam.getJSONArray("stats");
            int len = arr.length();
            JSONObject[] stats = new JSONObject[len];
            for (int i = 0; i < len; i++) {
                stats[i] = arr.getJSONObject(i);
            }
            adapter = new StatisticAdapter(getContext(), stats);
            statList.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LinearLayout llRes = view.findViewById(R.id.ll_result);
        if (!result) {
            llRes.setVisibility(View.GONE);
        } else {

            try {
                JSONObject total = mStartParam.getJSONObject("total");
                ((TextView) view.findViewById(R.id.tv_result_stat_count)).setText(Integer.toString(total.getInt("quantity")));
                ((TextView) view.findViewById(R.id.tv_result_stat_avr)).setText(Integer.toString(total.getInt("avgWeight")));
                ((TextView) view.findViewById(R.id.tv_result_stat_sum)).setText(Integer.toString(total.getInt("weight")));

                ((TextView) view.findViewById(R.id.tv_leaderWeight)).setText(total.getString("leaderWeight"));
                ((TextView) view.findViewById(R.id.tv_bigFishWeight)).setText(total.getString("bigFishWeight"));
                ((TextView) view.findViewById(R.id.tv_koiWeight)).setText(total.getString("koiWeight"));

                ((TextView) view.findViewById(R.id.tv_leaderTeam)).setText(total.getString("leaderTeam"));
                ((TextView) view.findViewById(R.id.tv_bigFishTeam)).setText(total.getString("bigFishTeam"));
                ((TextView) view.findViewById(R.id.tv_koiTeam)).setText(total.getString("koiTeam"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        if (result) {
            statList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    teamListener.teamClicked(adapter.getTeamId(position), adapter.getTeamName(position));
                }
            });
        } else {
            statList.setEnabled(false);
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
                dayListener.dayOneClicked();
            }
        });

        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayListener.dayTwoClicked();
            }
        });

        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayListener.dayThreeClicked();
            }
        });

        day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayListener.dayFourClicked();
            }
        });

        ((ImageView) view.findViewById(R.id.iv_online)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayListener.onlineClicked();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StatisticTeamNameClicked) {
            //init the listener
            teamListener = (StatisticTeamNameClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StatisticTeamNameClicked");
        }
        if (context instanceof StatisticDayClicked) {
            //init the listener
            dayListener = (StatisticDayClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StatisticDayClicked");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        teamListener = null;
    }
}