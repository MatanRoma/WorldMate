package com.example.androidsecondproject.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidsecondproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationManager {

    public static void sendNotification(Context context,final JSONObject rootObject){


        final String url = "https://fcm.googleapis.com/fcm/send";
        final String kkk="AAAABbrOweI:APA91bEnV1kmFrDiPTZrN_tGxxi8H8rbwiohInI2qa1WUyqHR_MSxZOMSeZ_DQ5zwJS6HZiSDjM-j_14zgeXdDI7FfTdgqYLCpc3HKohtRMRIFhQafmJ19X_znhIkcRkZ9fq4x4YGI41";

        RequestQueue queue= Volley.newRequestQueue(context);
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers=new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Authorization","key="+kkk);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return rootObject.toString().getBytes();
            }
        };
        queue.add(request);
        queue.start();


    }

}
