package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.StatisticTeamNameClicked;
import com.linaverde.fishingapp.services.StatisticAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StatisticsFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private static final String TOURNAMENT_NAME = "name";
    private static final String RESULT = "result";

    private JSONObject mStartParam;
    private String tournamentName;
    private boolean result;

    StatisticTeamNameClicked listener;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    public static StatisticsFragment newInstance(String json, String tournamentName, boolean result) {
        StatisticsFragment fragment = new StatisticsFragment();
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
        View view = inflater.inflate(R.layout.fragment_statistics_comand, container, false);

        TextView tvTournamentName = view.findViewById(R.id.tv_tournament_name);
        tvTournamentName.setText(tournamentName);

        TextView tvDate = view.findViewById(R.id.tv_date);
        try {
            String [] sdate = mStartParam.getString("date").substring(0, mStartParam.getString("date").indexOf("T")).split("-");
            tvDate.setText(sdate[2]+"-"+sdate[1]+"-"+sdate[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                TextView resultCoun = view.findViewById(R.id.tv_result_stat_count);
                resultCoun.setText(Integer.toString(total.getInt("quantity")));
                TextView resultAvr = view.findViewById(R.id.tv_result_stat_avr);
                resultAvr.setText(Integer.toString(total.getInt("avgWeight")));
                TextView resultSum = view.findViewById(R.id.tv_result_stat_sum);
                resultSum.setText(Integer.toString(total.getInt("weight")));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        if (result) {
            statList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.teamClicked(adapter.getTeamId(position), adapter.getTeamName(position));
                }
            });
        } else {
            statList.setEnabled(false);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StatisticTeamNameClicked) {
            //init the listener
            listener = (StatisticTeamNameClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StatisticTeamNameClicked");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}