package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.mask.PorterImageView;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.models.Team;
import com.linaverde.fishingapp.services.ImageHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class LogoTopMenuFragment extends Fragment {

    private TopMenuEventListener listener;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LOGO = "logo";
    private static final String TEAM = "team";

    private String logo = null;
    private String team = null;

    public LogoTopMenuFragment() {
        // Required empty public constructor
    }

    public static LogoTopMenuFragment newInstance(String logo, String team) {
        LogoTopMenuFragment fragment = new LogoTopMenuFragment();
        Bundle args = new Bundle();
        if (logo != null)
            args.putString(LOGO, logo);
        args.putString(TEAM, team);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.d("teamname", getArguments().getString(TEAM));
            team = getArguments().getString(TEAM);
            logo = getArguments().getString(LOGO);
        }
    }

    ImageButton menu, settings, gps, chat, message, sync;
    PorterShapeImageView ivLogo;
    TextView teamName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logo_top_menu, container, false);

        ivLogo = view.findViewById(R.id.iv_team_logo);
        if (logo != null && !logo.equals("null") && !logo.equals("")) {
            ivLogo.setImageBitmap(ImageHelper.decodeToImage(logo));
        }
        teamName = view.findViewById(R.id.tv_team_name);
        teamName.setText(team);


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