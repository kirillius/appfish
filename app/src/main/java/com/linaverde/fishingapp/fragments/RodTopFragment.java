package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RodTopFragment extends Fragment {

    private TopMenuEventListener listener;
    private static final String TYPE = "spod";


    public RodTopFragment() {
        // Required empty public constructor
    }

    public static RodTopFragment newInstance(String type) {
        RodTopFragment fragment = new RodTopFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    private String rodType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rodType = null;
        if (getArguments() != null) {
            rodType = getArguments().getString(TYPE);
        }

    }

    ImageButton menu, settings, gps, chat, message, sync;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rod_top, container, false);

        TextView tv = view.findViewById(R.id.tv_rod_settings);

        if (rodType.equals("spod")) {
            tv.setText(getString(R.string.settings_spod));
        } else if (rodType.equals("carp")) {
            tv.setText(getString(R.string.settings_work));
        }

        menu = view.findViewById(R.id.top_menu);
        settings = view.findViewById(R.id.settings);
        gps = view.findViewById(R.id.top_gps);
        chat = view.findViewById(R.id.top_chat);
        message = view.findViewById(R.id.top_message);
        sync = view.findViewById(R.id.top_sync);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMenuClick();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSettingsClick();
            }
        });

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGPSClick();
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChatClick();
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMessageClick();
            }
        });

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSyncClick();
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TopMenuEventListener) {
            //init the listener
            listener = (TopMenuEventListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TopMenuEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}