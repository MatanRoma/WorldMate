package com.example.androidsecondproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Intent intent=new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);*/
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= mAuth.getCurrentUser();
        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
        if(firebaseUser!=null) {
            Toast.makeText(SplashActivity.this, firebaseUser.getUid(), Toast.LENGTH_SHORT).show();
            intent.putExtra("is_logged_in",true);
        }
        else{
            Toast.makeText(SplashActivity.this, "User NOT!! EXISTS", Toast.LENGTH_SHORT).show();
            intent.putExtra("is_logged_in",false);
        }
        startActivity(intent);
        finish();

        /*mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();

               if(firebaseUser!=null) {
                   Toast.makeText(SplashActivity.this, firebaseUser.toString(), Toast.LENGTH_SHORT).show();
               }
               else{
                   Toast.makeText(SplashActivity.this, "User NOT!! EXISTS", Toast.LENGTH_SHORT).show();

               }
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);


            }
        };*/

        //mAuth.addAuthStateListener(mAuthStateListener);

    }
}
