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
import com.linaverde.fishingapp.models.Fish;
import com.linaverde.fishingapp.models.FishDictionaryItem;
import com.linaverde.fishingapp.services.DialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditFishFragment extends Fragment {


    private static final String FISH_JSON = "json";
    private static final String PIN = "pin";
    private static final String DICT = "dict";
    private static final String TEAM_ID = "team";
    private static final String SECTOR = "sector";
    private static final String STAGE_ID = "stage";

    private String fishJson;
    private JSONArray dict;
    private String pin;
    private String teamId;
    private int sector;
    private String stageId;
    FishChangedRequestListener listener;

    public EditFishFragment() {
        // Required empty public constructor
    }

    public static EditFishFragment newInstance(String fish, String dict, String pin, String teamId, String stageId, int sector) {
        EditFishFragment fragment = new EditFishFragment();
        Bundle args = new Bundle();
        args.putString(FISH_JSON, fish);
        args.putString(PIN, pin);
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
            fishJson = getArguments().getString(FISH_JSON);
            pin = getArguments().getString(PIN);
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
    EditText etWeight;
    EditText etPin;
    RelativeLayout rlButtonConfirm;
    FishDictionaryItem[] dictionary;
    Fish fish;
    LinearLayout editTime;
    boolean newFish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_fish, container, false);

        //инициализация
        etHours = view.findViewById(R.id.et_hours);
        etMin = view.findViewById(R.id.et_min);
        etSec = view.findViewById(R.id.et_sec);
        tvType = view.findViewById(R.id.tv_fish_type);
        etWeight = view.findViewById(R.id.et_weight);
        rlButtonConfirm = view.findViewById(R.id.rl_button_fish_save);
        etPin = view.findViewById(R.id.et_pin);
        editTime = view.findViewById(R.id.ll_edit_time);

        dictionary = new FishDictionaryItem[dict.length()];
        try {
            for (int i = 0; i < dict.length(); i++) {
                dictionary[i] = new FishDictionaryItem(dict.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (fishJson != null) {
            newFish = false;
            try {
                fish = new Fish(new JSONObject(fishJson));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            editTime.setVisibility(View.GONE);
        } else {
            newFish = true;
            fish = new Fish(dictionary[0].getId());
        }

        //заполнение полей
        String[] time = fish.getTime().split(":");
        etHours.setText(time[0]);
        etMin.setText(time[1]);
        etSec.setText(time[2]);

        for (FishDictionaryItem fishDictionaryItem : dictionary) {
            if (fish.getFishId().equals(fishDictionaryItem.getId())) {
                tvType.setText(fishDictionaryItem.getName());
            }
        }

        etWeight.setText(Integer.toString(fish.getWeight()));
        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.createSelectFishTypeDialog(getContext(), getLayoutInflater(), getString(R.string.select_fish_type),
                        dictionary, fish.getFishId(), new CompleteActionListener() {
                            @Override
                            public void onOk(String input) {
                                for (FishDictionaryItem fishDictionaryItem : dictionary) {
                                    if (input.equals(fishDictionaryItem.getId())) {
                                        tvType.setText(fishDictionaryItem.getName());
                                    }
                                }
                                fish.setFishId(input);
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
                if (etPin.getText().toString().equals(pin)) {
                    if (!etWeight.getText().toString().equals("")) {
                        JSONObject object = new JSONObject();
                        if (newFish) {
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
                            fish.setTime(time);
                        }
                        try {
                            if (!newFish) {
                                object.put("id", fish.getId());
                            }
                            object.put("fishId", fish.getFishId());
                            object.put("weight", Integer.parseInt(etWeight.getText().toString()));
                            object.put("time", fish.getDateTime());
                            listener.fishChangedRequest(stageId, teamId, pin, object.toString(), sector);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.input_weight), null);
                    }
                } else {
                    DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.wrong_pin), null);
                }
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FishChangedRequestListener) {
            //init the listener
            listener = (FishChangedRequestListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FishChangedRequestListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}