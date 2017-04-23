package com.syncme.dev.syncme.controllers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpController {

    public static final String TAG = "HttpController";

    private static Context mContext;
    private static HttpController instance;

    private HttpController(Context context) {
        mContext = context;
    }

    public static HttpController getInstance(Context ctx) {
        if (instance == null) {
            instance = new HttpController(ctx.getApplicationContext());
        }
        return instance;
    }

    public void executePost(String url) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "That didn't work!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                return params;
            }
        };

        // Add the request to the RequestQueue.
        VolleyController.getInstance(mContext).addToQueue(postRequest);
    }
}
