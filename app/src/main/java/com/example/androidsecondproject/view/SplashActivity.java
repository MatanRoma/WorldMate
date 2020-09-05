package com.example.androidsecondproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.SplashViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

public class SplashActivity extends AppCompatActivity {

 /*   private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthStateListener;*/
    private SplashViewModel mViewModel;
    Intent mIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
       /* mAuth = FirebaseAuth.getInstance();
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
        finish();*/
        mViewModel = new ViewModelProvider(this,new ViewModelFactory(getApplication(), eViewModels.Splash)).get(SplashViewModel.class);
        mIntent = new Intent(SplashActivity.this,MainActivity.class);

        final Observer<Boolean> authObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean isAuth) {

                if(isAuth){
                    String uid=mViewModel.getUserUid();
                    mIntent.putExtra("uid",uid);
                    Toast.makeText(SplashActivity.this, uid, Toast.LENGTH_SHORT).show();
                }
                /*else{
                    mIntent.putExtra("is_logged_in",false);
                }*/
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        startActivity(mIntent);
                        finish();
                    }
                }, 1000);

            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.getIsAuth().observe(this, authObserver);

      //  mModel.getIsAuth().setValue(true);



    }
}
