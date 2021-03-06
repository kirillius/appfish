package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.RodPositionChangedListener;
import com.linaverde.fishingapp.interfaces.RodsSettingsChangeListener;
import com.linaverde.fishingapp.interfaces.RodsSettingsListener;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.RodsSettingsListAdapter;
import com.linaverde.fishingapp.models.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class RodsDetailFragment extends Fragment implements RodsSettingsChangeListener {

    private static final String JSON = "json";
    private static final String TYPE = "spod";
    private static final String CAST = "cast";

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
    MapFragment mapFragment = null;
    UserInfo userInfo;
    JSONArray rodsPositions = null;
    private boolean cast;

    public RodsDetailFragment() {
        // Required empty public constructor
    }

    public static RodsDetailFragment newInstance(String json, String type, boolean cast) {
        RodsDetailFragment fragment = new RodsDetailFragment();
        Bundle args = new Bundle();
        args.putString(JSON, json);
        args.putString(TYPE, type);
        args.putBoolean(CAST, cast);
        fragment.setArguments(args);
        return fragment;
    }

    public void changeRodPosition(JSONObject rodObj) {
        try {
            for (int i = 0; i < rodsPositions.length(); i++) {
                if (rodsPositions.getJSONObject(i).getInt("id") == rodObj.getInt("id")) {
                    rodsPositions.remove(i);
                    rodsPositions.put(rodObj);
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            cast = getArguments().getBoolean(CAST);

        }
        newParams = new JSONArray();
    }

    JSONObject timerJson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rods_detail, container, false);

        userInfo = new UserInfo(getContext());
        ((TextView) view.findViewById(R.id.tv_team_name)).setText(userInfo.getCaption());
        buttons = view.findViewById(R.id.ll_buttons);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.hide();
        rodsSettingsChangeListener = this;
        timerJson = null;
        List<JSONObject> section1 = new ArrayList<>();
        List<JSONObject> section2 = new ArrayList<>();

        try {
            rodId = settings.getJSONArray("rods").getJSONObject(0).getInt("rodId");
            switch (rodId) {
                case 2:
                    ((ImageView) view.findViewById(R.id.iv_rod_icon)).setImageDrawable(getContext().getDrawable(R.drawable.rod_2_icon));
                    break;
                case 3:
                    ((ImageView) view.findViewById(R.id.iv_rod_icon)).setImageDrawable(getContext().getDrawable(R.drawable.rod_3_icon));
                    break;
                case 4:
                    ((ImageView) view.findViewById(R.id.iv_rod_icon)).setImageDrawable(getContext().getDrawable(R.drawable.rod_4_icon));
                    break;
            }


            Log.d("RodId", Integer.toString(rodId));
            JSONArray array = settings.getJSONArray("rods").getJSONObject(0).getJSONArray("settings");
            for (int i = 0; i < array.length(); i++) {
                String paramId = array.getJSONObject(i).getJSONObject("param").getString("id");
                if (paramId.equals("TIMER")) {
                    timerJson = array.getJSONObject(i);
                } else if (!paramId.equals("COBR_QTY") && !paramId.equals("SPOD_QTY")) {
                    if (array.getJSONObject(i).getJSONObject("param").getInt("section") == 1) {
                        section1.add(array.getJSONObject(i));
                    } else {
                        section2.add(array.getJSONObject(i));
                    }
                }
            }

            Comparator<JSONObject> comparator = new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    try {
                        if (o1.getJSONObject("param").getInt("groupId") <
                                o2.getJSONObject("param").getInt("groupId")) {
                            return -1;
                        } else if (o1.getJSONObject("param").getInt("groupId") >
                                o2.getJSONObject("param").getInt("groupId")) {
                            return 1;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            };

            //сгруппировали по groupId
            section1.sort(comparator);
            section2.sort(comparator);

            int i = 1;
            while (i < section1.size()) {
                if (section1.get(i - 1).getJSONObject("param").getInt("groupId") !=
                        section1.get(i).getJSONObject("param").getInt("groupId")) {
                    section1.add(i, (new JSONObject()).put("divider", true));
                    i++;
                }
                i++;
            }

            i = 1;
            while (i < section2.size()) {
                if (section2.get(i - 1).getJSONObject("param").getInt("groupId") !=
                        section2.get(i).getJSONObject("param").getInt("groupId")) {
                    section2.add(i, (new JSONObject()).put("divider", true));
                    i++;
                }
                i++;
            }

            adapter1 = new RodsSettingsListAdapter(getContext(), section1, progressBar, rodId, rodType, cast, this);
            adapter2 = new RodsSettingsListAdapter(getContext(), section2, progressBar, rodId, rodType, cast, this);

            ((ListView) view.findViewById(R.id.list_rods_settings_1)).setAdapter(adapter1);
            ((ListView) view.findViewById(R.id.list_rods_settings_2)).setAdapter(adapter2);

            cancel = view.findViewById(R.id.rl_cancel);
            confirm = view.findViewById(R.id.rl_confirm);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.updateDetailedFragment(rodType, rodId, cast);
                }
            });

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        boolean added;
                        for (int i = 0; i < section1.size(); i++) {
                            added = false;
                            if (!section1.get(i).has("divider")) {
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
                                    if (value.getClass() == JSONObject.class) {
                                        missedParam.put("valueId", ((JSONObject) value).getString("id"));
                                    } else {
                                        missedParam.put("valueId", value);
                                    }
                                    newParams.put(missedParam);
                                }
                            }
                        }
                        for (int i = 0; i < section2.size(); i++) {
                            added = false;
                            if (!section2.get(i).has("divider")) {
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
                                    if (value.getClass() == JSONObject.class) {
                                        missedParam.put("valueId", ((JSONObject) value).getString("id"));
                                    } else {
                                        missedParam.put("valueId", value);
                                    }
                                    newParams.put(missedParam);
                                }
                            }
                        }
                        if (timerJson != null) {
                            JSONObject missedParam = new JSONObject();
                            missedParam.put("paramId", timerJson.getJSONObject("param").getString("id"));
                            missedParam.put("valueId", timerJson.getString("value"));
                            newParams.put(missedParam);
                        }
