package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;

public class TopMenuFragment extends Fragment {

    private TopMenuEventListener listener;

    public TopMenuFragment() {
        // Required empty public constructor
    }

    ImageButton menu, settings, gps, chat, message, sync;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_menu, container, false);
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