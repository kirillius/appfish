package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.WeightStageClickedListener;
import com.linaverde.fishingapp.models.Stage;
import com.linaverde.fishingapp.services.StageAdapter;

import org.json.JSONArray;
import org.json.JSONException;

public class WeightingStagesFragment extends Fragment {

    private static final String STAGES = "stages";
    private static final String MATCH_NAME = "matchName";

    private String stagesJson;
    private String matchName;

    WeightStageClickedListener listener;

    public WeightingStagesFragment() {
        // Required empty public constructor
    }

    public static WeightingStagesFragment newInstance(String stages, String matchName) {
        WeightingStagesFragment fragment = new WeightingStagesFragment();
        Bundle args = new Bundle();
        args.putString(STAGES, stages);
        args.putString(MATCH_NAME, matchName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stagesJson = getArguments().getString(STAGES);
            matchName = getArguments().getString(MATCH_NAME);
        }
    }

    ListView lvStages;
    TextView tvMatchName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weighting_stages, container, false);
        lvStages = view.findViewById(R.id.lv_stages);
        tvMatchName = view.findViewById(R.id.tv_tournament_name);
        tvMatchName.setText(matchName);
        try {
            JSONArray stagesArray = new JSONArray(stagesJson);
            Stage[] stages = new Stage[stagesArray.length()];
            for (int i = 0; i < stagesArray.length(); i++){
                stages[i] = new Stage(stagesArray.getJSONObject(i));
            }
            StageAdapter adapter = new StageAdapter(getContext(), stages, listener);
            lvStages.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WeightStageClickedListener) {
            //init the listener
            listener = (WeightStageClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement WeightStageClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}