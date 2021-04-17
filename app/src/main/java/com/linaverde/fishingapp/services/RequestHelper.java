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
import java.util.Arrays;

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

    public void getLinks(RequestListener listener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(context.getResources().getString(R.string.url_backend) + "/links", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d("Request", "links request successful");
                Log.d("Request", "answer: " + new String(response, StandardCharsets.UTF_8));
                listener.onComplete(getAnswerBytes(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Request", "links request error with code " + statusCode);
//                if (errorResponse != null) {
//                    String res = new String(errorResponse, StandardCharsets.UTF_8);
//                }
                listener.onError(statusCode);
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
//                if (errorResponse.length > 0) {
//                    String res = new String(errorResponse, StandardCharsets.UTF_8);
//                }
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
                //Log.d("Request", method + " post request failed with error " + new String(errorResponse, StandardCharsets.UTF_8));
                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                listener.onError(statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    public void executeDelete(String method, String[] keys, String[] values, RequestListener listener){
        AsyncHttpClient client = new AsyncHttpClient();
        String logParam = "";
        RequestParams params = new RequestParams();
        if (keys != null)
            for (int i = 0; i < keys.length; i++) {
                params.put(keys[i], values[i]);
                logParam += keys[i] + "=" + values[i] + ";";
            }
        Log.d("DELETE", "init " + method + " get request with params " + logParam);

        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        client.delete(context.getResources().getString(R.string.url_backend) + "/" + method, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d("DELETE", method + " request successful");
                Log.d("DELETE", "answer: " + new String(response, StandardCharsets.UTF_8));
                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                listener.onComplete(getAnswerBytes(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Request", method + " request error with code " + statusCode);
                //Log.d("Request", method + " post request failed with error " + new String(errorResponse, StandardCharsets.UTF_8));
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
        String logParam = "";
        RequestParams params = new RequestParams();
        if (keys != null)
            for (int i = 0; i < keys.length; i++) {
                params.put(keys[i], values[i]);
                logParam += keys[i] + "=" + values[i] + ";";
            }
        Log.d("Request", "init " + method + " post request with params " + logParam);
        if (json != null) {
            Log.d("Request", json);
        }

        if (json != null) {
            Log.d("Request", method + " request json " + json);
        }

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
                Log.d("Request", method + " post request failed with code " + statusCode);
                //Log.d("Request", method + " post request failed with error " + new String(errorResponse, StandardCharsets.UTF_8));
                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                listener.onError(statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public void getWeather(double latitude, double longitude, RequestListener listener) {
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
//                if (responseBody.length > 0) {
//                    String res = new String(responseBody, StandardCharsets.UTF_8);
//                    Log.d("Request", "weather error " + res);
//                }
            }
        });
    }

}
