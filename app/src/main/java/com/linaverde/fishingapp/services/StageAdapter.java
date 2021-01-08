package com.linaverde.fishingapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.QueueComparator;
import com.linaverde.fishingapp.models.Stage;
import com.linaverde.fishingapp.models.TeamsQueue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class StageAdapter extends ArrayAdapter<Stage> {
    Context context;
    List<Stage> values;

    public StageAdapter (Context context, Stage[] values) {
        super(context, R.layout.stages_list_item, values);
        this.context = context;
        //Collections.sort(list);
        this.values = Arrays.asList(values);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.stages_list_item, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.tv_stage_name);
        TextView status = (TextView) rowView.findViewById(R.id.tv_stage_status);

        name.setText(values.get(pos).getName());
        if (values.get(pos).getStatus() == 0){
            status.setText(context.getString(R.string.stage_continue));
        } else {
            status.setText(context.getString(R.string.stage_end));
        }

        return rowView;
    }

    @Nullable
    @Override
    public Stage getItem(int position) {
        return values.get(position);
    }
}
