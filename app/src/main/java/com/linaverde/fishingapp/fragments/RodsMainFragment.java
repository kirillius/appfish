package com.linaverde.fishingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.services.RodsMainSettingsGridAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RodsMainFragment extends Fragment {

    private static final String SPOD = "spod";
    private static final String JSON = "json";

    private boolean spod;
    private JSONObject settings;

    public RodsMainFragment() {
        // Required empty public constructor
    }

    public static RodsMainFragment newInstance(boolean spod, String json) {
        RodsMainFragment fragment = new RodsMainFragment();
        Bundle args = new Bundle();
        args.putBoolean(SPOD, spod);
        args.putString(JSON, json);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            spod = getArguments().getBoolean(SPOD);
            try {
                settings = new JSONObject(getArguments().getString(JSON));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rods_main, container, false);

        TextView tv = view.findViewById(R.id.tv_rods_type);

        if (spod){
            tv.setText(getString(R.string.settings_rods_spod));
        } else {
            tv.setText(getString(R.string.settings_rods_work));
        }

        RodsMainSettingsGridAdapter adapterRod1, adapterRod2, adapterRod3, adapterRod4;
        try {
            JSONArray rods = settings.getJSONArray("rods");
            adapterRod1 = new RodsMainSettingsGridAdapter(getContext(), getLayoutInflater(), rods.getJSONObject(0).getJSONArray("settings"));
            ((GridView)view.findViewById(R.id.rod1_main_settings_grid)).setAdapter(adapterRod1);
            adapterRod2 = new RodsMainSettingsGridAdapter(getContext(), getLayoutInflater(), rods.getJSONObject(1).getJSONArray("settings"));
            ((GridView)view.findViewById(R.id.rod2_main_settings_grid)).setAdapter(adapterRod1);
            adapterRod3 = new RodsMainSettingsGridAdapter(getContext(), getLayoutInflater(), rods.getJSONObject(2).getJSONArray("settings"));
            ((GridView)view.findViewById(R.id.rod3_main_settings_grid)).setAdapter(adapterRod1);
            adapterRod4 = new RodsMainSettingsGridAdapter(getContext(), getLayoutInflater(), rods.getJSONObject(3).getJSONArray("settings"));
            ((GridView)view.findViewById(R.id.rod4_main_settings_grid)).setAdapter(adapterRod1);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return view;
    }
}