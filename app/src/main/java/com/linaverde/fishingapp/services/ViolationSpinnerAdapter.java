package com.linaverde.fishingapp.services;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.FishDictionaryItem;
import com.linaverde.fishingapp.models.ViolationDictionaryItem;

public class ViolationSpinnerAdapter extends ArrayAdapter<ViolationDictionaryItem> implements SpinnerAdapter {

    private Activity context;
    ViolationDictionaryItem[] data;

    public ViolationSpinnerAdapter(Activity context, int layoutId, int textViewId, ViolationDictionaryItem[] data) {
        super(context, layoutId, textViewId, data);
        this.context = context;
        this.data = data;
    }
    
    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view =  View.inflate(context, R.layout.spinner_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_spinner_item);
        textView.setText(data[position].getName());
        return textView;
    }


    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        ViolationDictionaryItem item = data[position];

        if (item != null) { // парсим данные с каждого объекта
            ImageView image = (ImageView) row.findViewById(R.id.iv_spinner_item);
            TextView type = (TextView) row.findViewById(R.id.tv_spinner_item);
//            if (myFlag != null) {
//                myFlag.setBackground(context.getResources().getDrawable(item.getCountryFlag(), context.getTheme()));
//            }
            if (type != null)
                type.setText(item.getName());
        }

        return row;
    }

    @Override
    public ViolationDictionaryItem getItem(int position) {
        return data[position];
    }
}
