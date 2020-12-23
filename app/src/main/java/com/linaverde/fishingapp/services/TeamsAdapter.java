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

public class TeamsAdapter extends ArrayAdapter<JSONObject> {

    Context context;
    JSONObject [] values;

    public TeamsAdapter(Context context, JSONObject [] values) {
        super(context, R.layout.statistic_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.teams_list_item, parent, false);
        TextView number = (TextView) rowView.findViewById(R.id.tv_team_number);
        TextView name = (TextView) rowView.findViewById(R.id.tv_team_name);

        JSONObject team = values[pos];

        try {
            number.setText(Integer.toString(pos+1) + "." );
            name.setText(team.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rowView;
    }
}