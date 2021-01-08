package com.linaverde.fishingapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.Fish;
import com.linaverde.fishingapp.models.Team;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FishAdapter extends ArrayAdapter<Fish> {

    Context context;
    List<Fish> values;

    public FishAdapter(Context context, Fish[] values) {
        super(context, R.layout.teams_list_item, values);
        this.context = context;
        List<Fish> list = Arrays.asList(values);
        Collections.sort(list);
        this.values = list;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fish_list_item, parent, false);
        TextView number = (TextView) rowView.findViewById(R.id.tv_fish_number);
        TextView time = (TextView) rowView.findViewById(R.id.tv_fish_time);
        Spinner type = (Spinner) rowView.findViewById(R.id.sp_fish_type);
        TextView weigh = (TextView) rowView.findViewById(R.id.tv_fish_weight);

        Fish fish = values.get(pos);

        number.setText(Integer.toString(pos + 1) + ".");
        time.setText(Long.toString(fish.getTime().getTime()));


        return rowView;
    }

}
