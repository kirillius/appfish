package com.linaverde.fishingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.Team;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterOneTeamFragment extends Fragment {

    private static final String CAPTAIN = "captain";
    private static final String ASSISTANT = "assistant";

    private String captainName;
    private String assistantName;


    public static RegisterOneTeamFragment newInstance(String captain, String assistantName) {

        RegisterOneTeamFragment fragment = new RegisterOneTeamFragment();
        Bundle args = new Bundle();
        args.putString(CAPTAIN, captain);
        args.putString(ASSISTANT, assistantName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            captainName = getArguments().getString(CAPTAIN);
            assistantName = getArguments().getString(ASSISTANT);
        }
    }

    TextView tvCaptainName;
    TextView tvAssistantName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_one_team, container, false);
        tvCaptainName = view.findViewById(R.id.tv_captain_name);
        tvCaptainName.setText(captainName);
        tvAssistantName = view.findViewById(R.id.tv_assistant_name);
        tvAssistantName.setText(assistantName);
        return view;
    }

}