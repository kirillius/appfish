package com.linaverde.fishingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.CastTimerAccumulator;
import com.linaverde.fishingapp.services.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class TimeFragment extends Fragment {


    public TimeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time, container, false);

        Calendar rightNow = Calendar.getInstance();
        int currHour = rightNow.get(Calendar.HOUR_OF_DAY);

        UserInfo userInfo = new UserInfo(getContext());
        String startTime = userInfo.getTime();
        String[] eventValues = startTime.split(":");
        int startHour = Integer.parseInt(eventValues[0]);

        int diff = currHour - startHour;
        if (diff < 0)
            diff = 24 + diff;

        Log.d("CurrHour", Integer.toString(currHour));
        Log.d("StartHour", Integer.toString(startHour));
        Log.d("Difference", Integer.toString(diff));

        int i = diff+1;
        String buttonID = "time_icon_" + i;
        int currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
        view.findViewById(currRes).setVisibility(View.VISIBLE);


        return view;
    }
}