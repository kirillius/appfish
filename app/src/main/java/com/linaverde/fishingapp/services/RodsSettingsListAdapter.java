package com.linaverde.fishingapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.linaverde.fishingapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RodsSettingsListAdapter extends ArrayAdapter<JSONObject> {

    Context context;
    List<JSONObject> values;

    public RodsSettingsListAdapter(Context context, List<JSONObject> values) {
        super(context, R.layout.rods_settings_list_item, values);
        this.context = context;
        this.values = values;
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
            if (value.getClass() == JSONObject.class){
                tvValue.setText(((JSONObject) value).getString("name"));
            } else {
                tvValue.setText(value.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rowView;
    }
}
