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
import com.linaverde.fishingapp.interfaces.WeightStageClickedListener;
import com.linaverde.fishingapp.models.Stage;

import java.util.Arrays;
import java.util.List;

public class StageAdapter extends ArrayAdapter<Stage> {
    Context context;
    List<Stage> values;
    WeightStageClickedListener listener;

    public StageAdapter (Context context, Stage[] values, WeightStageClickedListener listener) {
        super(context, R.layout.stages_list_item, values);
        this.context = context;
        //Collections.sort(list);
        this.values = Arrays.asList(values);
        this.listener = listener;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.stages_list_item, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.tv_stage_name);
        TextView status = (TextView) rowView.findViewById(R.id.tv_stage_status);
        ImageView stats = (ImageView) rowView.findViewById(R.id.iv_stages_stats);

        name.setText(values.get(pos).getName());
        if (values.get(pos).getStatus() == 0){
            status.setText(context.getString(R.string.stage_continue));
            stats.setImageDrawable(context.getDrawable(R.drawable.stages_stats_open));
        } else {
            status.setText(context.getString(R.string.stage_end));
            stats.setImageDrawable(context.getDrawable(R.drawable.stages_stats_closed));
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.stageClicked(values.get(pos).getId());
            }
        };

        name.setOnClickListener(clickListener);
        status.setOnClickListener(clickListener);

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.statsClicked(values.get(pos).getId());
            }
        });

        return rowView;
    }

    @Nullable
    @Override
    public Stage getItem(int position) {
        return values.get(position);
    }
}
