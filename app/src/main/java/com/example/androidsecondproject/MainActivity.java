package com.example.androidsecondproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity implements PreferencesFragment.PreferencesFragmentInterface {

    private  final  String PREFERENCE_FRAGMENT="preferences_fragment";
    private  final  String DESCRIPTION_FRAGMENT="description_fragment";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //moveToPreferences();
        moveToDescription();


       // final Button btn = findViewById(R.id.tst);

        final Button manBtn = findViewById(R.id.man_btn);
        final Button womanBtn = findViewById(R.id.woman_btn);

        /*manBtn.setOnClickListener(new View.OnClickListener() {
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
*/


        requestWithSomeHttpHeaders();





        /*final Button editBtn = findViewById(R.id.edit_btn);
        final Button saveBtn = findViewById(R.id.save_btn);
        final TextView descriptionTv = findViewById(R.id.description_tv);
        final EditText descriptionEt = findViewById(R.id.description_et);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descriptionTv.setVisibility(View.GONE);
                descriptionEt.setVisibility(View.VISIBLE);
                descriptionEt.setText(descriptionTv.getText().toString());
                editBtn.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descriptionTv.setVisibility(View.VISIBLE);
                descriptionEt.setVisibility(View.GONE);
                descriptionTv.setText(descriptionEt.getText().toString());
                editBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.GONE);
            }
        });*/






    }

    public void moveToPreferences()
    {
        PreferencesFragment preferencesFragment = PreferencesFragment.newInstance();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id,preferencesFragment,PREFERENCE_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void moveToDescription()
    {
        DescriptionFragment descriptionFragment = DescriptionFragment.newInstance();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id,descriptionFragment,DESCRIPTION_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
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

    @Override
    public void OnClickContinue() {

    }
}