package com.linaverde.fishingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.activities.StatisticActivity;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.OneTeamClickListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.ImageHelper;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterOneTeamFragment extends Fragment {

    private static final String CAPTAIN = "captain";
    private static final String ASSISTANT = "assistant";
    private static final String CAPTAINID = "captainID";
    private static final String ASSISTANTID = "assistantID";
    private static final String CAPTAIN_DOCS = "captain_docs";
    private static final String ASSISTANT_DOCS = "assistant_docs";
    private static final String MATCH_ID = "matchID";
    private static final String TEAM_ID = "teamID";
    private static final String MATCH_NAME = "matchName";

    private String matchName;
    private String captainName;
    private String assistantName;
    private String captainId;
    private String assistantId;
    private String captainPhoto = "";
    private String assistantPhoto = "";
    private JSONObject captainDocs;
    private JSONObject assistantDocs;
    private String matchId;
    private String teamId;
    private String[] captainLinks;
    private String[] assistantLinks;

    OneTeamClickListener listener;

    public static RegisterOneTeamFragment newInstance(String captain, String captainId,
                                                      String assistantName, String assistantId,
                                                      String captainDocs, String assistantDocs, String matchId, String teamId, String matchName) {

        RegisterOneTeamFragment fragment = new RegisterOneTeamFragment();
        Bundle args = new Bundle();
        args.putString(CAPTAIN, captain);
        args.putString(ASSISTANT, assistantName);
        args.putString(CAPTAINID, captainId);
        args.putString(ASSISTANTID, assistantId);
        args.putString(CAPTAIN_DOCS, captainDocs);
        args.putString(ASSISTANT_DOCS, assistantDocs);
        args.putString(MATCH_ID, matchId);
        args.putString(TEAM_ID, teamId);
        args.putString(MATCH_NAME, matchName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            captainName = getArguments().getString(CAPTAIN);
            assistantName = getArguments().getString(ASSISTANT);
            captainId = getArguments().getString(CAPTAINID);
            assistantId = getArguments().getString(ASSISTANTID);
            matchId = getArguments().getString(MATCH_ID);
            teamId = getArguments().getString(TEAM_ID);
            matchName = getArguments().getString(MATCH_NAME);

            try {
                captainDocs = new JSONObject(getArguments().getString(CAPTAIN_DOCS));
                assistantDocs = new JSONObject(getArguments().getString(ASSISTANT_DOCS));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    TextView tvCaptainName;
    TextView tvAssistantName;
    PorterShapeImageView ivCaptainPhoto;
    PorterShapeImageView ivAssistantPhoto;

    ImageButton captainPass, captainMed, captainSport;
    ImageButton assistantPass, assistantMed, assistantSport;

    RelativeLayout buttonEndReg;
    ContentLoadingProgressBar progressBar;

    TextView statistics, violations;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_one_team, container, false);
        tvCaptainName = view.findViewById(R.id.tv_captain_name);
        tvCaptainName.setText(captainName);
        tvAssistantName = view.findViewById(R.id.tv_assistant_name);
        tvAssistantName.setText(assistantName);
        ivCaptainPhoto = view.findViewById(R.id.iv_captain_photo);
        ivAssistantPhoto = view.findViewById(R.id.iv_assistant_photo);
        progressBar = view.findViewById(R.id.progress_bar);

        ImageView photo = view.findViewById(R.id.iv_photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo.setVisibility(View.GONE);
            }
        });

        RequestHelper requestHelper = new RequestHelper(getContext());
        progressBar.show();
        requestHelper.executeGet("teams", new String[]{"match", "team"}, new String[]{matchId, teamId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                try {
                    json = json.getJSONObject("teams");
                    captainPhoto = json.getString("captainPhoto");
                    assistantPhoto = json.getString("assistantPhoto");
                    captainLinks = new String[4];
                    captainLinks[0] = json.getString("captainLink1");
                    captainLinks[1] = json.getString("captainLink2");
                    captainLinks[2] = json.getString("captainLink3");
                    captainLinks[3] = json.getString("captainLink4");

                    assistantLinks = new String[4];
                    assistantLinks[0] = json.getString("assistantLink1");
                    assistantLinks[1] = json.getString("assistantLink2");
                    assistantLinks[2] = json.getString("assistantLink3");
                    assistantLinks[3] = json.getString("assistantLink4");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (captainPhoto != null && !captainPhoto.equals("null") && !captainPhoto.equals("")) {
                    ivCaptainPhoto.setImageBitmap(ImageHelper.decodeToImage(captainPhoto));
                    ivCaptainPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            photo.setImageBitmap(ImageHelper.decodeToImage(captainPhoto));
                            photo.setVisibility(View.VISIBLE);
                        }
                    });
                }
                if (assistantPhoto != null && !assistantPhoto.equals("null") && !assistantPhoto.equals("")) {
                    ivAssistantPhoto.setImageBitmap(ImageHelper.decodeToImage(assistantPhoto));
                    ivAssistantPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            photo.setImageBitmap(ImageHelper.decodeToImage(assistantPhoto));
                            photo.setVisibility(View.VISIBLE);
                        }
                    });
                }
                view.findViewById(R.id.captain_net1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!captainLinks[0].equals("")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(captainLinks[0]));
                            startActivity(browserIntent);
                        }
                    }
                });

                view.findViewById(R.id.captain_net2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!captainLinks[1].equals("")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(captainLinks[1]));
                            startActivity(browserIntent);
                        }
                    }
                });

                view.findViewById(R.id.captain_net3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!captainLinks[2].equals("")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(captainLinks[2]));
                            startActivity(browserIntent);
                        }
                    }
                });

                view.findViewById(R.id.captain_net4).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!captainLinks[3].equals("")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(captainLinks[3]));
                            startActivity(browserIntent);
                        }
                    }
                });

                view.findViewById(R.id.assistant_net1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!assistantLinks[0].equals("")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(assistantLinks[0]));
                            startActivity(browserIntent);
                        }
                    }
                });

                view.findViewById(R.id.assistant_net2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!assistantLinks[1].equals("")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(assistantLinks[1]));
                            startActivity(browserIntent);
                        }
                    }
                });

                view.findViewById(R.id.assistant_net3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!assistantLinks[2].equals("")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(assistantLinks[2]));
                            startActivity(browserIntent);
                        }
                    }
                });

                view.findViewById(R.id.assistant_net4).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!assistantLinks[3].equals("")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(assistantLinks[3]));
                            startActivity(browserIntent);
                        }
                    }
                });
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
            }
        });

        captainPass = view.findViewById(R.id.ib_captain_pass);
        captainMed = view.findViewById(R.id.ib_captain_med);
        captainSport = view.findViewById(R.id.ib_captain_sport);

        assistantPass = view.findViewById(R.id.ib_assistant_pass);
        assistantMed = view.findViewById(R.id.ib_assistant_med);
        assistantSport = view.findViewById(R.id.ib_assistant_sport);

        buttonEndReg = view.findViewById(R.id.button_end_reg);

        try {
            if (captainDocs.getBoolean("doc1")) {
                captainPass.setImageResource(R.drawable.document_check);
            } else {
                captainPass.setEnabled(false);
            }
            if (captainDocs.getBoolean("doc2")) {
                captainMed.setImageResource(R.drawable.document_check);
            } else {
                captainMed.setEnabled(false);
            }
            if (captainDocs.getBoolean("doc3")) {
                captainSport.setImageResource(R.drawable.document_check);
            } else {
                captainSport.setEnabled(false);
            }
            if (assistantDocs.getBoolean("doc1")) {
                assistantPass.setImageResource(R.drawable.document_check);
            } else {
                assistantPass.setEnabled(false);
            }
            if (assistantDocs.getBoolean("doc2")) {
                assistantMed.setImageResource(R.drawable.document_check);
            } else {
                assistantMed.setEnabled(false);
            }
            if (assistantDocs.getBoolean("doc3")) {
                assistantSport.setImageResource(R.drawable.document_check);
            } else {
                assistantSport.setEnabled(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        captainPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDocumentClicked(captainId, 1);
            }
        });

        captainMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDocumentClicked(captainId, 2);
            }
        });

        captainSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDocumentClicked(captainId, 3);
            }
        });

        assistantPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDocumentClicked(assistantId, 1);
            }
        });

        assistantMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDocumentClicked(assistantId, 2);
            }
        });

        assistantSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDocumentClicked(assistantId, 3);
            }
        });

        statistics = view.findViewById(R.id.tv_statistics);

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StatisticActivity.class);
                Bundle b = new Bundle();
                b.putString("matchId", matchId);
                b.putString("teamId", teamId);
                b.putString("matchName", matchName);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        violations = view.findViewById(R.id.tv_violations);

        violations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViolationClicked(teamId);
            }
        });

        UserInfo userInfo = new UserInfo(getContext());
        if (userInfo.getUserType() != 1){
            buttonEndReg.setVisibility(View.GONE);
        }

        buttonEndReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogBuilder.createTwoButtons(getContext(), getLayoutInflater(), getString(R.string.end_reg_team_question), new CompleteActionListener() {
                    @Override
                    public void onOk(String input) {
                        progressBar.show();
                        RequestHelper requestHelper = new RequestHelper(getContext());
                        requestHelper.executePost("teamcheckin", new String[]{"match", "team"}, new String[]{matchId, teamId}, null, new RequestListener() {
                            @Override
                            public void onComplete(JSONObject json) {
                                progressBar.hide();
                                try {
                                    String error = json.getString("error");
                                    if (!error.equals("") && !error.equals("null")) {
                                        DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.error) + error, null);
                                    } else {
                                        DialogBuilder.createDefaultDialog(getContext(), getLayoutInflater(), getString(R.string.end_reg_team), null);
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

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });


        return view;
    }

    private void getPhotosAndLinks() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OneTeamClickListener) {
            //init the listener
            listener = (OneTeamClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DocumentClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}