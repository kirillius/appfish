package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.RodsSettingsChangeListener;
import com.linaverde.fishingapp.interfaces.RodsSettingsListener;
import com.linaverde.fishingapp.interfaces.TeamListClickListener;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.RodsSettingsListAdapter;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RodsDetailFragment extends Fragment implements RodsSettingsChangeListener {

    private static final String JSON = "json";
    private static final String TYPE = "spod";

    private JSONObject settings;
    private String rodType;
    private int rodId;
    private JSONArray newParams;
    private RodsSettingsListAdapter adapter1, adapter2;
    private LinearLayout buttons;
    private RelativeLayout confirm, cancel;
    RodsSettingsChangeListener rodsSettingsChangeListener;
    RodsSettingsListener listener;
    private ContentLoadingProgressBar progressBar;

    public RodsDetailFragment() {
        // Required empty public constructor
    }

    public static RodsDetailFragment newInstance(String json, String type) {
        RodsDetailFragment fragment = new RodsDetailFragment();
        Bundle args = new Bundle();
        args.putString(JSON, json);
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                settings = new JSONObject(getArguments().getString(JSON));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            rodType = getArguments().getString(TYPE);

        }
        newParams = new JSONArray();
    }

    JSONObject timerJson;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rods_detail, container, false);

        UserInfo userInfo = new UserInfo(getContext());
        ((TextView)view.findViewById(R.id.tv_team_name)).setText(userInfo.getCaption());
        buttons = view.findViewById(R.id.ll_buttons);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.hide();
        rodsSettingsChangeListener = this;
        timerJson = null;
        List<JSONObject> section1 = new ArrayList<>();
        List<JSONObject> section2 = new ArrayList<>();
        try {
            rodId = settings.getJSONArray("rods").getJSONObject(0).getInt("rodId");
            Log.d("RodId", Integer.toString(rodId));
            JSONArray array = settings.getJSONArray("rods").getJSONObject(0).getJSONArray("settings");
            for (int i = 0; i < array.length(); i++){
                if (array.getJSONObject(i).getJSONObject("param").getString("id").equals("TIMER")){
                    timerJson = array.getJSONObject(i);
                }
                else if (array.getJSONObject(i).getJSONObject("param").getInt("section") == 1){
                    section1.add(array.getJSONObject(i));
                } else {
                    section2.add(array.getJSONObject(i));
                }
            }

            adapter1 = new RodsSettingsListAdapter(getContext(), section1, progressBar, this);
            adapter2 = new RodsSettingsListAdapter(getContext(), section2, progressBar, this);

            ((ListView)view.findViewById(R.id.list_rods_settings_1)).setAdapter(adapter1);
            ((ListView)view.findViewById(R.id.list_rods_settings_2)).setAdapter(adapter2);

            cancel = view.findViewById(R.id.rl_cancel);
            confirm = view.findViewById(R.id.rl_confirm);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.updateDetailedFragment(rodType, rodId);
                }
            });

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        boolean added;
                        for (int i = 0; i < section1.size(); i++) {
                            added = false;
                            String param = section1.get(i).getJSONObject("param").getString("id");
                            for (int j = 0; j < newParams.length(); j++) {
                                String changed;
                                changed = newParams.getJSONObject(j).getString("paramId");
                                if (param.equals(changed)) {
                                    added = true;
                                    break;
                                }
                            }
                            if (!added) {
                                JSONObject missedParam = new JSONObject();
                                missedParam.put("paramId", param);
                                Object value = section1.get(i).get("value");
                                if (value.getClass() == JSONObject.class){
                                    missedParam.put("valueId", ((JSONObject) value).getString("id"));
                                } else {
                                    missedParam.put("valueId", value);
                                }
                                newParams.put(missedParam);
                            }
                        }
                        for (int i = 0; i < section2.size(); i++) {
                            added = false;
                            String param = section2.get(i).getJSONObject("param").getString("id");
                            for (int j = 0; j < newParams.length(); j++) {
                                String changed;
                                changed = newParams.getJSONObject(j).getString("paramId");
                                if (param.equals(changed)) {
                                    added = true;
                                    break;
                                }
                            }
                            if (!added) {
                                JSONObject missedParam = new JSONObject();
                                missedParam.put("paramId", param);
                                Object value = section2.get(i).get("value");
                                if (value.getClass() == JSONObject.class){
                                    missedParam.put("valueId", ((JSONObject) value).getString("id"));
                                } else {
                                    missedParam.put("valueId", value);
                                }
                                newParams.put(missedParam);
                            }
                        }
                        if (timerJson != null){
                            JSONObject missedParam = new JSONObject();
                            missedParam.put("paramId", timerJson.getJSONObject("param").getString("id"));
                            missedParam.put("valueId", timerJson.getString("value"));
                            newParams.put(missedParam);
                        }
                        listener.sendRodsSettings(rodType, rodId, newParams.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            LinearLayout llTimer = view.findViewById(R.id.ll_timer);
            if (timerJson != null){
                String timeValue = timerJson.getString("value");
                String dateValue = timeValue.split("T")[0];
                timeValue = timeValue.split("T")[1];
                llTimer.setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.timer_value)).setText(timeValue);
                String finalTimeValue = timeValue;
                llTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogBuilder.createTimeInputDialog(getContext(), getLayoutInflater(), "Введите время заброса", null, new CompleteActionListener() {
                            @Override
                            public void onOk(String input) {
                                if (!input.equals(finalTimeValue)){
                                    buttons.setVisibility(View.VISIBLE);
                                    Log.d("New timer value", dateValue + "T" + input);
                                    ((TextView) view.findViewById(R.id.timer_value)).setText(input);
                                    timerJson.remove("value");
                                    try {
                                        timerJson.put("value", dateValue + "T" + input);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void paramChanged(String paramId, String value) {
        try {
            JSONObject object = new JSONObject();
            object.put("paramId", paramId);
            for (int i = 0; i < newParams.length(); i++) {
                if (newParams.getJSONObject(i).getString("paramId").equals(paramId)) {
                    newParams.remove(i);
                    break;
                }
            }
            if (isJSONValid(value)){
                object.put("valueId", (new JSONObject(value)).getString("id"));
            } else {
                object.put("valueId", value);
            }
            newParams.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        if (newParams.length() != 0){
            buttons.setVisibility(View.VISIBLE);
        }
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RodsSettingsListener) {
            //init the listener
            listener = (RodsSettingsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RodsSettingsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}