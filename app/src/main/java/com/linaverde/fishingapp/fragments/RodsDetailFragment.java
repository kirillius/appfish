package com.linaverde.fishingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.services.RodsSettingsListAdapter;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class RodsDetailFragment extends Fragment {

    private static final String JSON = "json";
    private static final String TYPE = "spod";

    private JSONObject settings;
    private String rodType;
    private String rodId;

    public RodsDetailFragment() {
        // Required empty public constructor
    }

    public static RodsDetailFragment newInstance(String json, String type) {
        RodsDetailFragment fragment = new RodsDetailFragment();
        Bundle args = new Bundle();
        args.putString(JSON, json);
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                settings = new JSONObject(getArguments().getString(JSON));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            rodType = getArguments().getString(TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rods_detail, container, false);

        UserInfo userInfo = new UserInfo(getContext());
        ((TextView)view.findViewById(R.id.tv_team_name)).setText(userInfo.getCaption());

        List<JSONObject> section1 = new ArrayList<>();
        List<JSONObject> section2 = new ArrayList<>();
        try {
            rodId = settings.getJSONArray("rods").getJSONObject(0).getString("rodId");
            JSONArray array = settings.getJSONArray("rods").getJSONObject(0).getJSONArray("settings");
            for (int i = 0; i < array.length(); i++){
                if (array.getJSONObject(i).getJSONObject("param").getInt("section") == 1){
                    section1.add(array.getJSONObject(i));
                } else {
                    section2.add(array.getJSONObject(i));
                }
            }

            RodsSettingsListAdapter adapter1, adapter2;
            adapter1 = new RodsSettingsListAdapter(getContext(), section1);
            adapter2 = new RodsSettingsListAdapter(getContext(), section2);

            ((ListView)view.findViewById(R.id.list_rods_settings_1)).setAdapter(adapter1);
            ((ListView)view.findViewById(R.id.list_rods_settings_2)).setAdapter(adapter2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}