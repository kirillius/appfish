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
import com.linaverde.fishingapp.models.Team;
import com.linaverde.fishingapp.models.TeamsQueue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QueueAdapter extends ArrayAdapter<TeamsQueue> {
    Context context;
    List<TeamsQueue> values;

    public QueueAdapter (Context context, TeamsQueue[] values) {
        super(context, R.layout.teams_list_item, values);
        this.context = context;
        this.values = Arrays.asList(values);
        Comparator<TeamsQueue> comparator = new QueueComparator();
        this.values.sort(comparator);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.teams_list_item, parent, false);
        TextView number = (TextView) rowView.findViewById(R.id.tv_team_number);
        TextView name = (TextView) rowView.findViewById(R.id.tv_team_name);
        PorterShapeImageView logo = (PorterShapeImageView) rowView.findViewById(R.id.iv_team_logo);

        TeamsQueue team = values.get(pos);

        //number.setText(Integer.toString(pos + 1) + ".");
        name.setText(team.getTeamName());
        if (team.getQueue() != 0)
            number.setText(Integer.toString(team.getQueue()));
        String logoString = team.getLogo();
        if (logoString != null && !logoString.equals("null") && !logoString.equals("")) {
            logo.setImageBitmap(ImageHelper.decodeToImage(logoString));
        }

        return rowView;
    }

    @Nullable
    @Override
    public TeamsQueue getItem(int position) {
        return values.get(position);
    }
}
