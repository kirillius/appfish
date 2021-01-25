package com.linaverde.fishingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.services.DetailedStatisticAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DetailedStatsFragment extends Fragment {


    private static final String ARG_PARAM = "param";
    private static final String TEAM_NAME = "name";

    private JSONObject mStartParam;
    private String teamName;

    public DetailedStatsFragment() {
        // Required empty public constructor
    }


    public static DetailedStatsFragment newInstance(String json, String teamName) {
        DetailedStatsFragment fragment = new DetailedStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, json);
        args.putString(TEAM_NAME, teamName);
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
            teamName = getArguments().getString(TEAM_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_stats, container, false);

        TextView tvTournamentName = view.findViewById(R.id.tv_team_name);
        tvTournamentName.setText(teamName);

        TextView tvDate = view.findViewById(R.id.tv_date);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        tvDate.setText(df.format(c));

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
        return view;
    }
}