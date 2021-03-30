package com.linaverde.fishingapp.services;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.models.MapMark;

import java.util.ArrayList;
import java.util.List;

public class MapAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater inflater;

    private List<MapMark> marks;

    private List<MapMark> rodsMarks;

    private boolean editable;

    private int cellHeight;

    public MapAdapter(Context context, LayoutInflater inflater, List<MapMark> marks, int cellHeight, boolean editable) {
        this.context = context;
        this.inflater = inflater;
        this.marks = marks;
        this.editable = editable;
        rodsMarks = new ArrayList<>();
        this.cellHeight = cellHeight;
    }

    @Override
    public int getCount() {
        return marks.size();
    }

    @Override
    public Object getItem(int position) {
        return marks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (marks.get(position).isInfo()) {
            rowView = inflater.inflate(R.layout.map_distance_item, parent, false);
            ((TextView) rowView).setText(Double.toString(marks.get(position).getDistance()));
        } else {
            rowView = inflater.inflate(R.layout.map_grid_item, parent, false);
            int rodId = marks.get(position).getRodId();
            Log.d("rodId", Integer.toString(rodId));
            switch (rodId) {
                case 1:
                    ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_1));
                    rodsMarks.add(marks.get(position));
                    break;
                case 2:
                    ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_2));
                    rodsMarks.add(marks.get(position));
                    break;
                case 3:
                    ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_3));
                    rodsMarks.add(marks.get(position));
                    break;
                case 4:
                    ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_4));
                    rodsMarks.add(marks.get(position));
                    break;
            }

            if (editable) {
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selected;
                        if (rodId == 0){
                            selected = -1;
                        } else {
                            selected = rodId-1;
                        }
                        DialogBuilder.createSelectRodOnMapDialog(context, inflater, "Выберите удочку", selected, new CompleteActionListener() {
                            @Override
                            public void onOk(String input) {
                                Log.d("New mark", input);
                                for(int i = 0; i < rodsMarks.size(); i++){
                                    if (rodsMarks.get(i).getRodId() == Integer.parseInt(input)){
                                        rodsMarks.get(i).setRodId(0);
                                        rodsMarks.remove(i);
                                        rodsMarks.add(marks.get(position));
                                        break;
                                    }
                                }
                                marks.get(position).setRodId(Integer.parseInt(input));
                                MapAdapter.this.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                });
            }
        }
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cellHeight, context.getResources().getDisplayMetrics());
        rowView.getLayoutParams().height = dimensionInDp;
        rowView.requestLayout();

        return rowView;
    }
}