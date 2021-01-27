package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.FishChangedRequestListener;
import com.linaverde.fishingapp.interfaces.ViolationChangedRequestListener;
import com.linaverde.fishingapp.models.Fish;
import com.linaverde.fishingapp.models.FishDictionaryItem;
import com.linaverde.fishingapp.models.Violation;
import com.linaverde.fishingapp.models.ViolationDictionaryItem;
import com.linaverde.fishingapp.services.DialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditViolationFragment extends Fragment {

    private static final String FOUL_JSON = "json";
    private static final String DICT = "dict";
    private static final String TEAM_ID = "team";
    private static final String SECTOR = "sector";
    private static final String STAGE_ID = "stage";

    private String foulJson;
    private JSONArray dict;
    private String teamId;
    private int sector;
    private String stageId;

    ViolationChangedRequestListener listener;

    public EditViolationFragment() {
        // Required empty public constructor
    }

    public static EditViolationFragment newInstance(String foul, String dict, String teamId, String stageId, int sector) {
        EditViolationFragment fragment = new EditViolationFragment();
        Bundle args = new Bundle();
        args.putString(FOUL_JSON, foul);
        args.putString(DICT, dict);
        args.putString(TEAM_ID, teamId);
        args.putString(STAGE_ID, stageId);
        args.putInt(SECTOR, sector);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            foulJson = getArguments().getString(FOUL_JSON);
            teamId = getArguments().getString(TEAM_ID);
            stageId = getArguments().getString(STAGE_ID);
            sector = getArguments().getInt(SECTOR);
            try {
                dict = new JSONArray(getArguments().getString(DICT));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    EditText etHours, etMin, etSec;
    TextView tvType;
    RelativeLayout rlButtonConfirm;
    ViolationDictionaryItem[] dictionary;
    Violation foul;
    LinearLayout editTime;
    boolean newFoul;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_violation, container, false);

        etHours = view.findViewById(R.id.et_hours);
        etMin = view.findViewById(R.id.et_min);
        etSec = view.findViewById(R.id.et_sec);
        tvType = view.findViewById(R.id.tv_violation_type);
        rlButtonConfirm = view.findViewById(R.id.rl_button_violation_save);
        editTime = view.findViewById(R.id.ll_edit_time);

        dictionary = new ViolationDictionaryItem[dict.length()];
        try {
            for (int i = 0; i < dict.length(); i++) {
                dictionary[i] = new ViolationDictionaryItem(dict.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (foulJson != null) {
            newFoul = false;
            try {
                foul = new Violation(new JSONObject(foulJson));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            editTime.setVisibility(View.GONE);
        } else {
            newFoul = true;
            foul = new Violation(dictionary[0].getId());
        }

        String[] time = foul.getTime().split(":");
        etHours.setText(time[0]);
        etMin.setText(time[1]);
        etSec.setText(time[2]);

        for (ViolationDictionaryItem dictionaryItem : dictionary) {
            if (foul.getViolationId().equals(dictionaryItem.getId())) {
                tvType.setText(dictionaryItem.getName());
            }
        }

        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.createSelectViolationTypeDialog(getContext(), getLayoutInflater(), getString(R.string.select_violation_type),
                        dictionary, foul.getViolationId(), new CompleteActionListener() {
                            @Override
                            public void onOk(String input) {
                                for (ViolationDictionaryItem dictionaryItem : dictionary) {
                                    if (input.equals(dictionaryItem.getId())) {
                                        tvType.setText(dictionaryItem.getName());
                                    }
                                }
                                foul.setViolationId(input);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });

        rlButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                if (newFoul) {
                    String hours, mins, sec;
                    hours = etHours.getText().toString();
                    mins = etMin.getText().toString();
                    sec = etSec.getText().toString();
                    while (hours.length() < 2) {
                        hours = "0" + hours;
                    }
                    while (mins.length() < 2) {
                        mins = "0" + mins;
                    }
                    while (sec.length() < 2) {
                        sec = "0" + sec;
                    }
                    String time = hours + ":" + mins + ":" + sec;
                    foul.setTime(time);
                }
                try {
                    if (!newFoul) {
                        object.put("id", foul.getId());
                    }
                    object.put("foulId", foul.getViolationId());
                    object.put("time", foul.getDateTime());
                    listener.violationChangedRequest(stageId, teamId, object.toString(), sector);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViolationChangedRequestListener) {
            //init the listener
            listener = (ViolationChangedRequestListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ViolationChangedRequestListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}