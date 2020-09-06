package com.example.androidsecondproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // final Button btn = findViewById(R.id.tst);

        final Button manBtn = findViewById(R.id.man_btn);
        final Button womanBtn = findViewById(R.id.woman_btn);

        manBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manBtn.setSelected(true);
                womanBtn.setSelected(false);
            }
        });

        womanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manBtn.setSelected(false);
                womanBtn.setSelected(true);
            }
        });


        requestWithSomeHttpHeaders();




    }


    public void requestWithSomeHttpHeaders() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://footballpool.dataaccess.eu/info.wso/GameScore";
        StringRequest getRequest = new StringRequest(StringRequest.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //Toast.makeText(MainActivity.this, "tst", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","error => "+error.toString());

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");


                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {


                return "{\"iGameNumber\":5}".getBytes();

            }

        };
        queue.add(getRequest);

    }
}