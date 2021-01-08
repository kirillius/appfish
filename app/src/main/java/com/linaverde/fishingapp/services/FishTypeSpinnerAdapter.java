package com.linaverde.fishingapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.Fish;
import com.linaverde.fishingapp.models.FishDictionaryItem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FishTypeSpinnerAdapter extends ArrayAdapter<FishDictionaryItem> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<FishDictionaryItem> values;
    private LayoutInflater inflater;

    public FishTypeSpinnerAdapter(@NonNull Context context, int resource, LayoutInflater inflater, FishDictionaryItem[] values) {
        super(context, resource);
        this.context = context;
        List<FishDictionaryItem> list = Arrays.asList(values);
        Collections.sort(list);
        this.values = list;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public FishDictionaryItem getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {
        View row = inflater.inflate(R.layout.fish_spinner_item, parent, false);
        TextView fishName = (TextView) row.findViewById(R.id.tv_spinner_item);
        fishName.setText(values.get(position).getName());

//        ImageView icon = (ImageView) row.findViewById(R.id.icon);
//
//        if (dayOfWeek[position] == "Котопятница"
//                || dayOfWeek[position] == "Субкота") {
//            icon.setImageResource(R.drawable.paw_on);
//        } else {
//            icon.setImageResource(R.drawable.ic_launcher);
//        }
        return row;
    }
}
