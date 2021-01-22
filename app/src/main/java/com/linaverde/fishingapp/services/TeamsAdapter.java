package com.linaverde.fishingapp.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.models.Team;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TeamsAdapter extends ArrayAdapter<Team> {

    Context context;
    List<Team> values;

    public TeamsAdapter(Context context, Team[] values) {
        super(context, R.layout.teams_list_item, values);
        this.context = context;
        List<Team> list = Arrays.asList(values);
        Collections.sort(list);
        this.values = list;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.teams_list_item, parent, false);
        TextView number = (TextView) rowView.findViewById(R.id.tv_team_number);
        TextView name = (TextView) rowView.findViewById(R.id.tv_team_name);
        ImageView checkIn = (ImageView) rowView.findViewById(R.id.iv_team_checkin);
        PorterShapeImageView logo = (PorterShapeImageView) rowView.findViewById(R.id.iv_team_logo);

        Team team = values.get(pos);

        number.setText(Integer.toString(pos + 1) + ".");
        name.setText(team.getName());
        String logoString = team.getLogo();
        if (logoString != null && !logoString.equals("null") && !logoString.equals("")) {
            logo.setImageBitmap(ImageHelper.decodeToImage(logoString));
        }

        if (team.getCheckIn()){
            checkIn.setImageDrawable(context.getDrawable(R.drawable.team_check));
        }

        return rowView;
    }

    @Nullable
    @Override
    public Team getItem(int position) {
        return values.get(position);
    }
}