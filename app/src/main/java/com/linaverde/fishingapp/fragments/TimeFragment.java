package com.linaverde.fishingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.linaverde.fishingapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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
        int currentTime = rightNow.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format

        Log.d("Current time", String.valueOf(currentTime));
        switch (currentTime) {
            case 6:
                view.findViewById(R.id.time_icon_1).setVisibility(View.VISIBLE);
                break;
            case 7:
                view.findViewById(R.id.time_icon_2).setVisibility(View.VISIBLE);
                break;
            case 8:
                view.findViewById(R.id.time_icon_3).setVisibility(View.VISIBLE);
                break;
            case 9:
                view.findViewById(R.id.time_icon_4).setVisibility(View.VISIBLE);
                break;
            case 10:
                view.findViewById(R.id.time_icon_5).setVisibility(View.VISIBLE);
                break;
            case 11:
                view.findViewById(R.id.time_icon_6).setVisibility(View.VISIBLE);
                break;
            case 12:
                view.findViewById(R.id.time_icon_7).setVisibility(View.VISIBLE);
                break;
            case 13:
                view.findViewById(R.id.time_icon_8).setVisibility(View.VISIBLE);
                break;
            case 14:
                view.findViewById(R.id.time_icon_9).setVisibility(View.VISIBLE);
                break;
            case 15:
                view.findViewById(R.id.time_icon_10).setVisibility(View.VISIBLE);
                break;
            case 16:
                view.findViewById(R.id.time_icon_11).setVisibility(View.VISIBLE);
                break;
            case 17:
                view.findViewById(R.id.time_icon_12).setVisibility(View.VISIBLE);
                break;
            case 18:
                view.findViewById(R.id.time_icon_13).setVisibility(View.VISIBLE);
                break;
            case 19:
                view.findViewById(R.id.time_icon_14).setVisibility(View.VISIBLE);
                break;
            case 20:
                view.findViewById(R.id.time_icon_15).setVisibility(View.VISIBLE);
                break;
            case 21:
                view.findViewById(R.id.time_icon_16).setVisibility(View.VISIBLE);
                break;
            case 22:
                view.findViewById(R.id.time_icon_17).setVisibility(View.VISIBLE);
                break;
            default:
                view.findViewById(R.id.time_icon_18).setVisibility(View.VISIBLE);
                break;
        }


        return view;
    }
}