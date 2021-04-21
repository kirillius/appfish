package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TopMenuEventListener;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.models.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;


public class TopMenuFragment extends Fragment {

    private TopMenuEventListener listener;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER = "param";
    private static final String SECTOR = "sector";

    private boolean user;
    private boolean sector;

    public TopMenuFragment() {
        // Required empty public constructor
    }

    public static TopMenuFragment newInstance(boolean user, boolean sector) {
        TopMenuFragment fragment = new TopMenuFragment();
        Bundle args = new Bundle();
        args.putBoolean(USER, user);
        args.putBoolean(SECTOR, sector);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sector = getArguments().getBoolean(SECTOR);
            user = getArguments().getBoolean(USER);
        }
    }

    ImageButton menu, settings, gps, chat, message, sync;
    TextView tvUsername, tvUserCaption;
    ImageView ivUserIcon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_menu, container, false);

        tvUsername = view.findViewById(R.id.tv_user_name);
        ivUserIcon = view.findViewById(R.id.iv_user_icon);
        tvUserCaption = view.findViewById(R.id.tv_user_caption);
        if (user) {
            UserInfo userInfo = new UserInfo(getContext());
            tvUsername.setText(userInfo.getUserName());
            int userType = userInfo.getUserType();
            switch (userType) {
                case 2:
                    ivUserIcon.setImageDrawable(getContext().getDrawable(R.drawable.fish_captain));
                    break;
                case 3:
                    ivUserIcon.setImageDrawable(getContext().getDrawable(R.drawable.fish_assistant));
                    break;
                default:
                    ivUserIcon.setImageDrawable(getContext().getDrawable(R.drawable.judge_icon));
            }
            tvUserCaption.setText(userInfo.getCaption());
        } else {
            tvUserCaption.setVisibility(View.GONE);
            view.findViewById(R.id.iv_user_icon).setVisibility(View.GONE);
            if (sector) {
                tvUsername.setText("Cектор" );
                UserInfo userInfo = new UserInfo(getContext());
                RequestHelper requestHelper = new RequestHelper(getContext());
                requestHelper.executeGet("map", new String[]{"match", "team"}, new String[]{userInfo.getMatchId(), userInfo.getTeamId()}, new RequestListener() {
                    @Override
                    public void onComplete(JSONObject json) {
                        try {
                            tvUsername.setText("Cектор " + json.getString("sector"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(int responseCode) {

                    }
                });
            } else {
                tvUsername.setText(R.string.tournament);
            }
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