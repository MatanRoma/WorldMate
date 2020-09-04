package com.example.androidsecondproject.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidsecondproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentInterface, RegisterFragment.RegisterFragmentInterface {

    private  final  String LOGIN_FRAGMENT="login_fragment";
    private  final  String REGISTER_FRAGMENT="register_fragment";


    LoginFragment loginFragment;
    RegisterFragment registerFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loginFragment = loginFragment.newInstance();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id,loginFragment,LOGIN_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
        loginFragment.setCancelable(false);


    }


    @Override
    public void onClickMoveToRegister() {
        registerFragment=RegisterFragment.newInstance();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id,registerFragment,REGISTER_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
     //   registerFragment.setCancelable(false);
    }

    @Override
    public void onClickMoveToLogin() {
        loginFragment=LoginFragment.newInstance();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id,loginFragment,LOGIN_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}