package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.WeightFishesClickListener;
import com.linaverde.fishingapp.interfaces.WeightTeamClickListener;
import com.linaverde.fishingapp.models.Fish;
import com.linaverde.fishingapp.models.FishDictionaryItem;
import com.linaverde.fishingapp.services.FishAdapter;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WeightingFishFragment extends Fragment {

    private static final String FISHES = "fishes";
    private static final String DICTIONARY = "dict";
    private static final String STAGE = "stage";
    private static final String TEAM = "team";

    private JSONArray fishes;
    private JSONArray dict;
    private String stageId;
    private String teamId;

    public WeightingFishFragment() {
        // Required empty public constructor
    }

    public static WeightingFishFragment newInstance(String fishes, String dict, String stage, String team) {
        WeightingFishFragment fragment = new WeightingFishFragment();
        Bundle args = new Bundle();
        args.putString(FISHES, fishes);
        args.putString(DICTIONARY, dict);
        args.putString(STAGE, stage);
        args.putString(TEAM, team);
        fragment.setArguments(args);
        return fragment;
    }

    ListView lvFishes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            teamId = getArguments().getString(TEAM);
            stageId = getArguments().getString(STAGE);
            try {
                fishes = new JSONArray(getArguments().getString(FISHES));
                dict = new JSONArray(getArguments().getString(DICTIONARY));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weighting_fish, container, false);

        lvFishes = view.findViewById(R.id.lv_fishes);
        Fish[] fishesArr;
        fishesArr = new Fish[fishes.length()];
        FishDictionaryItem[] dictArr;
        dictArr = new FishDictionaryItem[dict.length()];
        try {
            for (int i = 0; i < fishes.length(); i++) {
                fishesArr[i] = new Fish(fishes.getJSONObject(i));
            }
            for (int i = 0; i < dict.length(); i++) {
                dictArr[i] = new FishDictionaryItem(dict.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FishAdapter adapter = new FishAdapter(getContext(), fishesArr, dictArr);
        lvFishes.setAdapter(adapter);
        return view;
    }


}