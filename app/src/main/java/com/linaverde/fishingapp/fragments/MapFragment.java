package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.RodPositionChangedListener;
import com.linaverde.fishingapp.interfaces.RodsSettingsListener;
import com.linaverde.fishingapp.services.MapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapFragment extends Fragment {

    private static final String JSON_MAP = "json";
    private static final String RODS_ARRAY = "rods_array";
    private static final String ROD_ID = "rodId";

    private JSONObject jsonObject;
    private JSONArray rods;
    private int editableRod;

    RodPositionChangedListener listener = null;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(String json, String rods, int rodId) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(JSON_MAP, json);
        args.putInt(ROD_ID, rodId);
        args.putString(RODS_ARRAY, rods);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                jsonObject = new JSONObject(getArguments().getString(JSON_MAP));
                editableRod = getArguments().getInt(ROD_ID);
                rods = new JSONArray(getArguments().getString(RODS_ARRAY));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    GridView gridView;
    LinearLayout llTableNames;
    LinearLayout llButtons;
    RelativeLayout confirm, cancel;

    public void setRodsPositions(JSONArray rodsPositions) {
        rods = rodsPositions;
    }

    public JSONArray getRodsPositions(){
        return rods;
    }

    public void setListener(RodPositionChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        gridView = view.findViewById(R.id.grid_map);
        llTableNames = view.findViewById(R.id.ll_table_name);
        llButtons = view.findViewById(R.id.ll_buttons);

        if (editableRod > 0) {
            llButtons.setVisibility(View.VISIBLE);
        }

        MapHelper mapHelper = new MapHelper(getContext(), getLayoutInflater(), gridView, llTableNames, view.findViewById(R.id.ll_width), jsonObject, rods, editableRod);
        try {
            ((TextView) view.findViewById(R.id.ll_width_value)).setText(jsonObject.getString("width"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mapHelper.movePonton((ImageView) view.findViewById(R.id.iv_ponton));

        cancel = view.findViewById(R.id.rl_cancel);
        confirm = view.findViewById(R.id.rl_confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("editable rod", Integer.toString(editableRod));
                Log.d("new landmark", mapHelper.getLandmark(editableRod));
                Log.d("new distance", Double.toString(mapHelper.getDistance(editableRod)));
                if (listener != null) {
                    listener.rodPositionChanged(editableRod, mapHelper.getLandmark(editableRod), mapHelper.getDistance(editableRod));
                    getActivity().onBackPressed();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

}