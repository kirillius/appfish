package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.FishChangedRequestListener;
import com.linaverde.fishingapp.interfaces.FishListChangeActionListener;
import com.linaverde.fishingapp.models.Fish;
import com.linaverde.fishingapp.models.FishDictionaryItem;
import com.linaverde.fishingapp.services.FishAdapter;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WeightingFishFragment extends Fragment {

    private static final String FISHES = "fishes";
    private static final String DICTIONARY = "dict";
    private static final String STAGE = "stage";
    private static final String TEAM = "team";
    private static final String PIN = "pin";
    private static final String SECTOR = "sector";

    private JSONArray fishes;
    private JSONArray dict;
    private String stageId;
    private String teamId;
    private String pin;
    private int sector;

    private boolean fishChanged = false;
    private int changedPos = -1;
    private int newWeight = -1;
    private String newTime = "";
    private String newId = "";

    FishChangedRequestListener listener;

    public WeightingFishFragment() {
        // Required empty public constructor
    }

    public static WeightingFishFragment newInstance(String fishes, String dict, String stage, String team, String pin, int sector) {
        WeightingFishFragment fragment = new WeightingFishFragment();
        Bundle args = new Bundle();
        args.putString(FISHES, fishes);
        args.putString(DICTIONARY, dict);
        args.putString(STAGE, stage);
        args.putString(TEAM, team);
        args.putString(PIN, pin);
        args.putInt(SECTOR, sector);
        fragment.setArguments(args);
        return fragment;
    }

    ListView lvFishes;
    TextView tvPond, tvSector, tvMatch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            teamId = getArguments().getString(TEAM);
            pin = getArguments().getString(PIN);
            stageId = getArguments().getString(STAGE);
            sector = getArguments().getInt(SECTOR);
            try {
                fishes = new JSONArray(getArguments().getString(FISHES));
                dict = new JSONArray(getArguments().getString(DICTIONARY));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    ImageView buttonAdd;
    LinearLayout llPinCode;
    FishAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weighting_fish, container, false);

        tvSector = view.findViewById(R.id.tv_sector);
        tvPond = view.findViewById(R.id.tv_pond_name);
        tvMatch = view.findViewById(R.id.tv_tournament_name);
        UserInfo userInfo = new UserInfo(getContext());
        tvPond.setText(userInfo.getPond());
        tvSector.setText("Сектор " + Integer.toString(sector));
        tvMatch.setText(userInfo.getMatchName());

        lvFishes = view.findViewById(R.id.lv_fishes);
        List<Fish> fishesArr = new ArrayList<>();
        FishDictionaryItem[] dictArr;
        dictArr = new FishDictionaryItem[dict.length()];
        try {
            for (int i = 0; i < fishes.length(); i++) {
                fishesArr.add(new Fish(fishes.getJSONObject(i)));
            }
            for (int i = 0; i < dict.length(); i++) {
                dictArr[i] = new FishDictionaryItem(dict.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        llPinCode = view.findViewById(R.id.ll_pin);
        buttonAdd = view.findViewById(R.id.button_add_weighting);

        FishListChangeActionListener changeActionListener = new FishListChangeActionListener() {
            @Override
            public void fishWeighChanged(int pos, int weight) {
                fishChanged = true;
                changedPos = pos;
                newWeight = weight;
                llPinCode.setVisibility(View.VISIBLE);
                buttonAdd.setVisibility(View.GONE);
                fishesArr.get(pos).setWeight(weight);
                adapter.setChangedId(pos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void fishTimeChanged(int pos, String date, String time) {
                fishChanged = true;
                newTime = date + "T" + time;
                changedPos = pos;
                llPinCode.setVisibility(View.VISIBLE);
                buttonAdd.setVisibility(View.GONE);
                adapter.setChangedId(pos);
                fishesArr.get(pos).setTime(time);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void selectionChanged(int pos, String fishId) {
                fishChanged = true;
                newId = fishId;
                changedPos = pos;
                llPinCode.setVisibility(View.VISIBLE);
                buttonAdd.setVisibility(View.GONE);
                fishesArr.get(pos).setFishId(fishId);
                adapter.setChangedId(pos);
                adapter.notifyDataSetChanged();
            }
        };

        adapter = new FishAdapter(getContext(), fishesArr, dictArr, changeActionListener);
        lvFishes.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fishChanged) {
                    fishChanged = true;
                    fishesArr.add(new Fish(dictArr[0].getId()));
                    adapter.setChangedId(fishesArr.size() - 1);
                    adapter.setNewFishAdded();
                    adapter.notifyDataSetChanged();
                    llPinCode.setVisibility(View.VISIBLE);
                    buttonAdd.setVisibility(View.GONE);
                }
            }
        });

        EditText etPin = view.findViewById(R.id.et_pin);

        TextWatcher pinWathcer = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(pin)) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                    JSONObject object = new JSONObject();
                    try {
                        if (!fishesArr.get(changedPos).getId().equals("")){
                            object.put("id", fishesArr.get(changedPos).getId());
                        }
                        if (!newId.equals("")) {
                            object.put("fishId", newId);
                        } else {
                            object.put("fishId", fishesArr.get(changedPos).getFishId());
                        }
                        if (!newTime.equals("")) {
                            object.put("time", newTime);
                        } else {
                            object.put("time", fishesArr.get(changedPos).getDateTime());
                        }
                        if (newWeight != -1) {
                            object.put("weight", newWeight);
                        } else {
                            object.put("weight", fishesArr.get(changedPos).getWeight());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listener.fishChangedRequest(stageId, teamId, pin, object.toString(), sector);
                }
            }
        };

        etPin.addTextChangedListener(pinWathcer);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FishChangedRequestListener) {
            //init the listener
            listener = (FishChangedRequestListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FishChangedRequestListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}