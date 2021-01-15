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
import com.linaverde.fishingapp.interfaces.ViolationListChangeListener;
import com.linaverde.fishingapp.models.Fish;
import com.linaverde.fishingapp.models.Violation;
import com.linaverde.fishingapp.models.ViolationDictionaryItem;

import java.util.Collections;
import java.util.List;

public class ViolationAdapter extends ArrayAdapter<Violation> {

    Context context;
    List<Violation> values;
    ViolationDictionaryItem[] dict;
    ViolationListChangeListener clickListener;
    int changedId = -1;
    boolean newViolationAdded = false;

    public ViolationAdapter(Context context, List<Violation> values, ViolationDictionaryItem[] dict, ViolationListChangeListener clickListener) {
        super(context, R.layout.violation_list_item, values);
        this.context = context;
        this.values = values;
        //Collections.sort(values);
        this.dict = dict;
        this.clickListener = clickListener;
    }

    public void setChangedId(int id) {
        this.changedId = id;
    }
    public void setNewViolationAdded(){
        newViolationAdded = true;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.violation_list_item, parent, false);
        TextView number = (TextView) rowView.findViewById(R.id.tv_violation_number);
        TextView time = (TextView) rowView.findViewById(R.id.tv_violation_time);
        Spinner name = (Spinner) rowView.findViewById(R.id.spinner_violation_name);

        Violation violation = values.get(pos);
        number.setText(Integer.toString(pos + 1) + ".");
        time.setText(violation.getTime());


        ViolationSpinnerAdapter adapter = new ViolationSpinnerAdapter((Activity) context, R.layout.spinner_item, R.id.tv_spinner_item, dict);
        name.setAdapter(adapter);

        String foulId = violation.getViolationId();
        int selectionId = 0;
        for (int i = 0; i < dict.length; i++) {
            if (dict[i].getId().equals(foulId)) {
                name.setSelection(i, false);
                selectionId = i;
                break;
            }
        }

        if (changedId != -1 && changedId != pos){
            name.setEnabled(false);
        }

        int finalSelectionId = selectionId;

        name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (changedId == -1 || changedId == pos) {
                    if (position != finalSelectionId) {
                        clickListener.selectionChanged(pos, dict[position].getId());
                    }
                } else {
                    name.setSelection(finalSelectionId, false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clickListener.fishTimeClicked(fish.getDate(), fish.getTime());
                if (changedId == pos && pos == values.size()-1 && newViolationAdded) {
                    DialogBuilder.createTimeInputDialog(context, inflater, context.getString(R.string.input_new_time), violation.getTime(),
                            new CompleteActionListener() {
                                @Override
                                public void onOk(String input) {
                                    if (!input.equals(violation.getTime())) {
                                        time.setText(input);
                                        changedId = pos;
                                        clickListener.violationTimeChanged(pos, violation.getDate(), input);
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
