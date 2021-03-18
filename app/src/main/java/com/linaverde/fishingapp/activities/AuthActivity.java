package com.linaverde.fishingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthActivity extends AppCompatActivity {

    EditText login, password;
    ImageButton showPass;
    Button signIn;
    RelativeLayout rlAuth;
    ContentLoadingProgressBar progressBar;
    ImageView net1, net2, net3, net4;
    RequestHelper requestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        login = findViewById(R.id.et_login);
        password = findViewById(R.id.et_password);
        signIn = findViewById(R.id.button_sign_in);
        showPass = findViewById(R.id.ib_password_show);
        rlAuth = findViewById(R.id.rl_auth);
        progressBar = findViewById(R.id.progress_bar);

        requestHelper = new RequestHelper(this);
        UserInfo userInfo = new UserInfo(this);
        progressBar.hide();
        rlAuth.setVisibility(View.VISIBLE);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                String sLogin, sPassword;
                sLogin = login.getText().toString();
                sPassword = password.getText().toString();
                if (!sLogin.equals("") && !sPassword.equals("")) {
                    progressBar.show();
                    requestHelper.executeGet("session", new String[]{"username", "password"}, new String[]{sLogin, sPassword}, new RequestListener() {
                        @Override
                        public void onComplete(JSONObject json) {
                            progressBar.hide();
                            Log.d("Test auth", "Request fine");
                            try {
                                userInfo.saveUser(sLogin, sPassword, json.getString("userName"), json.getInt("userType"), json.getString("pond"),
                                        json.getString("matchId"), json.getString("matchName"), json.getString("teamId"), json.getString("caption"),
                                        json.getBoolean("isCheckInClosed"), json.getBoolean("isQueueClosed"), json.getBoolean("isSectorClosed"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Bundle args = new Bundle();
                            args.putString("info", json.toString());
                            Intent next = new Intent(AuthActivity.this, TournamentActivity.class);
                            next.putExtras(args); //Optional parameters
                            startActivity(next);
                            finish();
                        }


                        @Override
                        public void onError(int responseCode) {
                            progressBar.hide();
                            if (responseCode == 401) {
                                DialogBuilder.createDefaultDialog(AuthActivity.this, getLayoutInflater(), getString(R.string.login_error), null);
                            } else {
                                DialogBuilder.createDefaultDialog(AuthActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
                            }
                        }
                    });
                } else {
                    DialogBuilder.createDefaultDialog(AuthActivity.this, getLayoutInflater(), getString(R.string.fill_login_password), null);
                }
            }
        });

        showPass.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP:
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;

                    case MotionEvent.ACTION_DOWN:
                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;

                }
                return true;
            }
        });

        setLinks();

        ((RelativeLayout) findViewById(R.id.rl_change_pin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sLogin;
                sLogin = login.getText().toString();
                if (sLogin.equals("")) {
                    DialogBuilder.createDefaultDialog(AuthActivity.this, getLayoutInflater(), "Для смены пин-кода необходимо ввести логин", null);
                } else {
                    progressBar.show();
                    requestHelper.executePost("pin", new String[]{"username"}, new String[]{sLogin}, null, new RequestListener() {
                        @Override
                        public void onComplete(JSONObject json) {
                            progressBar.hide();
                            try {
                                if (json.getString("error").equals("")){
                                    DialogBuilder.createDefaultDialog(AuthActivity.this, getLayoutInflater(), "Пин изменен. Отправлено уведомление", null);
                                } else {
                                    DialogBuilder.createDefaultDialog(AuthActivity.this, getLayoutInflater(), json.getString("error"), null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(int responseCode) {
                            progressBar.hide();
                            DialogBuilder.createDefaultDialog(AuthActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
                        }
                    });
                }
            }
        });


    }

    private void setLinks() {
        net1 = findViewById(R.id.play);
        net2 = findViewById(R.id.network);
        net3 = findViewById(R.id.instagram);
        net4 = findViewById(R.id.facebook);

        requestHelper.getLinks(new RequestListener() {
            @Override
            public void onComplete(JSONObject links) {
                net1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.getString("Link1")));
                            startActivity(browserIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                net2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.getString("Link2")));
                            startActivity(browserIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                net3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.getString("Link3")));
                            startActivity(browserIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                net4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.getString("Link4")));
                            startActivity(browserIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onError(int responseCode) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }

}