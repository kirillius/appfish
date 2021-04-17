package com.linaverde.fishingapp.services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.MapRodClickedListener;
import com.linaverde.fishingapp.models.MapMark;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapHelper {

    Context context;
    GridView map;
    LinearLayout llMarks;
    JSONObject mapDetail;
    LayoutInflater inflater;
    JSONArray landmark;
    JSONArray rods;
    JSONArray spod;
    int editableRod;
    MapRodClickedListener listener;
    boolean showSpod;


    public MapHelper(Context context, LayoutInflater inflater, GridView map, LinearLayout llMarks,
                     JSONObject mapDetail, int editableRod, boolean showSpod, MapRodClickedListener listener) {
        this.context = context;
        this.map = map;
        this.llMarks = llMarks;
        this.mapDetail = mapDetail;
        this.inflater = inflater;
        this.editableRod = editableRod;
        this.listener = listener;
        this.showSpod = showSpod;
        try {
            this.rods = mapDetail.getJSONArray("rods");
            this.spod = mapDetail.getJSONArray("spod");
            landmark = mapDetail.getJSONArray("landmark");
            setLandmarks();
            setMap();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setLandmarks() throws JSONException {
        TextView empty = (TextView) inflater.inflate(R.layout.landmark, llMarks, false);
        empty.setText("");
        llMarks.addView(empty);
        for (int i = 0; i < landmark.length(); i++) {
            TextView view = (TextView) inflater.inflate(R.layout.landmark, llMarks, false);
            view.setText(landmark.getString(i));
            llMarks.addView(view);
        }
        llMarks.setWeightSum(landmark.length() + 1);

    }

    List<MapMark> marks;
    MapAdapter adapter;

    private void setMap() throws JSONException {
        map.setNumColumns(landmark.length() + 1);
        JSONObject distBig, distSmall;
        distSmall = mapDetail.getJSONArray("distance").getJSONObject(0);
        distBig = mapDetail.getJSONArray("distance").getJSONObject(1);
        marks = new ArrayList<>();
        double curr = distBig.getDouble("top");
        double currBot = distBig.getDouble("bottom");
        double currStep = distBig.getDouble("step");
        while (curr >= currBot) {
            marks.add(new MapMark(curr));
            for (int i = 0; i < landmark.length(); i++) {
                MapMark newMark = null;
                for (int j = 0; j < rods.length(); j++) {
                    String cMark = rods.getJSONObject(j).getString("landmark");
                    int cDist = rods.getJSONObject(j).getInt("distance");
                    if (landmark.getString(i).equals(cMark) && curr >= cDist && curr - currStep < cDist) {
                        newMark = new MapMark(cDist, rods.getJSONObject(j).getInt("id"), cMark,
                                rods.getJSONObject(j).getString("comment"),
                                rods.getJSONObject(j).getBoolean("cast"),
                                rods.getJSONObject(j).getInt("depth"));
                        break;
                    }
                }
                for (int j = 0; j < spod.length(); j++) {
                    String sMark = spod.getJSONObject(j).getString("landmark");
                    int sDist = spod.getJSONObject(j).getInt("distance");
                    if (landmark.getString(i).equals(sMark) && curr >= sDist && curr - currStep < sDist) {
                        if (newMark == null) {
                            Log.d("Spod", "create new spod grid item");
                            newMark = new MapMark(sDist, 0, sMark, spod.getJSONObject(j).getInt("id"));
                        } else {
                            Log.d("Spod", "update current grid item");
                            newMark.setSpodId(spod.getJSONObject(j).getInt("id"));

                        }
                        break;
                    }
                }
                if (newMark == null) {
                    marks.add(new MapMark(landmark.getString(i), curr));
                } else {
                    marks.add(newMark);
                }
            }
            curr = curr - currStep;
        }
        currBot = distSmall.getDouble("bottom");
        currStep = distSmall.getDouble("step");
        while (curr >= currBot) {
            marks.add(new MapMark(curr));
            for (int i = 0; i < landmark.length(); i++) {
                MapMark newMark = null;
                for (int j = 0; j < rods.length(); j++) {
                    String cMark = rods.getJSONObject(j).getString("landmark");
                    int cDist = rods.getJSONObject(j).getInt("distance");
                    if (landmark.getString(i).equals(cMark) && curr >= cDist && curr - currStep < cDist) {
                        newMark = new MapMark(cDist, rods.getJSONObject(j).getInt("id"), cMark,
                                rods.getJSONObject(j).getString("comment"),
                                rods.getJSONObject(j).getBoolean("cast"),
                                rods.getJSONObject(j).getInt("depth"));
                        break;
                    }
                }
                for (int j = 0; j < spod.length(); j++) {
                    String sMark = spod.getJSONObject(j).getString("landmark");
                    int sDist = spod.getJSONObject(j).getInt("distance");
                    if (landmark.getString(i).equals(sMark) && curr >= sDist && curr - currStep < sDist) {
                        if (newMark == null) {
                            Log.d("Spod", "create new spod grid item");
                            newMark = new MapMark(sDist, 0, sMark, spod.getJSONObject(j).getInt("id"));
                        } else {
                            Log.d("Spod", "update current grid item");
                            newMark.setSpodId(spod.getJSONObject(j).getInt("id"));

                        }
                        break;
                    }
                }
                if (newMark == null) {
                    marks.add(new MapMark(landmark.getString(i), curr));
                } else {
                    marks.add(newMark);
                }
            }
            curr = curr - currStep;
        }


        adapter = new MapAdapter(context, inflater, marks, mapDetail.getInt("cellHeight"), editableRod, showSpod, listener);
        map.setAdapter(adapter);
        //setDistance(distBig, distSmall);
    }

    public String getLandmark(int rodId, boolean spod) {
        List<MapMark> rodsMarks = adapter.getRodsMarks();

        for (int i = 0; i < rodsMarks.size(); i++) {
            if (!spod) {
                if (rodId == rodsMarks.get(i).getRodId()) {
                    return rodsMarks.get(i).getLandmark();
                }
            } else {
                if (rodId == rodsMarks.get(i).getSpodId()) {
                    return rodsMarks.get(i).getLandmark();
                }
            }
        }
        return "";
    }

    public double getDistance(int rodId, boolean spod) {
        List<MapMark> rodsMarks = adapter.getRodsMarks();
        for (int i = 0; i < rodsMarks.size(); i++) {
            if (!spod) {
                if (rodId == rodsMarks.get(i).getRodId()) {
                    return rodsMarks.get(i).getDistance();
                }
            } else {
                if (rodId == rodsMarks.get(i).getSpodId()) {
                    return rodsMarks.get(i).getDistance();
                }
            }
        }

        return -1;
    }

}
