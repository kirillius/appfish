package com.linaverde.fishingapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.Fish;
import com.linaverde.fishingapp.models.FishDictionaryItem;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class DistanceAdapter extends ArrayAdapter<Double> {

    Context context;
    List<Double> values;

    public DistanceAdapter(Context context, List<Double> values) {
        super(context, R.layout.map_distance_item, values);
        this.context = context;
        this.values = values;
    }


    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.map_distance_item, parent, false);

        ((TextView) rowView).setText(Double.toString(values.get(pos)));

        return rowView;
    }
}
