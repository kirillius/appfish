package com.linaverde.fishingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.services.RequestHelper;

import org.json.JSONObject;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        RequestHelper requestHelper = new RequestHelper(this);
        requestHelper.executeGet("session", new String[]{"username", "password"}, new String[]{"judge", "123"}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                Log.d("Test auth", "Request fine");
                Bundle args = new Bundle();
                args.putString("info", json.toString());
                Intent next = new Intent(AuthActivity.this, TournamentActivity.class);
                next.putExtras(args); //Optional parameters
                startActivity(next);
                finish();
            }

            @Override
            public void onError(int responseCode) {
                Log.d("Test auth", "Request error with code" + responseCode);
            }
        });
    }
}