package com.linaverde.fishingapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.Rod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RodsMainSettingsGridAdapter extends BaseAdapter {

    private Context context;

    private JSONArray values;
    private LayoutInflater inflater;
    private int rodID;
    private boolean cast;

    public RodsMainSettingsGridAdapter(Context context, LayoutInflater inflater, JSONObject values) {
        this.context = context;
        this.inflater = inflater;
        try {
            this.rodID = values.getInt("rodId");
            this.values = values.getJSONArray("settings");
            this.cast = values.getBoolean("cast");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return values.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return values.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.rods_main_settings_grid_item,
                parent, false);

        TextView tvName = rowView.findViewById(R.id.grid_param_name);
        TextView tvValue = rowView.findViewById(R.id.grid_param_value);
        try {
            JSONObject object = values.getJSONObject(position);
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

    public int getRodID(){
        return rodID;
    }

    public boolean isCast() {
        return cast;
    }
}
