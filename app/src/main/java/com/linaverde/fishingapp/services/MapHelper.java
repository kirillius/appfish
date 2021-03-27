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

    public MapHelper(Context context, LayoutInflater inflater, GridView map, LinearLayout llMarks, LinearLayout arrow, JSONObject mapDetail) {
        this.context = context;
        this.map = map;
        this.llMarks = llMarks;
        this.mapDetail = mapDetail;
        this.inflater = inflater;
        try {
            setLandmarks(arrow);
            setMap();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void movePonton(ImageView ponton) {
//        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(ponton.getLayoutParams());
//        marginParams.setMargins(0, 0, 0,0);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
//        layoutParams.addRule();
//        ponton.setLayoutParams(layoutParams);
        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) ponton.getLayoutParams();
        marginParams.setMargins(0, 0, map.getRequestedColumnWidth(), 0);
        Log.d("Column width", Integer.toString(map.getRequestedColumnWidth()));
    }

    private void setLandmarks(LinearLayout arrow) throws JSONException {
        landmark = mapDetail.getJSONArray("landmark");
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
                marks.add(new MapMark());
            }
            marks.add(new MapMark(curr, true));
            curr = curr - currStep;
        }
        currBot = distSmall.getDouble("bottom");
        currStep = distSmall.getDouble("step");
        while (curr >= currBot) {
            for (int i = 0; i < landmark.length(); i++) {
                marks.add(new MapMark());
            }
            marks.add(new MapMark(curr, true));
            curr = curr - currStep;
        }
        MapAdapter adapter = new MapAdapter(context, inflater, marks);
        map.setAdapter(adapter);
        //setDistance(distBig, distSmall);
    }

//    private void setDistance(JSONObject distBig, JSONObject distSmall) throws JSONException {
//        List<Double> distance = new ArrayList<>();
//        double curr = distBig.getDouble("top");
//        double currBot = distBig.getDouble("bottom");
//        double currStep = distBig.getDouble("step");
//        while(curr >= currBot){
//            distance.add(curr);
//            curr = curr - currStep;
//        }
//        currBot = distSmall.getDouble("bottom");
//        currStep = distSmall.getDouble("step");
//        while(curr >= currBot){
//            distance.add(curr);
//            curr = curr - currStep;
//        }
//        DistanceAdapter adapter = new DistanceAdapter(context, distance);
//        lvDistance.setAdapter(adapter);
//
//
//
////        lvDistance.setOnScrollChangeListener(new View.OnScrollChangeListener() {
////            @Override
////            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
////                int top = lvDistance.getTop();
////            }
////        });
//    }
}
