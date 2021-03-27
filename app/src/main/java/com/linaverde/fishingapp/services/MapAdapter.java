package com.linaverde.fishingapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
            ((TextView) rowView).setText(Double.toString(marks.get(position).getValue()));
        } else {
            rowView = inflater.inflate(R.layout.map_grid_item, parent, false);
        }


        return rowView;
    }
}