package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.RodsSettingsListener;
import com.linaverde.fishingapp.interfaces.ViolationChangedRequestListener;
import com.linaverde.fishingapp.services.RodsMainSettingsGridAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RodsMainFragment extends Fragment {

    private static final String SPOD = "spod";
    private static final String JSON = "json";

    private boolean spod;
    private JSONObject settings;

    RodsSettingsListener listener;

    public RodsMainFragment() {
        // Required empty public constructor
    }

    public static RodsMainFragment newInstance(boolean spod, String json) {
        RodsMainFragment fragment = new RodsMainFragment();
        Bundle args = new Bundle();
        args.putBoolean(SPOD, spod);
        args.putString(JSON, json);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            spod = getArguments().getBoolean(SPOD);
            try {
                settings = new JSONObject(getArguments().getString(JSON));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rods_main, container, false);

        TextView tv = view.findViewById(R.id.tv_rods_type);

        if (spod) {
            tv.setText(getString(R.string.settings_rods_spod));
        } else {
            tv.setText(getString(R.string.settings_rods_work));
        }

        String rodType;
        if (spod) {
            rodType = "spod";
        } else {
            rodType = "carp";
        }

        RodsMainSettingsGridAdapter adapterRod1, adapterRod2, adapterRod3, adapterRod4;
        try {
            JSONArray rods = settings.getJSONArray("rods");
            adapterRod1 = new RodsMainSettingsGridAdapter(getContext(), getLayoutInflater(), rods.getJSONObject(0));
            ((GridView) view.findViewById(R.id.rod1_main_settings_grid)).setAdapter(adapterRod1);
            adapterRod2 = new RodsMainSettingsGridAdapter(getContext(), getLayoutInflater(), rods.getJSONObject(1));
            ((GridView) view.findViewById(R.id.rod2_main_settings_grid)).setAdapter(adapterRod2);
            adapterRod3 = new RodsMainSettingsGridAdapter(getContext(), getLayoutInflater(), rods.getJSONObject(2));
            ((GridView) view.findViewById(R.id.rod3_main_settings_grid)).setAdapter(adapterRod3);
            adapterRod4 = new RodsMainSettingsGridAdapter(getContext(), getLayoutInflater(), rods.getJSONObject(3));
            ((GridView) view.findViewById(R.id.rod4_main_settings_grid)).setAdapter(adapterRod4);

            ((LinearLayout) view.findViewById(R.id.iv_rod1)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.rodsDetailedReqired(rodType, adapterRod1.getRodID(), adapterRod1.isCast());
                }
            });

            ((LinearLayout) view.findViewById(R.id.iv_rod2)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.rodsDetailedReqired(rodType, adapterRod2.getRodID(), adapterRod2.isCast());
                }
            });

            ((LinearLayout) view.findViewById(R.id.iv_rod3)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.rodsDetailedReqired(rodType, adapterRod3.getRodID(), adapterRod3.isCast());
                }
            });

            ((LinearLayout) view.findViewById(R.id.iv_rod4)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.rodsDetailedReqired(rodType, adapterRod4.getRodID(), adapterRod4.isCast());
                }
            });


            String rodName;
            if (spod) {
                rodName = "Сподовое удилище";
            } else {
                rodName = "Рабочее удилище";
            }

            for (int i = 1; i <= 4; i++) {
                int currRes;
                String ID;
                //название удочек в зависимости от типа
                ID = "rod_name_" + i;
                currRes = getResources().getIdentifier(ID, "id", getContext().getPackageName());
                ((TextView) view.findViewById(currRes)).setText(rodName);

                ID = "rod_" + i + "_back";
                currRes = getResources().getIdentifier(ID, "id", getContext().getPackageName());
                int imageRes;
                String imageId;
                Drawable d;
                if (rods.getJSONObject(i - 1).getBoolean("cast")) {
                    imageId = "rod_" + i + "_back_red";
                    imageRes = getResources().getIdentifier(imageId, "drawable", getContext().getPackageName());
                } else {
                    imageId = "rod_" + i + "_back_gr";
                    imageRes = getResources().getIdentifier(imageId, "drawable", getContext().getPackageName());
                }
                d = getContext().getDrawable(imageRes);
                ((ImageView) view.findViewById(currRes)).setImageDrawable(d);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RodsSettingsListener) {
            //init the listener
            listener = (RodsSettingsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RodsSettingsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}