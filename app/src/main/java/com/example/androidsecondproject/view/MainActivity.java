package com.example.androidsecondproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidsecondproject.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentInterface, RegisterFragment.RegisterFragmentInterface, AccountSetupFragment.AccountSetupFragmentInterface, PreferencesFragment.PreferencesFragmentInterface {

    private  final  String LOGIN_FRAGMENT="login_fragment";
    private  final  String REGISTER_FRAGMENT="register_fragment";
    private  final  String ACCOUNT_SETUP_FRAGMENT="account_setup_fragment";
    private  final  String ACCOUNT_PREFERENCES_FRAGMENT="account_preferences_fragment";
    private  final  String ACCOUNT_PHOTO_FRAGMENT="account_photo_fragment";

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

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
     //   loginFragment.setCancelable(false);

       /* loginFragment=loginFragment.newInstance();
        loginFragment.show(getSupportFragmentManager(),null);
        loginFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogFragmentTheme);*/

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

    /*    loginFragment.dismiss();
        registerFragment=registerFragment.newInstance();
        registerFragment.show(getSupportFragmentManager(),null);
        registerFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogFragmentTheme);*/
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

        /*registerFragment.dismiss();
        loginFragment=loginFragment.newInstance();
        loginFragment.show(getSupportFragmentManager(),null);
        loginFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogFragmentTheme);*/
    }

    @Override
    public void onMoveToNameSetup(String uid) {
        Fragment accountSetupFragment=AccountSetupFragment.newInstance();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id,accountSetupFragment,ACCOUNT_SETUP_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void OnClickContinueToPreferences(String firstName, String lastName, String gender, ArrayList<Integer> date) {
        PreferencesFragment preferencesFragment = PreferencesFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("first_name",firstName);
        bundle.putString("last_name",lastName);
        bundle.putString("gender",gender);
        bundle.putIntegerArrayList("date",date);
        preferencesFragment.setArguments(bundle);

        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id,preferencesFragment,ACCOUNT_PREFERENCES_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();



    }

    @Override
    public void OnClickContinueToPhoto(Bundle bundle) {
        ProfilePhotoFragment profilePhotoFragment = ProfilePhotoFragment.newInstance();
        profilePhotoFragment.setArguments(bundle);
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id,profilePhotoFragment,ACCOUNT_PHOTO_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String result = getIntent().getStringExtra("activity_result");
        Toast.makeText(this, "result in main activity", Toast.LENGTH_SHORT).show();
    }
}