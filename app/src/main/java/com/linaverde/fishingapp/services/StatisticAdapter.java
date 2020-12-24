package com.linaverde.fishingapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.linaverde.fishingapp.R;

import org.json.JSONException;
import org.json.JSONObject;


public class StatisticAdapter extends ArrayAdapter<JSONObject> {

    Context context;
    JSONObject [] values;

    public StatisticAdapter(Context context, JSONObject [] values) {
        super(context, R.layout.statistic_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.statistic_list_item, parent, false);
        TextView place = (TextView) rowView.findViewById(R.id.tv_stat_place);
        TextView sector = (TextView) rowView.findViewById(R.id.tv_stat_sector_name);
        TextView count = (TextView) rowView.findViewById(R.id.tv_stat_count);
        TextView avr = (TextView) rowView.findViewById(R.id.tv_stat_avr);
        TextView sum = (TextView) rowView.findViewById(R.id.tv_stat_sum);

        JSONObject stat = values[pos];

        try {
            place.setText(Integer.toString(stat.getInt("place")));
            sector.setText(stat.getString("teamName"));
            count.setText(Integer.toString(stat.getInt("quantity")));
            avr.setText(Integer.toString(stat.getInt("avgWeight")));
            sum.setText(Integer.toString(stat.getInt("maxWeight")));
            if (stat.getInt("mark") == 1){
                rowView.setBackground(context.getDrawable(R.drawable.stat_back_red));
            } else if (stat.getInt("mark") == 2) {
                rowView.setBackground(context.getDrawable(R.drawable.stat_back_green));
            } else if (stat.getInt("mark") == 3) {
                rowView.setBackground(context.getDrawable(R.drawable.stat_back_yellow));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rowView;
    }
}
