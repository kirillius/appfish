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

public class DetailedStatisticAdapter extends ArrayAdapter<JSONObject> {

    Context context;
    JSONObject [] values;


    public DetailedStatisticAdapter(Context context, JSONObject [] values) {
        super(context, R.layout.detailed_stats_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.detailed_stats_item, parent, false);
        TextView place = (TextView) rowView.findViewById(R.id.tv_stat_place);
        TextView date = (TextView) rowView.findViewById(R.id.tv_stat_date);
        TextView time = (TextView) rowView.findViewById(R.id.tv_stat_time);
        TextView fish = (TextView) rowView.findViewById(R.id.tv_stat_fish);
        TextView weight = (TextView) rowView.findViewById(R.id.tv_stat_weight);

        JSONObject stat = values[pos];

        try {
            place.setText(Integer.toString(pos+1)+ ".");
            String stime = stat.getString("date");
            String [] sdate = stime.substring(0, stime.indexOf("T")).split("-");
            stime = stime.substring(stime.indexOf("T") + 1);
            date.setText(sdate[2]+"-"+sdate[1]+"-"+sdate[0]);
            time.setText(stime);
            fish.setText(stat.getString("fish"));
            weight.setText(Integer.toString(stat.getInt("weight")));
            int mark = stat.getInt("mark");
            switch (mark){
                case 1:
                    rowView.setBackground(context.getDrawable(R.drawable.stat_back_red));
                    break;
                case 2:
                    rowView.setBackground(context.getDrawable(R.drawable.stat_back_green));
                    break;
                case 3:
                    rowView.setBackground(context.getDrawable(R.drawable.stat_back_yellow));
                    break;
                case 4:
                    rowView.setBackground(context.getDrawable(R.drawable.stats_multicolor));
                    break;
                case 5:
                    rowView.setBackground(context.getDrawable(R.drawable.stats_red_green));
                    break;
                case 6:
                    rowView.setBackground(context.getDrawable(R.drawable.stats_red_yellow));
                    break;
                case 7:
                    rowView.setBackground(context.getDrawable(R.drawable.stats_yellow_green));
                    break;
                default:
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rowView;
    }

    public String getTeamId(int pos){
        JSONObject stat = values[pos];
        String teamId = "";
        try {
            teamId = stat.getString("teamId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return teamId;
    }
}
