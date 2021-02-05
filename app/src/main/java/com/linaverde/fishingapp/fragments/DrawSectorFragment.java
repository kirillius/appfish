package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.activities.QueueActivity;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.QueueUpdateListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.interfaces.TeamListClickListener;
import com.linaverde.fishingapp.models.Team;
import com.linaverde.fishingapp.models.TeamsQueue;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.QueueAdapter;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.SectorAdapter;
import com.linaverde.fishingapp.services.TeamsAdapter;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DrawSectorFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private static final String TOURNAMENT_NAME = "name";
    private static final String MATCH = "match";

    private JSONObject mStartParam;
    private String tournamentName;
    private String matchId;
    ContentLoadingProgressBar progressBar;
    QueueUpdateListener listener;

    public DrawSectorFragment() {
        // Required empty public constructor
    }


    public static DrawSectorFragment newInstance(String json, String tournamentName, String matchId) {
        DrawSectorFragment fragment = new DrawSectorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, json);
        args.putString(TOURNAMENT_NAME, tournamentName);
        args.putString(MATCH, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                mStartParam = new JSONObject(getArguments().getString(ARG_PARAM));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tournamentName = getArguments().getString(TOURNAMENT_NAME);
            matchId = getArguments().getString(MATCH);
        }
    }

    SectorAdapter adapter;
    ListView teamsList;
    RelativeLayout endDraw;
    TeamsQueue[] teams;
    UserInfo userInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draw_sector, container, false);
        TextView tvTournamentName = view.findViewById(R.id.tv_tournament_name);
        tvTournamentName.setText(tournamentName);

        teamsList = view.findViewById(R.id.lv_stats);
        endDraw = view.findViewById(R.id.button_end_draw);

        try {
            JSONArray arr = mStartParam.getJSONArray("teams");
            int len = arr.length();
            teams = new TeamsQueue[len];
            for (int i = 0; i < len; i++) {
                teams[i] = new TeamsQueue(arr.getJSONObject(i));

            }
            adapter = new SectorAdapter(getContext(), teams);
            teamsList.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        userInfo = new UserInfo(getContext());
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.hide();
        setButtons(view);
        return view;
    }

    private void setButtons(View view) {
        RequestHelper requestHelper = new RequestHelper(getContext());
        if (userInfo.getUserType() != 1) {
            endDraw.setVisibility(View.GONE);
        } else {
            if (!userInfo.getSectorStatus()) {
                ((TextView) view.findViewById(R.id.button_end_draw_text)).setText(getString(R.string.end_draw));
                endDraw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean emptySector = false;
                        for (TeamsQueue team : teams) {
                            if (team.getSector() == 0) {
                                emptySector = true;
                                break;
                            }
                        }
                        if (emptySector) {
                            DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.sector_empty), null);
                        } else {
                            progressBar.show();
                            requestHelper.executePost("sectorclose", new String[]{"match"}, new String[]{matchId}, null, new RequestListener() {
                                @Override
                                public void onComplete(JSONObject json) {
                                    progressBar.hide();
                                    try {
                                        if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                                            DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.sector_draw_end), null);
                                            userInfo.setStatus(userInfo.getCheckInStatus(), userInfo.getQueueStatus(), true);
                                            setButtons(view);
                                        } else {
                                            DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), json.getString("error"), null);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(int responseCode) {
                                    progressBar.hide();
                                    DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.request_error), null);
                                }
                            });
                        }
                    }
                });
            } else {
                ((TextView) view.findViewById(R.id.button_end_draw_text)).setText(getString(R.string.open_draw));
                endDraw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.show();
                        requestHelper.executePost("sectoropen", new String[]{"match"}, new String[]{matchId}, null, new RequestListener() {
                            @Override
                            public void onComplete(JSONObject json) {
                                progressBar.hide();
                                try {
                                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                                        DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.sector_draw_open), null);
                                        userInfo.setStatus(userInfo.getCheckInStatus(), userInfo.getQueueStatus(), false);
                                        setButtons(view);
                                    } else {
                                        DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), json.getString("error"), null);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(int responseCode) {
                                progressBar.hide();
                                DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.request_error), null);
                            }
                        });
                    }
                });

            }

            if (userInfo.getUserType() == 1) {
                teamsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (!userInfo.getSectorStatus()) {
                            DialogBuilder.createInputDialog(getContext(), getLayoutInflater(), getString(R.string.enter_sector), new CompleteActionListener() {
                                @Override
                                public void onOk(String input) {

                                    if (Integer.parseInt(input) == 0) {
                                        DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.sector_zero), null);
                                    } else {
                                        listener.update(adapter.getItem(position), input);
                                    }
                                }

                                @Override
                                public void onCancel() {
                                    //do nothing
                                }
                            });
                        } else {
                            DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.sector_draw_end), null);
                        }
                    }
                });
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QueueUpdateListener) {
            //init the listener
            listener = (QueueUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TeamListClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}