package com.linaverde.fishingapp.services;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

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
                        base64 = base64.replaceAll("/r/n", "");
                        FileOutputStream fos = null;
                        try {
                            fos = context.openFileOutput("fish_protocol.pdf", Context.MODE_PRIVATE);
                            byte[] decodedString = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);
                            fos.write(decodedString);
                            fos.flush();
                            fos.close();

                        } catch (Exception e) {

                        } finally {
                            if (fos != null) {
                                fos = null;
                            }
                        }

                        File path = new File(context.getFilesDir(), "doc");
                        File newFile = new File(path, "fish_protocol.pdf");
                        Uri contentUri = getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", newFile);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(contentUri, "application/pdf");
                        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        try {
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context,
                                    "Не обнаружено приложений для открытия PDF!",
                                    Toast.LENGTH_SHORT).show();
                        }


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
