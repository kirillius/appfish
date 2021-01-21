package com.linaverde.fishingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.services.StatisticAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StatisticsFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private static final String TOURNAMENT_NAME = "name";
    private static final String RESULT = "result";

    private JSONObject mStartParam;
    private String tournamentName;
    private boolean result;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        TextView tvTournamentName = view.findViewById(R.id.tv_tournament_name);
        tvTournamentName.setText(tournamentName);

        TextView tvDate = view.findViewById(R.id.tv_date);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        tvDate.setText(df.format(c));

        ListView statList = view.findViewById(R.id.lv_stats);
        int count = 0, avr = 0, sum = 0;
        try {
            JSONArray arr = mStartParam.getJSONArray("stats");
            int len = arr.length();
            JSONObject [] stats = new JSONObject[len];
            for (int i = 0; i < len; i ++){
                stats[i] = arr.getJSONObject(i);
                count += stats[i].getInt("quantity");
                avr += stats[i].getInt("avgWeight");
                sum += stats[i].getInt("maxWeight");
            }
            if (count != 0) {
                avr = avr / count;
            } else {
                avr = 0;
            }
            StatisticAdapter adapter = new StatisticAdapter(getContext(), stats);
            statList.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LinearLayout llRes = view.findViewById(R.id.ll_result);
        if (!result)
            llRes.setVisibility(View.GONE);

        TextView resultCoun = view.findViewById(R.id.tv_result_stat_count);
        resultCoun.setText(Integer.toString(count));
        TextView resultAvr = view.findViewById(R.id.tv_result_stat_avr);
        resultAvr.setText(Integer.toString(avr));
        TextView resultSum = view.findViewById(R.id.tv_result_stat_sum);
        resultSum.setText(Integer.toString(sum));



        return view;
    }
}