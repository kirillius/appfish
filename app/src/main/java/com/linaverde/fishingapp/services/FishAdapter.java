package com.linaverde.fishingapp.services;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.Fish;
import com.linaverde.fishingapp.models.FishDictionaryItem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FishAdapter extends ArrayAdapter<Fish> {

    Context context;
    List<Fish> values;
    FishDictionaryItem[] dict;

    public FishAdapter(Context context, Fish[] values, FishDictionaryItem[] dict) {
        super(context, R.layout.teams_list_item, values);
        this.context = context;
        List<Fish> list = Arrays.asList(values);
        Collections.sort(list);
        this.values = list;
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
        Spinner type = (Spinner) rowView.findViewById(R.id.sp_fish_type);

        Fish fish = values.get(pos);
        number.setText(Integer.toString(pos + 1) + ".");
        time.setText(fish.getTime());
        weight.setText(Integer.toString(fish.getWeight()));

        FishTypeSpinnerAdapter adapter = new FishTypeSpinnerAdapter((Activity) context, R.layout.fish_spinner_item, R.id.tv_spinner_item, dict);
        type.setAdapter(adapter);

        String caughtId = fish.getId();
        for(int i = 0; i < dict.length; i++){
            if (dict[i].getId().equals(caughtId)){
                type.setSelection(i, false);
                break;
            }
        }
        return rowView;
    }

}
