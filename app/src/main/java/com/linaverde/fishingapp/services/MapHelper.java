package com.linaverde.fishingapp.services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
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

    public MapHelper(Context context, LayoutInflater inflater, GridView map, LinearLayout llMarks, LinearLayout arrow, JSONObject mapDetail) {
        this.context = context;
        this.map = map;
        this.llMarks = llMarks;
        this.mapDetail = mapDetail;
        this.inflater = inflater;
        try {
            landmark = mapDetail.getJSONArray("landmark");
            rods = mapDetail.getJSONArray("rods");
            setLandmarks(arrow);
            setMap();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void movePonton(ImageView ponton) {
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) ponton.getLayoutParams();
        marginParams.setMargins(0, 0, map.getRequestedColumnWidth(), 0);
        Log.d("Column width", Integer.toString(map.getRequestedColumnWidth()));
    }

    private void setLandmarks(LinearLayout arrow) throws JSONException {
        for (int i = 0; i < landmark.length(); i++) {
            TextView view = (TextView) inflater.inflate(R.layout.landmark, llMarks, false);
            view.setText(landmark.getString(i));
            llMarks.addView(view);
        }
        llMarks.setWeightSum(landmark.length() + 1);
        arrow.setWeightSum(landmark.length() + 1);

    }

    private void setMap() throws JSONException {
        map.setNumColumns(landmark.length() + 1);
        JSONObject distBig, distSmall;
        distSmall = mapDetail.getJSONArray("distance").getJSONObject(0);
        distBig = mapDetail.getJSONArray("distance").getJSONObject(1);
        List<MapMark> marks = new ArrayList<>();
        double curr = distBig.getDouble("top");
        double currBot = distBig.getDouble("bottom");
        double currStep = distBig.getDouble("step");
        while (curr >= currBot) {
            for (int i = 0; i < landmark.length(); i++) {
                MapMark newMark = null;
                for (int j = 0; j < rods.length(); j++){
                    String cMark = rods.getJSONObject(j).getString("landmark");
                    int cDist = rods.getJSONObject(j).getInt("distance");
                    if (landmark.getString(i).equals(cMark) && curr >= cDist && curr - currStep < cDist){
                        newMark = new MapMark(cDist, rods.getJSONObject(j).getInt("id"), cMark);
                        break;
                    }
                }
                if (newMark == null) {
                    marks.add(new MapMark());
                } else {
                    marks.add(newMark);
                }
            }
            marks.add(new MapMark(curr));
            curr = curr - currStep;
        }
        currBot = distSmall.getDouble("bottom");
        currStep = distSmall.getDouble("step");
        while (curr >= currBot) {
            for (int i = 0; i < landmark.length(); i++) {
                MapMark newMark = null;
                for (int j = 0; j < rods.length(); j++){
                    String cMark = rods.getJSONObject(j).getString("landmark");
                    int cDist = rods.getJSONObject(j).getInt("distance");
                    if (landmark.getString(i).equals(cMark) && curr >= cDist && curr - currStep < cDist){
                        newMark = new MapMark(cDist, rods.getJSONObject(j).getInt("id"), cMark);
                        break;
                    }
                }
                if (newMark == null) {
                    marks.add(new MapMark());
                } else {
                    marks.add(newMark);
                }
            }
            marks.add(new MapMark(curr));
            curr = curr - currStep;
        }
        MapAdapter adapter = new MapAdapter(context, inflater, marks);
        map.setAdapter(adapter);
        //setDistance(distBig, distSmall);
    }
}
