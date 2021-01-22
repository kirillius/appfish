package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.FishChangedRequestListener;
import com.linaverde.fishingapp.interfaces.ViolationChangedRequestListener;
import com.linaverde.fishingapp.interfaces.ViolationListChangeListener;
import com.linaverde.fishingapp.models.Fish;
import com.linaverde.fishingapp.models.FishDictionaryItem;
import com.linaverde.fishingapp.models.Violation;
import com.linaverde.fishingapp.models.ViolationDictionaryItem;
import com.linaverde.fishingapp.services.UserInfo;
import com.linaverde.fishingapp.services.ViolationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViolationsFragment extends Fragment {

    private static final String VIOLATIONS = "violations";
    private static final String DICTIONARY = "dict";
    private static final String STAGE = "stage";
    private static final String TEAM = "team";
    private static final String SECTOR = "sector";
    private static final String EDIT = "edit";

    private JSONArray violations;
    private JSONArray dict;
    private String stageId;
    private String teamId;
    private int sector;
    private boolean edit;

    public ViolationsFragment() {
        // Required empty public constructor
    }

    private boolean violationChanged = false;
    private int changedPos = -1;
    private String newTime = "";
    private String newId = "";

    public static ViolationsFragment newInstance(String violations, String dict, String stage, String team, int sector, boolean edit) {
        ViolationsFragment fragment = new ViolationsFragment();
        Bundle args = new Bundle();
        args.putString(VIOLATIONS, violations);
        args.putString(DICTIONARY, dict);
        args.putString(STAGE, stage);
        args.putString(TEAM, team);
        args.putInt(SECTOR, sector);
        args.putBoolean(EDIT, edit);
        fragment.setArguments(args);
        return fragment;
    }

    ViolationChangedRequestListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            teamId = getArguments().getString(TEAM);
            stageId = getArguments().getString(STAGE);
            sector = getArguments().getInt(SECTOR);
            edit = getArguments().getBoolean(EDIT);
            try {
                violations = new JSONArray(getArguments().getString(VIOLATIONS));
                dict = new JSONArray(getArguments().getString(DICTIONARY));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    TextView tvPond, tvSector, tvMatch, tvSanction;
    ListView lvViolations;
    ImageView buttonAdd;
    ViolationAdapter adapter;
    LinearLayout sanction;
    RelativeLayout confirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_violations, container, false);

        tvSector = view.findViewById(R.id.tv_sector);
        tvPond = view.findViewById(R.id.tv_pond_name);
        tvMatch = view.findViewById(R.id.tv_tournament_name);
        UserInfo userInfo = new UserInfo(getContext());
        tvPond.setText(userInfo.getPond());
        if (sector != -1) {
            tvSector.setText("Сектор " + Integer.toString(sector));
        } else {
            tvSector.setVisibility(View.GONE);
        }
        tvMatch.setText(userInfo.getMatchName());

        lvViolations = view.findViewById(R.id.lv_violations);
        buttonAdd = view.findViewById(R.id.button_add_violation);
        confirm = view.findViewById(R.id.rl_button_violation_save);
        sanction = view.findViewById(R.id.ll_sanction);
        tvSanction = view.findViewById(R.id.tv_sanction);

        List<Violation> violationsArr = new ArrayList<>();
        ViolationDictionaryItem[] dictArr;
        dictArr = new ViolationDictionaryItem[dict.length()];
        try {
            for (int i = 0; i < violations.length(); i++) {
                violationsArr.add(new Violation(violations.getJSONObject(i)));
            }
            for (int i = 0; i < dict.length(); i++) {
                dictArr[i] = new ViolationDictionaryItem(dict.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //определяем меру пресечения

        if (violationsArr.size() > 0) {
            boolean dis = false;
            for (int i = 0; i < violationsArr.size(); i++) {
                Violation curr = violationsArr.get(i);
                for (int j = 0; j < dictArr.length; j++) {
                    if (dictArr[j].getId().equals(curr.getViolationId())) {
                        if (dictArr[j].getSendOff() == 1) {
                            dis = true;
                            break;
                        }
                    }
                }
                if (dis) break;
            }

            if (dis) {
                tvSanction.setText(R.string.disqualification);
            }
        } else {
            tvSanction.setText("");
        }

        ViolationListChangeListener violationListChangeListener = new ViolationListChangeListener() {
            @Override
            public void selectionChanged(int pos, String foulId) {
                violationChanged = true;
                newId = foulId;
                changedPos = pos;
                confirm.setVisibility(View.VISIBLE);
                buttonAdd.setVisibility(View.GONE);
                sanction.setVisibility(View.GONE);
                violationsArr.get(pos).setViolationId(foulId);
                adapter.setChangedId(pos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void violationTimeChanged(int pos, String date, String time) {
                violationChanged = true;
                newTime = date + "T" + time;
                changedPos = pos;
                confirm.setVisibility(View.VISIBLE);
                buttonAdd.setVisibility(View.GONE);
                sanction.setVisibility(View.GONE);
                adapter.setChangedId(pos);
                violationsArr.get(pos).setTime(time);
                adapter.notifyDataSetChanged();
            }
        };

        if (!edit)
            lvViolations.setEnabled(false);

        if (!edit)
            buttonAdd.setVisibility(View.GONE);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!violationChanged) {
                    violationChanged = true;
                    violationsArr.add(new Violation(dictArr[0].getId()));
                    adapter.setChangedId(violationsArr.size() - 1);
                    adapter.setNewViolationAdded();
                    adapter.notifyDataSetChanged();
                    confirm.setVisibility(View.VISIBLE);
                    buttonAdd.setVisibility(View.GONE);
                    sanction.setVisibility(View.GONE);
                    changedPos = violationsArr.size() - 1;
                }
            }
        });

        adapter = new ViolationAdapter(getContext(), violationsArr, dictArr, edit, violationListChangeListener);
        lvViolations.setAdapter(adapter);

        if (!edit)
            confirm.setVisibility(View.GONE);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
                    if (!violationsArr.get(changedPos).getId().equals("")) {
                        object.put("id", violationsArr.get(changedPos).getId());
                    }
                    if (!newId.equals("")) {
                        object.put("foulId", newId);
                    } else {
                        object.put("foulId", violationsArr.get(changedPos).getViolationId());
                    }
                    if (!newTime.equals("")) {
                        object.put("time", newTime);
                    } else {
                        object.put("time", violationsArr.get(changedPos).getDateTime());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listener.violationChangedRequest(stageId, teamId, object.toString(), sector);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViolationChangedRequestListener) {
            //init the listener
            listener = (ViolationChangedRequestListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}