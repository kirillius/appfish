package com.linaverde.fishingapp.services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.MapMark;

import java.util.List;

public class MapAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater inflater;

    List<MapMark> marks;


    public MapAdapter(Context context, LayoutInflater inflater, List<MapMark> marks) {
        this.context = context;
        this.inflater = inflater;
        this.marks = marks;
    }

    @Override
    public int getCount() {
        return marks.size();
    }

    @Override
    public Object getItem(int position) {
        return marks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (marks.get(position).isInfo()){
            rowView = inflater.inflate(R.layout.map_distance_item, parent, false);
            ((TextView) rowView).setText(Double.toString(marks.get(position).getDistance()));
        } else {
            rowView = inflater.inflate(R.layout.map_grid_item, parent, false);
            int rodId = marks.get(position).getRodId();
            Log.d("rodId", Integer.toString(rodId));
            switch (rodId){
                case 1:
                    ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_1));
                    break;
                case 2:
                    ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_2));
                    break;
                case 3:
                    ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_3));
                    break;
                case 4:
                    ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_4));
                    break;

            }
        }


        return rowView;
    }
}