package com.linaverde.fishingapp.services;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.core.widget.ContentLoadingProgressBar;

import com.linaverde.fishingapp.BuildConfig;
import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.activities.AuthActivity;
import com.linaverde.fishingapp.activities.DocumentActivity;
import com.linaverde.fishingapp.activities.RegisterTeamActivity;
import com.linaverde.fishingapp.activities.WeightingActivity;
import com.linaverde.fishingapp.fragments.WeightingStagesFragment;
import com.linaverde.fishingapp.interfaces.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

import static androidx.core.content.FileProvider.getUriForFile;

public class ProtocolHelper {

    public static void sendProtocols(Context context, String matchId, ContentLoadingProgressBar progressBar) {
        RequestHelper requestHelper = new RequestHelper(context);
        progressBar.show();
        requestHelper.executeGet("sendprotocols", new String[]{"match"}, new String[]{matchId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        DialogBuilder.createDefaultDialog(context, LayoutInflater.from(context), context.getString(R.string.protocol_sent), null);
                    } else {
                        DialogBuilder.createDefaultDialog(context, LayoutInflater.from(context), json.getString("error"), null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(context, LayoutInflater.from(context), context.getString(R.string.request_error), null);
            }
        });
    }

    public static void getProtocol(Context context, String matchId, ContentLoadingProgressBar progressBar) {

        RequestHelper requestHelper = new RequestHelper(context);
        progressBar.show();
        requestHelper.executeGet("getprotocols", new String[]{"match"}, new String[]{matchId}, new RequestListener() {
            @Override
            public void onComplete(JSONObject json) {
                progressBar.hide();
                try {
                    if (json.getString("error").equals("") || json.getString("error").equals("null") || json.isNull("error")) {
                        String base64 = json.getString("doc");
                        //base64 = base64.replaceAll("/r/n", "");
                        String root = context.getFilesDir().toString();

                        Log.d("ResponseEnv", root);

                        File myDir = new File(root);
                        if (!myDir.exists()) {
                            myDir.mkdirs();
                        }


                        String fname = "fishProtocol.pdf";
                        File file = new File(myDir, fname);
                        if (file.exists())
                            file.delete();
                        try {

                            FileOutputStream out = new FileOutputStream(file);
                            byte[] pdfAsBytes = Base64.decode(base64, 0);
                            out.write(pdfAsBytes);
                            out.flush();
                            out.close();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        File imgFile = new File(myDir, fname);
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);

                        Uri uri;
                        if (Build.VERSION.SDK_INT < 24) {
                            uri = Uri.fromFile(file);
                        } else {
                            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", imgFile);
                        }

                        sendIntent.setDataAndType(uri, "application/pdf");
                        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(sendIntent);


                    } else {
                        DialogBuilder.createDefaultDialog(context, LayoutInflater.from(context), json.getString("error"), null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int responseCode) {
                progressBar.hide();
                DialogBuilder.createDefaultDialog(context, LayoutInflater.from(context), context.getString(R.string.request_error), null);
            }
        });

    }
}
