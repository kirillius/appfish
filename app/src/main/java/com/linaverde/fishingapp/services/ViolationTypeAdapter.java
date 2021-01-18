package com.linaverde.fishingapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.ViolationDictionaryItem;

public class ViolationTypeAdapter extends ArrayAdapter<ViolationDictionaryItem> {

    private Context context;
    private ViolationDictionaryItem[] dict;
    private int selected;

    public ViolationTypeAdapter(Context context, ViolationDictionaryItem[] dict, int selected) {
        super(context, R.layout.list_dialog_item, dict);
        this.context = context;
        this.dict = dict;
        this.selected = selected;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_dialog_item, parent, false);

        ViolationDictionaryItem item = dict[pos];

        TextView type = rowView.findViewById(R.id.tv_spinner_item);
        ImageView dot = rowView.findViewById(R.id.iv_spinner_item);

        type.setText(item.getName());

        if (selected == pos){
            dot.setImageResource(R.drawable.spinner_item_selected);
        }

        return rowView;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getSelected() {
        return selected;
    }

    @Nullable
    @Override
    public ViolationDictionaryItem getItem(int position) {
        return dict[position];
    }

}