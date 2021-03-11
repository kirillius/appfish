package com.linaverde.fishingapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.RodsSettingsChangeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RodsSettingsListAdapter extends ArrayAdapter<JSONObject> {

    Context context;
    List<JSONObject> values;
    RodsSettingsChangeListener changeListener;

    public RodsSettingsListAdapter(Context context, List<JSONObject> values, RodsSettingsChangeListener changeListener) {
        super(context, R.layout.rods_settings_list_item, values);
        this.context = context;
        this.values = values;
        this.changeListener = changeListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rods_settings_list_item,
                parent, false);

        TextView tvName = rowView.findViewById(R.id.grid_param_name);
        TextView tvValue = rowView.findViewById(R.id.grid_param_value);
        try {
            JSONObject object = values.get(position);
            tvName.setText(object.getJSONObject("param").getString("name"));
            Object value = object.get("value");
            if (value.getClass() == JSONObject.class) {
                tvValue.setText(((JSONObject) value).getString("name"));
            } else {
                tvValue.setText(value.toString());
            }


            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type = null;
                    CompleteActionListener listener = new CompleteActionListener() {
                        @Override
                        public void onOk(String input) {
                            if (!input.isEmpty()) {
                                try {
                                    if (value.getClass() != JSONObject.class){
                                        values.get(position).remove("value");
                                        values.get(position).put("value", input);
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
                        switch (type) {
                            case "number":
                                DialogBuilder.createInputNumberDialog(getContext(), inflater,
                                        "Введите числовое значение", listener);
                                break;
                            case "dateTime":
                                DialogBuilder.createTimeInputDialog(getContext(), inflater,
                                        "Введите время", null, listener);
                                break;
                            case "string":
                                DialogBuilder.createInputStringDialog(getContext(), inflater,
                                        "Введите значение", listener);
                                break;
                            case "object":
                                //вызов списка
                                break;
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
}
