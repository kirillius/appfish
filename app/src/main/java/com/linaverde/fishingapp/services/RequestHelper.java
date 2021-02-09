package com.linaverde.fishingapp.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import com.linaverde.fishingapp.R;
import com.linaverde.fishingapp.interfaces.RequestListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class RequestHelper {
    Context context;

    public RequestHelper(Context context) {
        this.context = context;

    }

    private JSONObject getAnswerBytes(byte[] response) {
        if (response.length > 0)
            try {
                return new JSONObject(new String(response, StandardCharsets.UTF_8));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return null;
    }

    public void updateMatchStatus(UserInfo userInfo, RequestListener listener) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", userInfo.getLogin());
        params.put("password", userInfo.getPassword());

//        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Log.d("Request", " status init");
        client.get(context.getResources().getString(R.string.url_backend) + "/session", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d("Request", " status completed");
//                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                JSONObject json = getAnswerBytes(response);
                try {
                    userInfo.setStatus(json.getBoolean("isCheckInClosed"), json.getBoolean("isQueueClosed"), json.getBoolean("isSectorClosed"));
                    if (listener != null)
                        listener.onComplete(getAnswerBytes(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                if (errorResponse != null) {
                    String res = new String(errorResponse, StandardCharsets.UTF_8);
                    Log.d("Request", "status request error with code " + statusCode + " with text "+ res);
                    if (listener != null)
                        listener.onError(statusCode);
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public void getDocument(String userId, int doc, RequestListener listener) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("doc", doc);

        Log.d("Request", "init get documents");

        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        client.get(context.getResources().getString(R.string.url_backend) + "/docs", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d("Request", "docs request successful");
                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                listener.onComplete(getAnswerBytes(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Request", "docs request error with code " + statusCode);
                if (errorResponse.length > 0) {
                    String res = new String(errorResponse, StandardCharsets.UTF_8);
                }
                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                listener.onError(statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }


    public void executeGet(String method, String[] keys, String[] values, RequestListener listener) {
        AsyncHttpClient client = new AsyncHttpClient();
        String logParam = "";
        RequestParams params = new RequestParams();
        if (keys != null)
            for (int i = 0; i < keys.length; i++) {
                params.put(keys[i], values[i]);
                logParam += keys[i] + "=" + values[i] + ";";
            }
        Log.d("Request", "init " + method + " get request with params " + logParam);

        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        client.get(context.getResources().getString(R.string.url_backend) + "/" + method, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d("Request", method + " request successful");
                Log.d("Request", "answer: " + new String(response, StandardCharsets.UTF_8));
                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                listener.onComplete(getAnswerBytes(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Request", method + " request error with code " + statusCode);
                if (errorResponse != null) {
                    String res = new String(errorResponse, StandardCharsets.UTF_8);
                }
                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                listener.onError(statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    public void executePost(String method, String[] keys, String[] values, String json, RequestListener listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("Request", "init " + method + " post request");

        String url = context.getResources().getString(R.string.url_backend) + "/" + method + "?";
        for (int i = 0; i < keys.length; i++) {
            url += keys[i] + "=" + values[i];
            if (i < keys.length - 1) {
                url += "&";
            }
        }

        StringEntity entity = null;
        if (json != null) {
            try {
                entity = new StringEntity(json);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        client.post(context, url, entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d("Request", method + " post request successful");
                Log.d("Request", getAnswerBytes(response).toString());
                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                listener.onComplete(getAnswerBytes(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Request", method + " post request failed");
                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                listener.onError(statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public void getWeather(double latitude, double longitude, RequestListener listener){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.gismeteo.net/v2/weather/forecast/";
        RequestParams params = new RequestParams();
        params.put("lang", "ru");
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("days", 1);
        //client.addHeader("X-Gismeteo-Token", "56b30cb255.3443075");
        client.get(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("Request", "weather get request successful");
                Log.d("Request", getAnswerBytes(response).toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Request", "weather get request failed with code " + statusCode);
                if (responseBody.length > 0) {
                    String res = new String(responseBody, StandardCharsets.UTF_8);
                    Log.d("Request", "weather error " + res);
                }
            }
        });
    }

}
