package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.FishChangedRequestListener;
import com.linaverde.fishingapp.models.Fish;
import com.linaverde.fishingapp.models.FishDictionaryItem;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.FishAdapter;
import com.linaverde.fishingapp.models.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class WeightingFishFragment extends Fragment {

    private static final String FISHES = "fishes";
    private static final String DICTIONARY = "dict";
    private static final String STAGE = "stage";
    private static final String TEAM = "team";
    private static final String PIN = "pin";
    private static final String PIN2 = "pin2";
    private static final String SECTOR = "sector";

    private JSONArray fishes;
    private JSONArray dict;
    private String stageId;
    private String teamId;
    private String pin;
    private String pin2;
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

    public static WeightingFishFragment newInstance(String fishes, String dict, String stage, String team, String pin, String pin2, int sector) {
        WeightingFishFragment fragment = new WeightingFishFragment();
        Bundle args = new Bundle();
        args.putString(FISHES, fishes);
        args.putString(DICTIONARY, dict);
        args.putString(STAGE, stage);
        args.putString(TEAM, team);
        args.putString(PIN, pin);
        args.putString(PIN2, pin2);
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
            pin2 = getArguments().getString(PIN2);
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
        adapter = new FishAdapter(getContext(), fishesArr, dictArr);
        lvFishes.setAdapter(adapter);

        if (userInfo.getUserType() == 1 || userInfo.getUserType() == 4) {
            lvFishes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.fishChanged(adapter.getItem(position).toString(), dict.toString(), pin, pin2, teamId, stageId, sector);
                }
            });

            lvFishes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    DialogBuilder.createInputNumberDialog(getContext(), getLayoutInflater(), "Вы действительно хотите удалить эту позицию взвешивания?", true, new CompleteActionListener() {
                        @Override
                        public void onOk(String input) {
                            if (input.equals(pin) || input.equals(pin2)) {
                                listener.fishDeleted(adapter.getId(position), stageId, teamId, pin, pin2, sector);
                            } else {
                                DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), "Неверный пин", null);
                            }
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    return true;
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.fishAdded(dict.toString(), pin, pin2, teamId, stageId, sector);
                }
            });
        } else {
            buttonAdd.setVisibility(View.GONE);
            //lvFishes.setEnabled(false);
        }

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