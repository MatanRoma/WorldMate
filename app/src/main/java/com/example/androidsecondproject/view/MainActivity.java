package com.example.androidsecondproject.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.MainViewModel;
import com.example.androidsecondproject.viewmodel.RegisterViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentInterface, RegisterFragment.RegisterFragmentInterface, AccountSetupFragment.AccountSetupFragmentInterface, PreferencesFragment.PreferencesFragmentInterface,
                                                     ProfilePhotoFragment.PhotoFragmentInterface
{

    private  final  String LOGIN_FRAGMENT="login_fragment";
    private  final  String REGISTER_FRAGMENT="register_fragment";
    private  final  String ACCOUNT_SETUP_FRAGMENT="account_setup_fragment";
    private  final  String ACCOUNT_PREFERENCES_FRAGMENT="account_preferences_fragment";
    private  final  String ACCOUNT_PHOTO_FRAGMENT="account_photo_fragment";

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private MainViewModel mViewModel;
    private CircleImageView mProfileIv;
    private TextView mNameTv;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    View headerView;



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().hasExtra("is_logged_in")){
            settingUpMainActivity();
        }
        else{
            moveToLoginFragment();
        }








     //   loginFragment.setCancelable(false);

       /* loginFragment=loginFragment.newInstance();
        loginFragment.show(getSupportFragmentManager(),null);
        loginFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogFragmentTheme);*/

    }

    @SuppressLint("RestrictedApi")
    private void settingUpMainActivity() {

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        headerView =  navigationView.getHeaderView(0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mProfileIv=headerView.findViewById(R.id.profile_image);
        mNameTv=headerView.findViewById(R.id.username_tv);
        setSupportActionBar(toolbar);

        mViewModel=new ViewModelProvider(this,new ViewModelFactory(getApplication(), eViewModels.Main)).get(MainViewModel.class);
        Observer<Profile> profileObserver=new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                Toast.makeText(MainActivity.this, profile.getFirstName(), Toast.LENGTH_SHORT).show();
                mNameTv.setText(profile.getFirstName());
            }
        };
        Observer<Uri> pictureObserver=new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                Glide.with(MainActivity.this).load(uri).into(mProfileIv);
            }
        };
        mViewModel.getDownloadResultSuccess().observe(this, pictureObserver);
        mViewModel.getProfileResultSuccess().observe(this, profileObserver);

        mViewModel.getNavigationHeaderProfile();
        mViewModel.getNavigationHeaderImage();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                handleNavigationItemSelected(menuItem.getTitle().toString());
                return false;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void handleNavigationItemSelected(String title) {

        switch (title) {
            case "Home":
                break;
            case "My Profile":
                break;
            case "Your Matches":
                break;
            case "Messages":
                break;
            case "Settings":
                break;
            case "Logout":
                mViewModel.logout();
                moveToLoginFragment();
        }

    }

    private void moveToLoginFragment(){
        loginFragment = loginFragment.newInstance();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id,loginFragment,LOGIN_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
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
    public void OnClickContinueToPreferences() {
        PreferencesFragment preferencesFragment = PreferencesFragment.newInstance();

      /*  Bundle bundle = new Bundle();
        bundle.putString("first_name",firstName);
        bundle.putString("last_name",lastName);
        bundle.putString("gender",gender);
        bundle.putIntegerArrayList("date",date);
        preferencesFragment.setArguments(bundle);*/

        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id,preferencesFragment,ACCOUNT_PREFERENCES_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();



    }

    @Override
    public void OnClickContinueToPhoto() {
        ProfilePhotoFragment profilePhotoFragment = ProfilePhotoFragment.newInstance();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id,profilePhotoFragment,ACCOUNT_PHOTO_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void OnClickContinueToApp() {
        //TODO
        FragmentManager fragmentManager=getSupportFragmentManager();
        Fragment prefernceFragment=fragmentManager.findFragmentByTag(ACCOUNT_PREFERENCES_FRAGMENT);
        fragmentManager.popBackStack();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.remove(prefernceFragment);
        transaction.commit();
        settingUpMainActivity();


    }

    @Override
    public void onLoginToApp() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.remove(loginFragment);
        transaction.commit();
        settingUpMainActivity();

    }


    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            drawerLayout.openDrawer(Gravity.START);
        }
        return super.onOptionsItemSelected(item);
    }
}