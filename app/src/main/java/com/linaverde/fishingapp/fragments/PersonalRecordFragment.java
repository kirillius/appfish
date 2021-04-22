package com.linaverde.fishingapp.fragments;

import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.RecordButtonsAccumulator;
import com.linaverde.fishingapp.models.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalRecordFragment extends Fragment {

    private static final String CATCHING = "info";
    private static final String ORIENTATION = "orientation";

    private JSONArray catchInfo;
    private boolean vertical;

    public PersonalRecordFragment() {
        // Required empty public constructor
    }

    public static PersonalRecordFragment newInstance(String catchInfo, boolean vertical) {
        PersonalRecordFragment fragment = new PersonalRecordFragment();
        Bundle args = new Bundle();
        args.putString(CATCHING, catchInfo);
        args.putBoolean(ORIENTATION, vertical);
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
            vertical = getArguments().getBoolean(ORIENTATION);
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


//        if (!vertical) {
//            LinearLayout rodNum = view.findViewById(R.id.ll_rods_names);
//            rodNum.setVisibility(View.GONE);
//        }

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

                //Инициализация бэков
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

                if (!vertical){
                    Log.d("Orientation", "not vertical");
                }
                if (!vertical)
                    for (int j = 0; j < 4; j++) {
                        backs[j].setScaleType(ImageView.ScaleType.FIT_XY);
                        backs[j].setAdjustViewBounds(false);
                    }

                //Инициализация таймера
                buttonID = "record_timer_" + id;
                currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
                TextView timer = ((TextView) view.findViewById(currRes));

                //инициализация счетчиков

                buttonID = "spod_" + id;
                currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
                TextView spod = ((TextView) view.findViewById(currRes));

                buttonID = "cobr_" + id;
                currRes = getResources().getIdentifier(buttonID, "id", getContext().getPackageName());
                TextView cobr = ((TextView) view.findViewById(currRes));

                RecordButtonsAccumulator acc = new RecordButtonsAccumulator(getContext(), userInfo.getTeamId(),
                        timer, catchInfo.getJSONObject(i), spod, cobr, progressBar);

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