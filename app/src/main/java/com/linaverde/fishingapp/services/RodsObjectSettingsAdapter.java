package com.linaverde.fishingapp.services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RodsObjectSettingsAdapter extends ArrayAdapter<JSONObject> {

    private Context context;
    private JSONObject[] dict;
    private int selected;

    public RodsObjectSettingsAdapter(Context context, JSONObject[] dict, int selected) {
        super(context, R.layout.list_dialog_item, dict);
        this.context = context;
        this.dict = dict;
        this.selected = selected;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_dialog_item, parent, false);

        try {
            JSONObject item = dict[pos];
            TextView type = rowView.findViewById(R.id.tv_spinner_item);
            ImageView dot = rowView.findViewById(R.id.iv_spinner_item);
            type.setText(item.getString("value"));
            if (selected == pos) {
                dot.setImageResource(R.drawable.spinner_item_selected);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rowView;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getSelected() {
        return selected;
    }

    @Override
    public JSONObject getItem(int position) {
        JSONObject object = new JSONObject();
        try {
            object.put("id", dict[position].getString("id"));
            object.put("name", dict[position].getString("value"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
