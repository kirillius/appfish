package com.linaverde.fishingapp.services;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.MapRodClickedListener;
import com.linaverde.fishingapp.models.MapMark;

import java.util.ArrayList;
import java.util.List;

public class MapAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater inflater;

    private List<MapMark> marks;

    private List<MapMark> rodsMarks;

    private final boolean editable;

    private final int cellHeight;

    private final int editableRod;

    MapRodClickedListener listener;

    private final boolean showSpod;

    int columnWidth;

    public MapAdapter(Context context, LayoutInflater inflater, List<MapMark> marks, int cellHeight,
                      int editableRod, boolean showSpod, MapRodClickedListener listener) {
        this.context = context;
        this.inflater = inflater;
        this.marks = marks;
        editable = editableRod > 0;
        rodsMarks = new ArrayList<>();
        this.cellHeight = cellHeight;
        this.editableRod = editableRod;
        this.listener = listener;
        this.showSpod = showSpod;
    }

    @Override
    public int getCount() {
        return marks.size();
    }

    @Override
    public MapMark getItem(int position) {
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
            int spodId = marks.get(position).getSpodId();
            int depth = marks.get(position).getDepth();

            if (!showSpod) {
                switch (rodId) {
                    case 1:
                        if (rodId != editableRod) {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_1_half));
                        } else {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_1_half_gr));
                        }
                        rodsMarks.add(marks.get(position));
                        break;
                    case 2:
                        if (rodId != editableRod) {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_2_half));
                        } else {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_2_half_gr));
                        }
                        rodsMarks.add(marks.get(position));
                        break;
                    case 3:
                        if (rodId != editableRod) {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_3_half));
                        } else {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_3_half_gr));
                        }
                        rodsMarks.add(marks.get(position));
                        break;
                    case 4:
                        if (rodId != editableRod) {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_4_half));
                        } else {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_4_half_gr));
                        }
                        rodsMarks.add(marks.get(position));
                        break;
                }
                if (rodId > 0 && !editable) {
                    ((TextView) rowView.findViewById(R.id.tv_mark)).setText(depth + " м");
                }
                if (spodId > 0) {
                    //Log.d("Spod place", "spod id drawing" + Integer.toString(spodId));
                    rowView.setBackground(context.getDrawable(R.drawable.spod_rod_back));
                }
                if (editable) {
                    rowView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (rodId <= 0) {
                                for (int i = 0; i < rodsMarks.size(); i++) {
                                    if (rodsMarks.get(i).getRodId() == editableRod) {
                                        rodsMarks.get(i).setRodId(0);
                                        rodsMarks.remove(i);
                                        break;
                                    }
                                }
                                rodsMarks.add(marks.get(position));
                                marks.get(position).setRodId(editableRod);
                                MapAdapter.this.notifyDataSetChanged();
                            }
                        }
                    });
                } else {
                    if (rodId > 0 && listener != null) {
                        rowView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.openSettingsList(rodId, getItem(position).isCast());
                            }
                        });

                        rowView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                String comment = getItem(position).getComment();
                                if (!comment.equals(" ") && !comment.equals("")) {
                                    Toast.makeText(context, comment, Toast.LENGTH_LONG).show();
                                    return true;
                                }
                                return false;
                            }
                        });
                    }
                }
            } else {
                switch (spodId) {
                    case 1:
                        if (spodId != editableRod) {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_1));
                        } else {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_1_gr));
                        }
                        rodsMarks.add(marks.get(position));
                        break;
                    case 2:
                        if (spodId != editableRod) {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_2));
                        } else {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_2_gr));
                        }
                        rodsMarks.add(marks.get(position));
                        break;
                    case 3:
                        if (spodId != editableRod) {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_3));
                        } else {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_3_gr));
                        }
                        rodsMarks.add(marks.get(position));
                        break;
                    case 4:
                        if (spodId != editableRod) {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_4));
                        } else {
                            ((ImageView) rowView.findViewById(R.id.iv_mark)).setImageDrawable(context.getDrawable(R.drawable.rod_num_4_gr));
                        }
                        rodsMarks.add(marks.get(position));
                        break;
                }
                if (editable) {
                    rowView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (spodId <= 0) {
                                for (int i = 0; i < rodsMarks.size(); i++) {
                                    if (rodsMarks.get(i).getSpodId() == editableRod) {
                                        rodsMarks.get(i).setSpodId(0);
                                        rodsMarks.remove(i);
                                        break;
                                    }
                                }
                                rodsMarks.add(marks.get(position));
                                marks.get(position).setSpodId(editableRod);
                                MapAdapter.this.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        }


        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cellHeight, context.getResources().getDisplayMetrics());
        rowView.getLayoutParams().height = dimensionInDp;
        rowView.requestLayout();

        if (position == 0) {
            getColumnWidth(rowView);
        }

        return rowView;
    }

    public List<MapMark> getRodsMarks() {
        return rodsMarks;
    }

    private void getColumnWidth(View view) {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                columnWidth = view.getMeasuredWidth();
                return true;
            }
        });
    }

    public int getColumnWidth() {
        return columnWidth;
    }
}