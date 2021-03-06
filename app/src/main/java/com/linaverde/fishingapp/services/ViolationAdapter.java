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

import androidx.annotation.Nullable;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.ViolationListChangeListener;
import com.linaverde.fishingapp.models.Fish;
import com.linaverde.fishingapp.models.FishDictionaryItem;
import com.linaverde.fishingapp.models.Violation;
import com.linaverde.fishingapp.models.ViolationDictionaryItem;

import java.util.Collections;
import java.util.List;

public class ViolationAdapter extends ArrayAdapter<Violation> {

    Context context;
    List<Violation> values;
    ViolationDictionaryItem[] dict;
    int changedId = -1;
    boolean newViolationAdded = false;
    boolean edit;

    public ViolationAdapter(Context context, List<Violation> values, ViolationDictionaryItem[] dict, boolean edit) {
        super(context, R.layout.violation_list_item, values);
        this.context = context;
        this.values = values;
        //Collections.sort(values);
        this.dict = dict;
        this.edit = edit;
    }

    public void setChangedId(int id) {
        this.changedId = id;
    }

    public void setNewViolationAdded() {
        newViolationAdded = true;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.violation_list_item, parent, false);
        TextView number = (TextView) rowView.findViewById(R.id.tv_violation_number);
        TextView time = (TextView) rowView.findViewById(R.id.tv_violation_time);
        TextView name = (TextView) rowView.findViewById(R.id.tv_violation_name);

        Violation violation = values.get(pos);
        number.setText(Integer.toString(pos + 1) + ".");
        time.setText(violation.getTime());

        for (ViolationDictionaryItem violationDictionaryItem : dict) {
            if (violation.getViolationId().equals(violationDictionaryItem.getId())) {
                name.setText(violationDictionaryItem.getName());
            }
        }
        return rowView;
    }
}
