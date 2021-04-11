package com.linaverde.fishingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonalRecordFragment extends Fragment {

    private static final String CATCHING = "info";

    private JSONArray catchInfo;

    public PersonalRecordFragment() {
        // Required empty public constructor
    }

    public static PersonalRecordFragment newInstance(String catchInfo) {
        PersonalRecordFragment fragment = new PersonalRecordFragment();
        Bundle args = new Bundle();
        args.putString(CATCHING, catchInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                catchInfo = (new JSONObject(getArguments().getString(CATCHING)).getJSONArray("rods"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    RequestHelper requestHelper;
    UserInfo userInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_record, container, false);

        userInfo = new UserInfo(getContext());
        requestHelper = new RequestHelper(getContext());

        RequestListener listener = new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {

            }

            @Override
            public void onError(int responseCode) {

            }
        };

        int[] timerStartValueSeconds = new int[4];
        for (int i = 0; i < 4; i++) {
            timerStartValueSeconds[i] = 14 * 60 + 50;
        }

        TextView[] tvTimer = new TextView[4];
        for (int i = 1; i <= 4; i++) {
            int currRes;
            String buttonID;
            buttonID = "record_timer_" + i;
            currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
            tvTimer[i - 1] = ((TextView) view.findViewById(currRes));
            tvTimer[i - 1].setText("14:50");
        }

        for (int i = 0; i < 4; i ++){
            try {
                int id = catchInfo.getJSONObject(i).getInt("id");
                String time = catchInfo.getJSONObject(i).getString("timer");
                time = time.substring(time.indexOf(":")+1);
                tvTimer[id-1].setText(time);
                String [] timeValues = time.split(":");
                timerStartValueSeconds[id-1] = Integer.parseInt(timeValues[0])*60 + Integer.parseInt(timeValues[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        CountDownTimer[] timers = new CountDownTimer[4];
        for (int i = 0; i < 4; i++) {
            timers[i] = null;
        }

        for (int i = 1; i <= 4; i++) {
            int currRes;
            String buttonID;
            buttonID = "set_btn" + i;
            currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
            int finalI = i;
            ((RelativeLayout) view.findViewById(currRes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Event", "set" + finalI + "clicked");
                    String startTime = Integer.toString(timerStartValueSeconds[finalI - 1] / 60) + ":" + Integer.toString(timerStartValueSeconds[finalI - 1] % 60);
                    tvTimer[finalI - 1].setText(startTime);
                    if (timers[finalI - 1] == null) {
                        timers[finalI - 1] = new CountDownTimer(timerStartValueSeconds[finalI - 1] * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                int sec = (int) (millisUntilFinished / 1000);
                                String currTime = Integer.toString((sec) / 60) + ":" + Integer.toString(sec % 60);
                                tvTimer[finalI - 1].setText(currTime);
                            }

                            @Override
                            public void onFinish() {
                                tvTimer[finalI - 1].setText("00:00");
                            }
                        };
                        timers[finalI - 1].start();
                    } else {
                        timers[finalI - 1].cancel();
                        timers[finalI - 1].start();
                    }
                    //requestHelper.executePost("catching", new String[]{"rod", "event", "time"}, new String[]{Integer.toString(finalI), "1", "time"}, null, listener);
                }
            });

            buttonID = "bite_btn" + i;
            currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
            ((RelativeLayout) view.findViewById(currRes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Event", "bite" + finalI + "clicked");
                    //requestHelper.executePost("catching", new String[]{"rod", "event", "time"}, new String[]{Integer.toString(finalI), "2", "time"}, null, listener);
                }
            });

            buttonID = "fish_btn" + i;
            currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
            ((RelativeLayout) view.findViewById(currRes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Event", "fish" + finalI + "clicked");
                    if (timers[finalI - 1] != null)
                        timers[finalI - 1].cancel();
                    //requestHelper.executePost("catching", new String[]{"rod", "event", "time"}, new String[]{Integer.toString(finalI), "3", "time"}, null, listener);
                }
            });

            buttonID = "gone_btn" + i;
            currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
            ((RelativeLayout) view.findViewById(currRes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Event", "gone" + finalI + "clicked");
                    if (timers[finalI - 1] != null)
                        timers[finalI - 1].cancel();
                    //requestHelper.executePost("catching", new String[]{"rod", "event", "time"}, new String[]{Integer.toString(finalI), "4", "time"}, null, listener);
                }
            });

        }

        return view;
    }
}