package com.linaverde.fishingapp.services;

import android.content.Context;
import android.util.Log;

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

    public void getDocument(String userId, int doc, RequestListener listener){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("doc", doc);

        Log.d("Request", "init get documents");

        client.get(context.getResources().getString(R.string.url_backend) + "/docs", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d("Request", "docs request successful");
                listener.onComplete(getAnswerBytes(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Request", "docs request error with code " + statusCode);
                if (errorResponse.length>0) {
                    String res = new String(errorResponse, StandardCharsets.UTF_8);
                }
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
        for (int i = 0; i < keys.length; i++) {
            params.put(keys[i], values[i]);
            logParam += keys[i] + "=" + values[i]+";";
        }
        Log.d("Request", "init " + method + " get request with params " + logParam);

        client.get(context.getResources().getString(R.string.url_backend) + "/" + method, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d("Request", method + " request successful");
                Log.d("Request", "answer: " + new String(response, StandardCharsets.UTF_8));
                listener.onComplete(getAnswerBytes(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Request", method + " request error with code " + statusCode);
                if (errorResponse.length>0) {
                    String res = new String(errorResponse, StandardCharsets.UTF_8);
                }
                listener.onError(statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    public void executePost(String method, String[] keys, String[] values, RequestListener listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("Request", "init " + method + " post request");

        RequestParams params = new RequestParams();
        for (int i = 0; i < keys.length; i++) {
            params.put(keys[i], values[i]);
        }

        client.post(context.getResources().getString(R.string.url_backend) + "/" + method, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                listener.onComplete(getAnswerBytes(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                listener.onError(statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

}
