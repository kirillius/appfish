package com.linaverde.fishingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.CompleteActionListener;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.linaverde.fishingapp.services.DialogBuilder;
import com.linaverde.fishingapp.services.ImageHelper;
import com.linaverde.fishingapp.services.RequestHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

public class DocumentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        ImageView ivDocument = findViewById(R.id.iv_document);
        ContentLoadingProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.show();
        Bundle b = getIntent().getExtras();
        String userId = b.getString("user");
        int doc = b.getInt("doc");
        Context context = this;

        RequestHelper requestHelper = new RequestHelper(this);
        requestHelper.getDocument(userId, doc, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        ivDocument.setImageBitmap(ImageHelper.decodeToImage(json.getString("doc")));

                    } else {
                        DialogBuilder.createDefaultDialog(DocumentActivity.this, getLayoutInflater(), json.getString("error"), new CompleteActionListener() {
                            @Override
                            public void onOk(String input) {
                                finish();
                            }

                            @Override
                            public void onCancel() {
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(DocumentActivity.this, getLayoutInflater(), getString(R.string.request_error), new CompleteActionListener() {
                    @Override
                    public void onOk(String input) {
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}