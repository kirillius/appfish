package com.linaverde.fishingapp.services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    ListView lvDistance;
    JSONArray landmark;

    public MapHelper (Context context, LayoutInflater inflater, GridView map, LinearLayout llMarks, ListView lvDistance, JSONObject mapDetail){
        this.context = context;
        this.map = map;
        this.llMarks = llMarks;
        this.mapDetail = mapDetail;
        this.inflater = inflater;
        this.lvDistance = lvDistance;
        try {
            setLandmarks();
            setMap();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setLandmarks() throws JSONException {

        landmark = mapDetail.getJSONArray("landmark");
        for (int i = 0; i < landmark.length(); i++){
            TextView view = (TextView) inflater.inflate(R.layout.landmark, llMarks, false);
            view.setText(landmark.getString(i));
            llMarks.addView(view);
        }
    }

    private void setMap() throws JSONException {
        map.setNumColumns(landmark.length());

        JSONObject distBig, distSmall;
        distSmall = mapDetail.getJSONArray("distance").getJSONObject(0);
        distBig = mapDetail.getJSONArray("distance").getJSONObject(1);
        int bigCountRows = (int) ((distBig.getDouble("top") - distBig.getDouble("bottom") + distBig.getDouble("step"))/distBig.getDouble("step"));
        int smallCountRows = (int) ((distSmall.getDouble("top") - distSmall.getDouble("bottom") + distSmall.getDouble("step"))/distSmall.getDouble("step"));
        List<MapMark> marks = new ArrayList<>();
        Log.d("rows count", Integer.toString(bigCountRows+smallCountRows));
        Log.d("all marks frig items count", Integer.toString((int)(bigCountRows+smallCountRows)*landmark.length()));
        for (int i = 0; i < (bigCountRows+smallCountRows)*landmark.length(); i++){
            marks.add(new MapMark());
        }
        MapAdapter adapter = new MapAdapter(context, inflater, marks);
        map.setAdapter(adapter);
        setDistance(distBig, distSmall);
    }

    private void setDistance(JSONObject distBig, JSONObject distSmall) throws JSONException {
        List<Double> distance = new ArrayList<>();
        double curr = distBig.getDouble("top");
        double currBot = distBig.getDouble("bottom");
        double currStep = distBig.getDouble("step");
        while(curr >= currBot){
            distance.add(curr);
            curr = curr - currStep;
        }
        currBot = distSmall.getDouble("bottom");
        currStep = distSmall.getDouble("step");
        while(curr >= currBot){
            distance.add(curr);
            curr = curr - currStep;
        }
        DistanceAdapter adapter = new DistanceAdapter(context, distance);
        lvDistance.setAdapter(adapter);



//        lvDistance.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                int top = lvDistance.getTop();
//            }
//        });
    }
}
