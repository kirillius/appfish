package com.linaverde.fishingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalRecordFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PersonalRecordFragment() {
        // Required empty public constructor
    }

    public static PersonalRecordFragment newInstance(String param1, String param2) {
        PersonalRecordFragment fragment = new PersonalRecordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

        for (int i = 1; i <= 4; i++) {
            int currRes; String buttonID;
            buttonID = "set_btn" + i;
            currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
            int finalI = i;
            ((RelativeLayout) view.findViewById(currRes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Event", "set" + finalI + "clicked");
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
                    //requestHelper.executePost("catching", new String[]{"rod", "event", "time"}, new String[]{Integer.toString(finalI), "3", "time"}, null, listener);
                }
            });

            buttonID = "gone_btn" + i;
            currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
            ((RelativeLayout) view.findViewById(currRes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Event", "gone" + finalI + "clicked");
                    //requestHelper.executePost("catching", new String[]{"rod", "event", "time"}, new String[]{Integer.toString(finalI), "4", "time"}, null, listener);
                }
            });

        }

        return view;
    }
}