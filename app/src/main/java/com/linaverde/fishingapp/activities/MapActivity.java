package com.linaverde.fishingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.services.UserInfo;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        UserInfo userInfo = new UserInfo(this);
        
    }
}