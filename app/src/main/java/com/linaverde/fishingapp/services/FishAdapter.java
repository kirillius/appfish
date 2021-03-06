package com.linaverde.fishingapp.services;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.FishListChangeActionListener;
import com.linaverde.fishingapp.models.Fish;
import com.linaverde.fishingapp.models.FishDictionaryItem;

import java.util.Collections;
import java.util.List;

public class FishAdapter extends ArrayAdapter<Fish> {

    Context context;
    List<Fish> values;
    FishDictionaryItem[] dict;

    public FishAdapter(Context context, List<Fish> values, FishDictionaryItem[] dict) {
        super(context, R.layout.teams_list_item, values);
        this.context = context;
        this.values = values;
        Collections.sort(values);
        this.dict = dict;
    }


    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fish_list_item, parent, false);
        TextView number = (TextView) rowView.findViewById(R.id.tv_fish_number);
        TextView time = (TextView) rowView.findViewById(R.id.tv_fish_time);
        TextView weight = (TextView) rowView.findViewById(R.id.tv_fish_weight);
        TextView type = (TextView) rowView.findViewById(R.id.tv_fish_type);

        Fish fish = values.get(pos);
        number.setText(Integer.toString(pos + 1) + ".");
        time.setText(fish.getTime());
        weight.setText(Integer.toString(fish.getWeight()));

        for (FishDictionaryItem fishDictionaryItem : dict) {
            if (fish.getFishId().equals(fishDictionaryItem.getId())) {
                type.setText(fishDictionaryItem.getName());
            }
        }

        return rowView;
    }

    public String getId(int pos){
        return values.get(pos).getId();
    }

}
