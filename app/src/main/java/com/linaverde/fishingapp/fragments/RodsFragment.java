package com.linaverde.fishingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.Rod;
import com.linaverde.fishingapp.services.RodsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RodsFragment extends Fragment {

    private static final String RODS_ISON = "json";

    private JSONArray rodsJson;

    public RodsFragment() {
        // Required empty public constructor
    }

    public static RodsFragment newInstance(String json) {
        RodsFragment fragment = new RodsFragment();
        Bundle args = new Bundle();
        args.putString(RODS_ISON, json);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                rodsJson = (new JSONObject(getArguments().getString(RODS_ISON))).getJSONArray("rods");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rods, container, false);

        List <Rod> rods = new ArrayList<>();
        for (int i = 0; i < rodsJson.length(); i++){
            try {
                rods.add(new Rod(rodsJson.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        gridView = view.findViewById(R.id.grid_rods);
        String[] names = getResources().getStringArray(R.array.rods_params);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++){
            data.add(names[i]);
            for (int j = 0; j < rods.size(); j++){
                data.add(rods.get(j).getRodsAsStrings()[i]);
            }
        }
        RodsAdapter adapter = new RodsAdapter(getContext(), getLayoutInflater(), rods, data);
        gridView.setAdapter(adapter);

        return view;
    }
}