//                        JSONObject spodParam = new JSONObject();
//                        spodParam.put("paramId", "SPOD_QTY");
//                        spodParam.put("valueId", 0);
//                        JSONObject cobrParam = new JSONObject();
//                        cobrParam.put("paramId", "COBR_QTY");
//                        cobrParam.put("valueId", 0);
//                        newParams.put(spodParam);
//                        newParams.put(cobrParam);
                        listener.sendRodsSettings(rodType, rodId, newParams.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            LinearLayout llTimer = view.findViewById(R.id.ll_timer);
            if (timerJson != null) {
                String timeValue = timerJson.getString("value");
                String dateValue = timeValue.split("T")[0];
                timeValue = timeValue.split("T")[1];
                llTimer.setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.timer_value)).setText(timeValue);
                String finalTimeValue = timeValue;
                if (!cast)
                    llTimer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogBuilder.createTimeInputDialog(getContext(), getLayoutInflater(), "Введите время заброса", null, new CompleteActionListener() {
                                @Override
                                public void onOk(String input) {
                                    if (!input.equals(finalTimeValue)) {
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

        if (newParams.length() != 0) {
            buttons.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void paramChanged(String paramId, String value, boolean addinfo) {
        try {
            JSONObject object = new JSONObject();
            object.put("paramId", paramId);
            for (int i = 0; i < newParams.length(); i++) {
                if (newParams.getJSONObject(i).getString("paramId").equals(paramId)) {
                    newParams.remove(i);
                    break;
                }
            }

            if (isJSONValid(value)) {
                object.put("valueId", (new JSONObject(value)).getString("id"));
            } else {
                if (addinfo) {
                    object.put("addinfo", value);
                    object.put("valueId", "");
                } else {
                    object.put("valueId", value);
                }
            }
            newParams.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        if (newParams.length() != 0) {
            buttons.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void openMap(RodPositionChangedListener positionChangedListener) {
        if (mapFragment == null) {
            progressBar.show();
            RequestHelper requestHelper = new RequestHelper(getContext());
            requestHelper.executeGet("map", new String[]{"match", "team"}, new String[]{userInfo.getMatchId(), userInfo.getTeamId()}, new RequestListener() {
                @Override
                public void onComplete(JSONObject json) {
                    try {
                        rodsPositions = json.getJSONArray("rods");
                        mapFragment = MapFragment.newInstance(json.toString(), rodId, rodsPositions.toString(), rodType.equals("spod"));
                        progressBar.hide();
                        listener.openMapFragment(mapFragment, positionChangedListener);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(int responseCode) {

                }
            });
        } else {
            try {
                rodsPositions = mapFragment.getRodsPositions();
                changeRodPosition(adapter1.getRodPositionJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mapFragment.setRodsPositions(rodsPositions);
            listener.openMapFragment(mapFragment, positionChangedListener);
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