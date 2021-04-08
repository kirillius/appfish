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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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

    public RodsSettingsListAdapter(Context context, List<JSONObject> values,
                                   ContentLoadingProgressBar progressBar, int rodId, String rodType, RodsSettingsChangeListener changeListener) {
        super(context, R.layout.rods_settings_list_item, values);
        this.context = context;
        this.values = values;
        this.changeListener = changeListener;
        this.progressBar = progressBar;
        this.rodId = rodId;
        this.rodType = rodType;
        userInfo = new UserInfo(context);

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
        View rowView = inflater.inflate(R.layout.rods_settings_list_item,
                parent, false);
        String paramId = null;
        String valueId = null;
        TextView tvName = rowView.findViewById(R.id.grid_param_name);
        TextView tvValue = rowView.findViewById(R.id.grid_param_value);
        try {
            JSONObject object = values.get(position);
            paramId = object.getJSONObject("param").getString("id");
            if (paramId.equals("LANDMARK"))
                landmarkObj = object;
            if (paramId.equals("DISTANCE"))
                distanceObj = object;
            tvName.setText(object.getJSONObject("param").getString("name"));
            Object value = object.get("value");
            if (value.getClass() == JSONObject.class) {
                tvValue.setText(((JSONObject) value).getString("name"));
                valueId = ((JSONObject) value).getString("id");
            } else {
                tvValue.setText(value.toString());
            }


            String finalParamId = paramId;
            String finalValueId = valueId;
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type = null;
                    String id = null;
                    CompleteActionListener listener = new CompleteActionListener() {
                        @Override
                        public void onOk(String input) {
                            if (!input.isEmpty()) {
                                try {
                                    if (value.getClass() != JSONObject.class) {
                                        values.get(position).remove("value");
                                        values.get(position).put("value", input);
                                    } else {
                                        values.get(position).remove("value");
                                        values.get(position).put("value", new JSONObject(input));
                                    }
                                    changeListener.paramChanged(object.getJSONObject("param").getString("id"),
                                            input);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onCancel() {

                        }
                    };

                    try {
                        type = object.getJSONObject("param").getString("type");
                        id = object.getJSONObject("param").getString("id");
                        if (id.equals("DISTANCE") || id.equals("LANDMARK")) {
                            RodPositionChangedListener positionListener = new RodPositionChangedListener() {
                                @Override
                                public void rodPositionChanged(int rodId, String landmark, double distance) {
                                    try {
                                        distanceObj.remove("value");
                                        distanceObj.put("value",  Double.toString(distance));
                                        landmarkObj.remove("value");
                                        landmarkObj.put("value", landmark);
                                        changeListener.paramChanged("LANDMARK", landmark);
                                        changeListener.paramChanged("DISTANCE", Double.toString(distance));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            Log.d("Rodid to map", Integer.toString(rodId));
                            changeListener.openMap(positionListener);
                        } else {
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
                                    createTypeSelect(finalParamId, finalValueId, tvName, listener);
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rowView;
    }


    private void createTypeSelect(String paramId, String valueId, TextView tvName, CompleteActionListener listener) {
        RodsSettingsStorage storage = new RodsSettingsStorage(context);
        if (storage.getParams(paramId) == null) {
            RequestHelper requestHelper = new RequestHelper(context);
            progressBar.show();
            requestHelper.executeGet("rodparams", new String[]{"params", "rodType"},
                    new String[]{paramId, rodType}, new RequestListener() {
                        @Override
                        public void onComplete(JSONObject json) {
                            progressBar.hide();
                            try {
                                DialogBuilder.createRodSettingsSelectDialog(context, inflater, "Выберите значение",
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
    }

    public JSONObject getRodPositionJson() throws JSONException {
        JSONObject rod = new JSONObject();
        rod.put("id", rodId);
        rod.put("landmark", landmarkObj.getString("value"));
        rod.put("distance", distanceObj.getString("value"));
        return rod;
    }
}
