package com.linaverde.fishingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.services.ImageHelper;

public class DocumentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        ImageView ivDocument = findViewById(R.id.iv_document);
        Bundle b = getIntent().getExtras();
        ivDocument.setImageBitmap(ImageHelper.decodeToImage(b.getString("image")));
    }
}