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
    FishListChangeActionListener clickListener;
    int changedId = -1;
    boolean newFishAdded = false;

    public FishAdapter(Context context, List<Fish> values, FishDictionaryItem[] dict, FishListChangeActionListener clickListener) {
        super(context, R.layout.teams_list_item, values);
        this.context = context;
        this.values = values;
        Collections.sort(values);
        this.dict = dict;
        this.clickListener = clickListener;
    }

    public void setChangedId(int id) {
        this.changedId = id;
    }

    public void setNewFishAdded() {
        newFishAdded = true;
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

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changedId == -1 || changedId == pos) {
                    DialogBuilder.createSelectFishTypeDialog(context, inflater, "Выберите тип рыбы", dict, fish.getFishId(), new CompleteActionListener() {
                        @Override
                        public void onOk(String input) {
                            for (FishDictionaryItem fishDictionaryItem : dict) {
                                if (input.equals(fishDictionaryItem.getId())) {
                                    type.setText(fishDictionaryItem.getName());
                                }
                            }
                            clickListener.selectionChanged(pos, input);
                            changedId = pos;
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clickListener.fishTimeClicked(fish.getDate(), fish.getTime());
                if (changedId == pos && pos == values.size() - 1 && newFishAdded) {
                    DialogBuilder.createTimeInputDialog(context, inflater, context.getString(R.string.input_new_time), fish.getTime(),
                            new CompleteActionListener() {
                                @Override
                                public void onOk(String input) {
                                    if (!input.equals(fish.getTime())) {
                                        time.setText(input);
                                        changedId = pos;
                                        clickListener.fishTimeChanged(pos, fish.getDate(), input);
                                    }
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                }
            }
        });

        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clickListener.fishWeighClicked(fish.getWeight());
                if (changedId == -1 || changedId == pos) {
                    int currWeight = fish.getWeight();
                    DialogBuilder.createWeightInputDialog(context, inflater, context.getString(R.string.input_new_weight), Integer.toString(currWeight),
                            new CompleteActionListener() {
                                @Override
                                public void onOk(String input) {
                                    if (!Integer.toString(currWeight).equals(input) && !input.equals("")) {
                                        changedId = pos;
                                        weight.setText(input);
                                        clickListener.fishWeighChanged(pos, Integer.parseInt(input));
                                    }
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                }
            }
        });

        return rowView;
    }

}
