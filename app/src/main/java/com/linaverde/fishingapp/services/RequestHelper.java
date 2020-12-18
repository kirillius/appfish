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

import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;


public class RequestHelper {
    Context context;

    public RequestHelper(Context context) {
        this.context = context;
    }

    private JSONObject getAnswerBytes(byte[] response) {
        if (response.length > 0 && response != null)
            try {
                return new JSONObject(new String(response, StandardCharsets.UTF_8));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return null;
    }

    public void executeGet(String method, String[] keys, String[] values, RequestListener listener) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        for (int i = 0; i < keys.length; i++) {
            params.put(keys[i], values[i]);
        }

        client.get(context.getResources().getString(R.string.url_backend) + "/" + method, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d("Request", method + " request successful");
                listener.onComplete(getAnswerBytes(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Request", method + " request error with code " + statusCode);
                if (errorResponse != null) {
                    listener.onError(getAnswerBytes(errorResponse));
                } else {
                    listener.onError(null);
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    public void executePost(String method, String[] keys, String[] values, RequestListener listener) {
        AsyncHttpClient client = new AsyncHttpClient();

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
                listener.onError(getAnswerBytes(errorResponse));
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

}
