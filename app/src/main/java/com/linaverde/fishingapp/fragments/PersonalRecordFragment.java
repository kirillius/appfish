package com.linaverde.fishingapp.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.models.RecordButtonsAccumulator;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    UserInfo userInfo;
    List<RecordButtonsAccumulator> accumulator;
    ContentLoadingProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_record, container, false);

        userInfo = new UserInfo(getContext());
        accumulator = new ArrayList<>();
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.hide();

        for (int i = 0; i < 4; i++) {
            try {
                int currRes;
                String buttonID;
                int id = catchInfo.getJSONObject(i).getInt("id");

                //Инициализация кнопок
                RelativeLayout[] btn = new RelativeLayout[4];

                buttonID = "set_btn" + id;
                currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
                btn[0] = (RelativeLayout) view.findViewById(currRes);

                buttonID = "bite_btn" + id;
                currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
                btn[1] = (RelativeLayout) view.findViewById(currRes);

                buttonID = "fish_btn" + id;
                currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
                btn[2] = (RelativeLayout) view.findViewById(currRes);

                buttonID = "gone_btn" + id;
                currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
                btn[3] = (RelativeLayout) view.findViewById(currRes);

                //Инициализация беков
                ImageView[] backs = new ImageView[4];

                buttonID = "set_btn" + id + "_back";
                currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
                backs[0] = ((ImageView) view.findViewById(currRes));

                buttonID = "bite_btn" + id + "_back";
                currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
                backs[1] = ((ImageView) view.findViewById(currRes));

                buttonID = "fish_btn" + id + "_back";
                currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
                backs[2] = ((ImageView) view.findViewById(currRes));

                buttonID = "gone_btn" + id + "_back";
                currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
                backs[3] = ((ImageView) view.findViewById(currRes));

                //Инициализация таймера
                buttonID = "record_timer_" + id;
                currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
                TextView timer = ((TextView) view.findViewById(currRes));
                String time = null;

                time = catchInfo.getJSONObject(i).getString("timer");

                time = time.substring(time.indexOf(":") + 1);
                timer.setText(time);
                String[] timeValues = time.split(":");
                int startTime = Integer.parseInt(timeValues[0]) * 60 + Integer.parseInt(timeValues[1]);

                RecordButtonsAccumulator acc = new RecordButtonsAccumulator(getContext(), userInfo.getTeamId(),
                        timer, catchInfo.getJSONObject(i), progressBar);

                acc.setButtonsBack(backs);
                acc.setButtons(btn);

                accumulator.add(acc);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return view;
    }


}