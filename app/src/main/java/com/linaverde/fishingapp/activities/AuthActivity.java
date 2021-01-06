package com.linaverde.fishingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.RequestHelper;
import com.linaverde.fishingapp.services.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthActivity extends AppCompatActivity {

    EditText login, password;
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        login = findViewById(R.id.et_login);
        password = findViewById(R.id.et_password);
        signIn = findViewById(R.id.button_sign_in);

        UserInfo userInfo = new UserInfo(this);
        userInfo.clearUserInfo();

        RequestHelper requestHelper = new RequestHelper(this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sLogin, sPassword;
                sLogin = login.getText().toString();
                sPassword = password.getText().toString();
                if (!sLogin.equals("") && !sPassword.equals("")) {
                    requestHelper.executeGet("session", new String[]{"username", "password"}, new String[]{sLogin, sPassword}, new RequestListener() {
                        @Override
                        public void onComplete(JSONObject json) {
                            Log.d("Test auth", "Request fine");
                            try {
                                userInfo.saveUser(sLogin, sPassword, json.getString("userName"), json.getInt("userType"), json.getString("pond"),
                                        json.getString("matchId"), json.getString("matchName"), json.getString("teamId"));
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
                            if (responseCode == 401) {
                                DialogBuilder.createDefaultDialog(AuthActivity.this, getLayoutInflater(), getString(R.string.login_error), null);
                            } else {
                                DialogBuilder.createDefaultDialog(AuthActivity.this, getLayoutInflater(), getString(R.string.request_error), null);
                            }
                        }
                    });
                } else {
                    DialogBuilder.createDefaultDialog(AuthActivity.this, getLayoutInflater(), "Заполните логин и парль", null);
                }
            }
        });


    }
}