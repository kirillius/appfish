package com.linaverde.fishingapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.Team;

import org.json.JSONException;
import org.json.JSONObject;

public class TeamsAdapter extends ArrayAdapter<Team> {

    Context context;
    Team[] values;

    public TeamsAdapter(Context context, Team[] values) {
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

        Team team = values[pos];

        number.setText(Integer.toString(pos + 1) + ".");
        name.setText(team.getName());

        return rowView;
    }

    @Nullable
    @Override
    public Team getItem(int position) {
        return values[position];
    }
}