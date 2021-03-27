package com.linaverde.fishingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.services.MapHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class MapFragment extends Fragment {

    private static final String JSON_MAP = "json";

    private JSONObject jsonObject;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(String json) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(JSON_MAP, json);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                jsonObject = new JSONObject(getArguments().getString(JSON_MAP));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    GridView gridView;
    LinearLayout llTableNames;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        gridView = view.findViewById(R.id.grid_map);
        llTableNames = view.findViewById(R.id.ll_table_name);
        listView = view.findViewById(R.id.lv_distance);
        MapHelper mapHelper = new MapHelper(getContext(), getLayoutInflater(),gridView, llTableNames, listView, jsonObject);

        return view;
    }
}