package com.linaverde.fishingapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.Rod;

import java.util.List;

public class RodsAdapter extends BaseAdapter {

    private Context context;
    private List<Rod> rods;
    private List<String> values;
    private LayoutInflater inflater;

    public RodsAdapter(Context context, LayoutInflater inflater, List<Rod> rods, List<String> values) {
        this.context = context;
        this.inflater = inflater;
        this.rods = rods;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return rods;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.grid_item, parent, false);

        TextView tv = rowView.findViewById(R.id.tv_grid_item);
        tv.setText(values.get(position));
        tv.setId(position);
        if(position < 5 || position > 54){
            tv.setBackground(context.getDrawable(R.drawable.dark_gradient));
        }
        if (position == 0) {
            rowView.setBackground(context.getDrawable(R.drawable.grid_item_top_left));
        } else if (position == 4) {
            rowView.setBackground(context.getDrawable(R.drawable.grid_item_top_right));
        } else if (position == 55) {
            rowView.setBackground(context.getDrawable(R.drawable.grid_item_bottom_left));
        } else if (position == 59){
            rowView.setBackground(context.getDrawable(R.drawable.grid_item_bottom_right));
        }
        return rowView;
    }
}