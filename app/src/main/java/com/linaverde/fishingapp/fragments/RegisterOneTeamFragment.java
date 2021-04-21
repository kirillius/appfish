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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.IOnBackPressed;
import com.linaverde.fishingapp.interfaces.LoadDocumentListener;
import com.linaverde.fishingapp.interfaces.OneTeamClickListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.ImageHelper;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.models.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterOneTeamFragment extends Fragment implements IOnBackPressed, LoadDocumentListener {

    private static final String TEAM_ID = "teamId";
    private static final String TEAM = "team";
    private static final String CHECKIN = "checkIn";
    private static final String BUTTONS = "buttons";


    private String matchName;
    private String captainPhoto = "";
    private String assistantPhoto = "";
    private String matchId;
    private String[] captainLinks;
    private String[] assistantLinks;
    private JSONObject team;
    private String teamId;
    private boolean checkIn;
    private boolean showButtons;
    UserInfo userInfo;


    OneTeamClickListener listener;

    public static RegisterOneTeamFragment newInstance(String teamId, String teamJson, boolean checkIn, boolean showButtons) {

        RegisterOneTeamFragment fragment = new RegisterOneTeamFragment();
        Bundle args = new Bundle();
        args.putString(TEAM_ID, teamId);
        args.putString(TEAM, teamJson);
        args.putBoolean(CHECKIN, checkIn);
        args.putBoolean(BUTTONS, showButtons);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            teamId = getArguments().getString(TEAM_ID);
            checkIn = getArguments().getBoolean(CHECKIN);
            showButtons = getArguments().getBoolean(BUTTONS);
            try {
                team = new JSONObject(getArguments().getString(TEAM));
                team = team.getJSONObject("teams");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    PorterShapeImageView ivCaptainPhoto;
    PorterShapeImageView ivAssistantPhoto;

    ImageButton captainPass, captainMed, captainSport;
    ImageButton assistantPass, assistantMed, assistantSport;

    RelativeLayout buttonEndReg;
    ContentLoadingProgressBar progressBar;

    TextView statistics, violations;
    ImageView photo, ivDocument;
    boolean documentOpen = false;
    boolean photoOpen = false;
    int status;
    RegisterOneTeamFragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_one_team, container, false);
        userInfo = new UserInfo(getContext());
        matchId = userInfo.getMatchId();
        matchName = userInfo.getMatchName();
        fragment = this;
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.hide();
        photo = view.findViewById(R.id.iv_photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo.setVisibility(View.GONE);
                photoOpen = false;
            }
        });

        ivDocument = view.findViewById(R.id.iv_document);

        try {
            ((TextView) view.findViewById(R.id.tv_captain_name)).setText(team.getString("captainName"));
            ((TextView) view.findViewById(R.id.tv_assistant_name)).setText(team.getString("assistantName"));
            status = team.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        fillPhotosAndLinks(view);
        fillDocuments(view);
        statistics = view.findViewById(R.id.tv_statistics);

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    listener.onStatisticsClicked(teamId);

            }
        });


        violations = view.findViewById(R.id.tv_violations);

        violations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViolationClicked(teamId);
            }
        });
        String sStatus = "";
        switch (status) {
            case 0:
                sStatus = "Не зарегистрирована";
                break;
            case 1:
                sStatus = "Зарегистрирована";
                break;
            case 2:
                sStatus = "Отказано в регистрации";
                break;
            default:
                sStatus = "";
        }

        buttonEndReg = view.findViewById(R.id.button_end_reg);
        if ((userInfo.getUserType() != 1 && userInfo.getUserType() != 4) || userInfo.getCheckInStatus() || !showButtons) {
            buttonEndReg.setVisibility(View.GONE);
        } else {
                ((TextView) view.findViewById(R.id.button_end_reg_text)).setText(sStatus);
                buttonEndReg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogBuilder.createSelectRegisterStatusDialog(getContext(), getLayoutInflater(), "Выберите статус регистрации", status, new CompleteActionListener() {
                            @Override
                            public void onOk(String input) {
                                listener.teamRegistered(teamId, Integer.parseInt(input));
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                });
        }
        return view;
    }

    String captainId;
    String assistantId;

    private void fillDocuments(View view) {

        LinearLayout llcaptainDocs, llassistantDocs;
        llcaptainDocs = view.findViewById(R.id.ll_captain_docs);
        llassistantDocs = view.findViewById(R.id.ll_assistant_docs);

        if (!showButtons && userInfo.getUserType() != 1 && userInfo.getUserType() != 4) {
            llcaptainDocs.setVisibility(View.GONE);
            llassistantDocs.setVisibility(View.GONE);
        } else {

            captainPass = view.findViewById(R.id.ib_captain_pass);
            captainMed = view.findViewById(R.id.ib_captain_med);
            captainSport = view.findViewById(R.id.ib_captain_sport);

            assistantPass = view.findViewById(R.id.ib_assistant_pass);
            assistantMed = view.findViewById(R.id.ib_assistant_med);
            assistantSport = view.findViewById(R.id.ib_assistant_sport);


            JSONObject captainDocs = null;
            JSONObject assistantDocs = null;

            try {
                captainDocs = team.getJSONObject("captainDocs");
                assistantDocs = team.getJSONObject("assistantDocs");
            } catch (JSONException e) {
                e.printStackTrace();
            }

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

            try {
                captainId = team.getString("captainId");
                assistantId = team.getString("assistantId");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            captainPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onDocumentClicked(captainId, 1);
                }
            });

            captainMed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onDocumentClicked(captainId, 2);
                }
            });

            captainSport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onDocumentClicked(captainId, 3);
                }
            });

            assistantPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onDocumentClicked(assistantId, 1);
                }
            });

            assistantMed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onDocumentClicked(assistantId, 2);
                }
            });

            assistantSport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onDocumentClicked(assistantId, 3);
                }
            });
        }
    }

    private void fillPhotosAndLinks(View view) {
        ivCaptainPhoto = view.findViewById(R.id.iv_captain_photo);
        ivAssistantPhoto = view.findViewById(R.id.iv_assistant_photo);

        try {
            captainPhoto = team.getString("captainPhoto");
            assistantPhoto = team.getString("assistantPhoto");
            captainLinks = new String[4];
            captainLinks[0] = team.getString("captainLink1");
            captainLinks[1] = team.getString("captainLink2");
            captainLinks[2] = team.getString("captainLink3");
            captainLinks[3] = team.getString("captainLink4");

            assistantLinks = new String[4];
            assistantLinks[0] = team.getString("assistantLink1");
            assistantLinks[1] = team.getString("assistantLink2");
            assistantLinks[2] = team.getString("assistantLink3");
            assistantLinks[3] = team.getString("assistantLink4");
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
                    photoOpen = true;
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
                    photoOpen = true;
                }
            });
        }

        if (!captainLinks[0].equals("")) {
            ImageView iv = view.findViewById(R.id.captain_net1);
            iv.setImageDrawable(getContext().getDrawable(R.drawable.facebook_icon_green));
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(captainLinks[0]));
                    startActivity(browserIntent);

                }
            });
        }

        if (!captainLinks[1].equals("")) {
            ImageView iv = view.findViewById(R.id.captain_net2);
            iv.setImageDrawable(getContext().getDrawable(R.drawable.inst_icon_green));
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(captainLinks[1]));
                    startActivity(browserIntent);

                }
            });
        }

        if (!captainLinks[2].equals("")) {
            ImageView iv = view.findViewById(R.id.captain_net3);
            iv.setImageDrawable(getContext().getDrawable(R.drawable.network_icon_green));
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(captainLinks[2]));
                    startActivity(browserIntent);
                }

            });
        }

        if (!captainLinks[3].equals("")) {
            ImageView iv = view.findViewById(R.id.captain_net4);
            iv.setImageDrawable(getContext().getDrawable(R.drawable.play_icon_green));
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(captainLinks[3]));
                    startActivity(browserIntent);
                }
            });
        }

        if (!assistantLinks[0].equals("")) {
            ImageView iv = view.findViewById(R.id.assistant_net1);
            iv.setImageDrawable(getContext().getDrawable(R.drawable.facebook_icon_green));
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(assistantLinks[0]));
                    startActivity(browserIntent);

                }
            });
        }

        if (!assistantLinks[1].equals("")) {
            ImageView iv = view.findViewById(R.id.assistant_net2);
            iv.setImageDrawable(getContext().getDrawable(R.drawable.inst_icon_green));
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(assistantLinks[1]));
                    startActivity(browserIntent);
                }
            });
        }

        if (!assistantLinks[2].equals("")) {
            ImageView iv = view.findViewById(R.id.assistant_net3);
            iv.setImageDrawable(getContext().getDrawable(R.drawable.network_icon_green));
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(assistantLinks[2]));
                    startActivity(browserIntent);
                }
            });
        }

        if (!assistantLinks[3].equals("")) {
            ImageView iv = view.findViewById(R.id.assistant_net4);
            iv.setImageDrawable(getContext().getDrawable(R.drawable.play_icon_green));
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(assistantLinks[3]));
                    startActivity(browserIntent);
                }
            });
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OneTeamClickListener) {
            //init the listener
            listener = (OneTeamClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OneTeamClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public boolean onBackPressed() {
        if (photoOpen || documentOpen) {
            photo.setVisibility(View.GONE);
            photoOpen = false;
            documentOpen = false;
            ivDocument.setVisibility(View.GONE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDocumentClicked(String userId, int doc) {
        progressBar.show();
        RequestHelper requestHelper = new RequestHelper(getContext());
        requestHelper.getDocument(userId, doc, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        ivDocument.setImageBitmap(ImageHelper.decodeToImage(json.getString("doc")));
                        ivDocument.setVisibility(View.VISIBLE);
                        documentOpen = true;

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