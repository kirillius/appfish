package com.linaverde.fishingapp.services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.widget.ContentLoadingProgressBar;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.RodPositionChangedListener;
import com.linaverde.fishingapp.interfaces.RodsSettingsChangeListener;
import com.linaverde.fishingapp.interfaces.RodsSettingsParamSwithcListener;
import com.linaverde.fishingapp.models.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class RodsSettingsListAdapter extends ArrayAdapter<JSONObject> {

    Context context;
    List<JSONObject> values;
    RodsSettingsChangeListener changeListener;
    ContentLoadingProgressBar progressBar;
    LayoutInflater inflater;
    RodPositionChangedListener positionChangedListener;
    JSONObject landmarkObj;
    JSONObject distanceObj;
    JSONArray rodsPositions;
    UserInfo userInfo;
    int rodId;
    JSONObject mapJson;
    String rodType;
    boolean cast;

    public RodsSettingsListAdapter(Context context, List<JSONObject> values,
                                   ContentLoadingProgressBar progressBar, int rodId, String rodType, boolean cast, RodsSettingsChangeListener changeListener) {
        super(context, R.layout.rods_settings_list_item, values);
        this.context = context;
        this.values = values;
        this.changeListener = changeListener;
        this.progressBar = progressBar;
        this.rodId = rodId;
        this.rodType = rodType;
        userInfo = new UserInfo(context);
        this.cast = cast;

        RequestHelper requestHelper = new RequestHelper(getContext());
        progressBar.show();
        requestHelper.executeGet("map", new String[]{"match", "team"}, new String[]{userInfo.getMatchId(), userInfo.getTeamId()}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                try {
                    rodsPositions = json.getJSONArray("rods");
                    mapJson = json;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.hide();
            }

            @Override
            public void onError(int responseCode) {

            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        try {
            JSONObject object = values.get(position);
            if (object.has("divider")) {
                rowView = inflater.inflate(R.layout.list_divider, parent, false);
            } else {
                rowView = inflater.inflate(R.layout.rods_settings_list_item,
                        parent, false);

                String valueId = null;
                TextView tvName = rowView.findViewById(R.id.grid_param_name);
                TextView tvValue = rowView.findViewById(R.id.grid_param_value);

                String paramId = object.getJSONObject("param").getString("id");
                if (paramId.equals("LANDMARK"))
                    landmarkObj = object;
                if (paramId.equals("DISTANCE"))
                    distanceObj = object;
                tvName.setText(object.getJSONObject("param").getString("name"));
                Object value = object.get("value");
                if (value.getClass() == JSONObject.class) {
                    if (((JSONObject) value).has("addInfo") && !((JSONObject) value).getString("addInfo").equals("")) {
                        tvValue.setText(((JSONObject) value).getString("addInfo"));
                    } else {
                        tvValue.setText(((JSONObject) value).getString("name"));
                        valueId = ((JSONObject) value).getString("id");
                    }
                } else {
                    tvValue.setText(value.toString());
                }

                if (paramId.equals("DISTANCE"))
                    tvValue.setText(tvValue.getText().toString() + "м");


                String finalParamId = paramId;
                String finalValueId = valueId;
                if (!cast)
                    rowView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            CompleteActionListener listener = new CompleteActionListener() {
                                @Override
                                public void onOk(String input) {
                                    if (!input.isEmpty()) {
                                        try {
                                            values.get(position).remove("value");
                                            values.get(position).put("value", input);
                                            changeListener.paramChanged(object.getJSONObject("param").getString("id"),
                                                    input, false);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }

                                @Override
                                public void onCancel() {

                                }
                            };

                            RodsSettingsParamSwithcListener rodsSwithcListener = new RodsSettingsParamSwithcListener() {
                                @Override
                                public void onOk(String input, boolean info) {
                                    if (!input.isEmpty()) {
                                        try {
                                            values.get(position).remove("value");
                                            if (!info) {
                                                values.get(position).put("value", new JSONObject(input));
                                            } else {
                                                values.get(position).put("value", input);
                                            }
                                            changeListener.paramChanged(object.getJSONObject("param").getString("id"),
                                                    input, info);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onClear() {
                                    progressBar.show();
                                    RequestHelper requestHelper = new RequestHelper(context);
                                    requestHelper.executeDelete("rods", new String[]{"team", "rod", "rodType"},
                                            new String[]{userInfo.getTeamId(), Integer.toString(rodId), rodType},
                                            new RequestListener() {
                                                @Override
                                                public void onComplete(JSONObject json) {
                                                    progressBar.hide();
                                                }

                                                @Override
                                                public void onError(int responseCode) {
                                                    progressBar.hide();
                                                }
                                            });
                                }

                                @Override
                                public void onCancel() {

                                }
                            };

                            try {
                                String id = object.getJSONObject("param").getString("id");
                                if (id.equals("DISTANCE") || id.equals("LANDMARK")) {
                                    RodPositionChangedListener positionListener = new RodPositionChangedListener() {
                                        @Override
                                        public void rodPositionChanged(int rodId, String landmark, double distance) {
                                            try {
                                                distanceObj.remove("value");
                                                distanceObj.put("value", Double.toString(distance));
                                                landmarkObj.remove("value");
                                                landmarkObj.put("value", landmark);
                                                changeListener.paramChanged("LANDMARK", landmark, false);
                                                changeListener.paramChanged("DISTANCE", Double.toString(distance), false);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    Log.d("Rodid to map", Integer.toString(rodId));
                                    changeListener.openMap(positionListener);
                                } else {
                                    String lastUpdate = object.getJSONObject("param").getString("updateDate");
                                    String type = object.getJSONObject("param").getString("type");
                                    switch (type) {
                                        case "number":
                                            DialogBuilder.createInputNumberDialog(getContext(), inflater,
                                                    "Введите числовое значение", false, listener);
                                            break;
                                        case "string":
                                            DialogBuilder.createInputStringDialog(getContext(), inflater,
                                                    "Введите значение", listener);
                                            break;
                                        case "object":
                                            createTypeSelect(object.getJSONObject("param").getString("name"),lastUpdate,
                                                    finalParamId, finalValueId, tvName, rodsSwithcListener);
                                            break;
                                    }
                                }
                            } catch (
                                    JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
            }
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

        return rowView;
    }


    private void createTypeSelect(String settingName, String lastUpdate, String paramId, String valueId, TextView tvName, RodsSettingsParamSwithcListener listener) {
        RodsSettingsStorage storage = new RodsSettingsStorage(context);
        if (storage.getParams(paramId) == null) {
            getNewParamDict(storage, settingName, lastUpdate, paramId, valueId, tvName, listener);
        } else {
            String updateTime = storage.getTime(paramId);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date savedDate = format.parse(updateTime);
                Date lastDate = format.parse(lastUpdate);
                if (savedDate.equals(lastDate)) {
                    Log.d("Param Storage", paramId + " saved instanse is up-to-date");
                    DialogBuilder.createRodSettingsSelectDialog(context, inflater, settingName, "Выберите значение",
                            storage.getParams(paramId), valueId, listener);
                } else if (savedDate.before(lastDate)) {
                    Log.d("Param Storage", paramId + " saved instanse is out-of-date");
                    getNewParamDict(storage, settingName, lastUpdate, paramId, valueId, tvName, listener);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void getNewParamDict(RodsSettingsStorage storage, String settingName, String date, String paramId, String valueId, TextView tvName, RodsSettingsParamSwithcListener listener) {
        RequestHelper requestHelper = new RequestHelper(context);
        progressBar.show();
        requestHelper.executeGet("rodparams", new String[]{"params", "rodType"},
                new String[]{paramId, rodType}, new RequestListener() {
                    @Override
                    public void onComplete(JSONObject json) {
                        progressBar.hide();
                        try {
                            storage.saveParam(paramId, json.getJSONArray("params").getJSONObject(0).getJSONArray("values").toString(), date);
                            DialogBuilder.createRodSettingsSelectDialog(context, inflater, settingName, "Выберите значение",
                                    json.getJSONArray("params").getJSONObject(0).getJSONArray("values"), valueId, listener);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(int responseCode) {
                        progressBar.hide();
                        DialogBuilder.createDefaultDialog(context, inflater, "Невозможно получить список значений", null);
                    }
                });
    }

    public JSONObject getRodPositionJson() throws JSONException {
        JSONObject rod = new JSONObject();
        rod.put("id", rodId);
        rod.put("landmark", landmarkObj.getString("value"));
        rod.put("distance", distanceObj.getString("value"));
        return rod;
    }
}
