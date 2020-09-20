package com.example.androidsecondproject.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.androidsecondproject.viewmodel.ViewModelFactory;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentInterface, RegisterFragment.RegisterFragmentInterface,
        AccountSetupFragment.AccountSetupFragmentInterface, PreferencesFragment.PreferencesFragmentInterface, ProfilePhotoFragment.PhotoFragmentInterface, ProfileFragment.UpdateDrawerFromProfileFragment
{

    private  final  String LOGIN_FRAGMENT="login_fragment";
    private  final  String REGISTER_FRAGMENT="register_fragment";
    private  final  String ACCOUNT_SETUP_FRAGMENT="account_setup_fragment";
    private  final  String ACCOUNT_PREFERENCES_FRAGMENT="account_preferences_fragment";
    private  final  String ACCOUNT_PHOTO_FRAGMENT="account_photo_fragment";
    private  final  String ACCOUNT_PROFILE_FRAGMENT = "account_profile_fragment";
    private  final  String SWIPE_FRAGMENT = "swipe_fragment";
    private final String QUESTIONS_FRAGMENT = "questions_fragment";
    private final String MATCHES_FRAGMENT = "matches_fragment";

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private MainViewModel mViewModel;
    private CircleImageView mProfileIv;
    private TextView mNameTv;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       initializeViewComponents();
        setObservers();
        if(mViewModel.checkIfAuth()){ // from splash activity
            fetchProfileData();
        }
        else{
            moveToLoginFragment();
        }
    }

    private void initializeViewComponents() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        headerView =  navigationView.getHeaderView(0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mProfileIv=headerView.findViewById(R.id.profile_image);
        mNameTv=headerView.findViewById(R.id.username_tv);
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                handleNavigationItemSelected(menuItem.getTitle().toString());
                return false;
            }
        });

        ActionBar actionBar=getSupportActionBar();
     //   actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
    }

    private void setObservers() {
        mViewModel=new ViewModelProvider(this,new ViewModelFactory(getApplication(), eViewModels.Main)).get(MainViewModel.class);



        Observer<Profile> profileObserverSuccess=new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                Toast.makeText(MainActivity.this, "observer", Toast.LENGTH_SHORT).show();
                if(profile.getPreferences()==null){
                    moveToPreferences();
                }
                else if(mViewModel.isFirstTime()){
                    mNameTv.setText(profile.getFirstName());
                    Glide.with(MainActivity.this).load(profile.getProfilePictureUri()).error(R.drawable.man_profile).into(mProfileIv);
                    mViewModel.setFirstTime(false);
                    moveToSwipeFragment();
                }
                else {
                    mNameTv.setText(profile.getFirstName());
                    Glide.with(MainActivity.this).load(profile.getProfilePictureUri()).error(R.drawable.man_profile).into(mProfileIv);
                }
            }
        };

        Observer<String> profileObserverFail=new Observer<String>() {
            @Override
            public void onChanged(String s) {
                moveToAccountSetup();
            }
        };



        mViewModel.getProfileResultSuccess().observe(this, profileObserverSuccess);
        mViewModel.getProfileResultFailed().observe(this,profileObserverFail);




    }



    private void fetchProfileData() {
        mViewModel.getNavigationHeaderProfile();
    }

    private void handleNavigationItemSelected(String title) {
        switch (title) {
            case "Home":
                moveToSwipeFragment();
                break;
            case "My Profile":
                moveToProfileFragment();
                break;
            case "Your Matches":
                moveToMatchesFragment();
                break;
            case "Questions":
                moveToQuestionsFragment();
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



    private void moveToSwipeFragment() {
        Fragment swipeFragment = SwipeFragment.newInstance(mViewModel.getProfile());
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.flContent,swipeFragment,SWIPE_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
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
    private void moveToAccountSetup(){
        Fragment accountSetupFragment=AccountSetupFragment.newInstance();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id,accountSetupFragment,ACCOUNT_SETUP_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onMoveToAccountSetup() {
       moveToAccountSetup();
    }

    private void moveToPreferences(){
        PreferencesFragment preferencesFragment = PreferencesFragment.newInstance();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id,preferencesFragment,ACCOUNT_PREFERENCES_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void OnClickContinueToPreferences() {
        moveToPreferences();
    }

    @Override
    public void OnClickContinueToPhoto(Profile profile) {
        ProfilePhotoFragment profilePhotoFragment = ProfilePhotoFragment.newInstance(profile);
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
        Fragment preferenceFragment=fragmentManager.findFragmentByTag(ACCOUNT_PREFERENCES_FRAGMENT);
        fragmentManager.popBackStack();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.remove(preferenceFragment);
        transaction.commit();
        fetchProfileData();
    }

    @Override
    public void onLoginToApp() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.remove(loginFragment);
        transaction.commit();
        fetchProfileData();
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

    public void moveToProfileFragment()
    {
//      ProfileFragment profileFragment = ProfileFragment.newInstance(mViewModel.getProfile(),mViewModel.getPictureUri().toString());
        ProfileFragment profileFragment = ProfileFragment.newInstance(mViewModel.getProfile());
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.flContent,profileFragment,ACCOUNT_PROFILE_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void moveToQuestionsFragment() {
        QuestionsFragment questionsFragment = QuestionsFragment.newInstance(mViewModel.getProfile());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flContent, questionsFragment, QUESTIONS_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void moveToMatchesFragment() {
        MatchesFragment matchesFragment = MatchesFragment.newInstance(mViewModel.getProfile());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flContent, matchesFragment, MATCHES_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onUpdateProfile(Profile profile) {
        mViewModel.setProfile(profile);
    }
}