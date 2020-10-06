package com.example.androidsecondproject.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.LocationPoint;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.LocationViewModel;
import com.example.androidsecondproject.viewmodel.MainViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentInterface, RegisterFragment.RegisterFragmentInterface,
        AccountSetupFragment.AccountSetupFragmentInterface, PreferencesFragment.PreferencesFragmentInterface, ProfilePhotoFragment.PhotoFragmentInterface,
        ProfileFragment.UpdateDrawerFromProfileFragment,MatchesFragment.OnMoveToChat, SwipeFragment.SwipeInterface, ChatFragment.OnMoveToProfilePreviewFromChat,
        ProfilePreviewFragment.OnMoveToPhotoPreview, NewMatchDialogFragment.OnNewMatchDialogFragmentDialofListener, LikesFragment.LikesFragmentInterface {
    private final int REQUEST_CHECK_SETTINGS =2 ;
    private Geocoder mGeoCoder;
    private String mCityName;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    int LOCATION_REQUEST = 1;

    private BroadcastReceiver tokenReceiver;
    private final String LOGIN_FRAGMENT = "login_fragment";
    private final String REGISTER_FRAGMENT = "register_fragment";
    private final String ACCOUNT_SETUP_FRAGMENT = "account_setup_fragment";
    private final String ACCOUNT_PREFERENCES_FRAGMENT = "account_preferences_fragment";
    private final String ACCOUNT_PHOTO_FRAGMENT = "account_photo_fragment";
    private final String ACCOUNT_PROFILE_FRAGMENT = "account_profile_fragment";
    private final String SWIPE_FRAGMENT = "swipe_fragment";
    private final String QUESTIONS_FRAGMENT = "questions_fragment";
    private final String MATCHES_FRAGMENT = "matches_fragment";
    private final String CHAT_FRAGMENT = "chat_fragment";
    private final String PROFILE_PREVIEW_FRAGMENT = "profile_preview_fragment";
    private final String PHOTO_PREVIEW_FRAGMENT = "photo_preview_fragment";
    private  final String SETTINGS_FRAGMENT="settings_fragment";
    private final String LIKES_FRAGMENT="likes_fragment";

    private final String STACK ="secondary_stack";

    private MainViewModel mViewModel;
    private CircleImageView mProfileIv;
    private TextView mNameTv;
    private Toolbar mToolbar;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;
    Observer<Location> mLocationObserver;



    private AlertDialog show;




  //  private ImageView loaderIv;
    private SpinKitView mLoadingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   loaderIv=findViewById(R.id.main_loader);
        mLoadingAnimation =findViewById(R.id.spin_kit);
        Log.d("notifi2",savedInstanceState+"");

        initializeViewComponents();
        setObservers();
        onMessageTokenReceived();
        navigationView.getMenu().getItem(0).setChecked(true);







        if (mViewModel.checkIfAuth()) { // from splash activity
            fetchProfileData();
        } else {
            moveToLoginFragment();
        }
    }

    private void initializeViewComponents() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        headerView = navigationView.getHeaderView(0);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setVisibility(View.GONE);
        mProfileIv = headerView.findViewById(R.id.profile_image);
        mNameTv = headerView.findViewById(R.id.username_tv);
        setSupportActionBar(mToolbar);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(!mViewModel.isLoginAsGuest())
                    menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                handleNavigationItemSelected(menuItem.getTitle().toString());
                return false;
            }
        });

        ActionBar actionBar = getSupportActionBar();


        //   actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
    }

    private void setObservers() {
        mViewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication(), eViewModels.Main)).get(MainViewModel.class);


        Observer<Profile> profileObserverSuccess = new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                Log.d("observer","out");
              //  mToolbar.setVisibility(View.VISIBLE);
                mViewModel.setToken();
                if (profile.getPreferences() == null) {
                    moveToPreferences();
                   // loaderIv.setVisibility(View.GONE);
                    mLoadingAnimation.setVisibility(View.GONE);
                } else if (mViewModel.isFirstTime()) {
                    mNameTv.setText(profile.getFirstName());
                    Glide.with(MainActivity.this).load(profile.getProfilePictureUri()).error(R.drawable.man_profile).into(mProfileIv);
                    getTokenWhenLogin();
                    mViewModel.setFirstTime(false);
                    handleReturnFromNotif();
                  // loaderIv.setVisibility(View.GONE);
                    mLoadingAnimation.setVisibility(View.GONE);

                } else {
                    Log.d("observer","in");
                    mNameTv.setText(profile.getFirstName());
                    Glide.with(MainActivity.this).load(profile.getProfilePictureUri()).error(R.drawable.man_profile).into(mProfileIv);
                }
            }


        };

        Observer<String> profileObserverFail = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                moveToAccountSetup();
            }
        };
        Observer<Profile> otherProfileObserverSuccess = new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                String chatId;
                SwipeFragment swipeFragment = (SwipeFragment) getSupportFragmentManager().findFragmentByTag(SWIPE_FRAGMENT);
                if (swipeFragment == null || !swipeFragment.isVisible()) {
                    Log.d("call","6");
                    moveToSwipeFragment();
                    chatId=getIntent().getStringExtra("chat_id");
                    chatId=getIntent().getAction().substring(3);

                }
                else{
//                   chatId  = getIntent().getExtras().getString("chat_id");
                    chatId=getIntent().getAction().substring(3);
                   Log.d("other_match_uid",chatId+"");
                }
                moveToChat(mViewModel.getMyProfile(), profile, chatId); // get  here only from notification
            }
        };

        mViewModel.getOtherProfileResultSuccess().observe(this, otherProfileObserverSuccess);
        mViewModel.getProfileResultSuccess().observe(this, profileObserverSuccess);
        mViewModel.getProfileResultFailed().observe(this, profileObserverFail);
    }


    private void fetchProfileData() {
        //loaderIv.setVisibility(View.VISIBLE);
        Log.d("login","login");
        mLoadingAnimation.setVisibility(View.VISIBLE);
        mViewModel.getNavigationHeaderProfile();
    }

    private void handleNavigationItemSelected(String title) {
        if(mViewModel.isLoginAsGuest()&&!title.equals(getString(R.string.home))&&!title.equals(getString(R.string.logout))){
            guestNotAllowedDialog(false);
        }
        else {
            if (getString(R.string.home).equals(title)) {
                clearStack(null);
                setTitle(R.string.app_name);
            } else if (getString(R.string.my_profile).equals(title)) {
                ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag(ACCOUNT_PROFILE_FRAGMENT);
                if (profileFragment == null || !profileFragment.isVisible()) {
                    moveToProfileFragment();
                }
            } else if (getString(R.string.your_matches).equals(title)) {
                MatchesFragment matchesFragment = (MatchesFragment) getSupportFragmentManager().findFragmentByTag(MATCHES_FRAGMENT);
                if (matchesFragment == null || !matchesFragment.isVisible()) {
                    moveToMatchesFragment();
                }
            }else if("Your Likes".equals(title)){
                LikesFragment likesFragment = (LikesFragment) getSupportFragmentManager().findFragmentByTag(LIKES_FRAGMENT);
                if (likesFragment == null || !likesFragment.isVisible()) {
                    moveToLikesFragment();
                }
            }
            else if (getString(R.string.questions).equals(title)) {
                QuestionsFragment questionsFragment = (QuestionsFragment) getSupportFragmentManager().findFragmentByTag(QUESTIONS_FRAGMENT);
                if (questionsFragment == null || !questionsFragment.isVisible()) {
                    moveToQuestionsFragment();
                }
            } else if (getString(R.string.messages).equals(title)) {
            } else if (getString(R.string.settings).equals(title)) {
                moveToSettingFragment();
            } else if (getString(R.string.logout).equals(title)) {
                logoutUser();
            }
        }
    }

    private void moveToLikesFragment() {
        LikesFragment likesFragment = LikesFragment.newInstance(mViewModel.getMyProfile());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flContent,likesFragment,LIKES_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
        setTitle("Your Likes");
    }

    private void logoutUser(){
        if (!mViewModel.isLoginAsGuest()) {
           mViewModel.removeProfileListener();
            mViewModel.setToken("");
            LocationViewModel.getInstance(getApplicationContext()).removeObservers(this);
         //   mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);

            mViewModel.logout();
            mViewModel.setFirstTime(true);
            mViewModel.setFirstLocation(true);
            getIntent().setAction("");
        } else {
            mViewModel.setLoginAsGuest(false);
            navigationView.getMenu().getItem(5).setChecked(true);

        }
        clearStack(null);
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag(SWIPE_FRAGMENT)).commit();
        moveToLoginFragment();
    }


    private void moveToSwipeFragment() {
        Log.d("login","swipe");
        Fragment swipeFragment;
        if(!mViewModel.isLoginAsGuest()) {
            swipeFragment = SwipeFragment.newInstance(mViewModel.getMyProfile());
        }
        else {
            swipeFragment=SwipeFragment.newInstance();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flContent_second, swipeFragment, SWIPE_FRAGMENT);
        transaction.commit();
        setTitle(R.string.app_name);
        Log.d("call","mainActivity");

    }

    private void moveToLoginFragment() {
         LoginFragment loginFragment = LoginFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id, loginFragment, LOGIN_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void onClickMoveToRegister() {
        RegisterFragment registerFragment = RegisterFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_id, registerFragment, REGISTER_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void onClickMoveToLogin() {
        LoginFragment loginFragment = LoginFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.main_activity_id, loginFragment, LOGIN_FRAGMENT);
        transaction.replace(R.id.main_activity_id,fragmentManager.findFragmentByTag(LOGIN_FRAGMENT),LOGIN_FRAGMENT);

        transaction.commit();
    }

    private void moveToAccountSetup() {
        Fragment accountSetupFragment = AccountSetupFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
      //  transaction.add(R.id.main_activity_id, accountSetupFragment, ACCOUNT_SETUP_FRAGMENT);
        transaction.replace(R.id.main_activity_id,accountSetupFragment,ACCOUNT_SETUP_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void onMoveToAccountSetup() {
        moveToAccountSetup();
    }

    private void moveToPreferences() {
        PreferencesFragment preferencesFragment = PreferencesFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        /*transaction.add(R.id.main_activity_id, preferencesFragment, ACCOUNT_PREFERENCES_FRAGMENT);
        Fragment photoFragment = fragmentManager.findFragmentByTag(ACCOUNT_PHOTO_FRAGMENT);
        if(photoFragment!=null)
            transaction.remove(photoFragment);*/
        transaction.replace(R.id.main_activity_id,preferencesFragment,ACCOUNT_PREFERENCES_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void OnClickContinueToPreferences() {
        moveToPreferences();
    }

    @Override
    public void OnClickContinueToPhoto(Profile profile) {
        ProfilePhotoFragment profilePhotoFragment = ProfilePhotoFragment.newInstance(profile);
        FragmentManager fragmentManager = getSupportFragmentManager();
     //   fragmentManager.popBackStack();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
       // transaction.add(R.id.main_activity_id, profilePhotoFragment, ACCOUNT_PHOTO_FRAGMENT);
        transaction.replace(R.id.main_activity_id,profilePhotoFragment,ACCOUNT_PHOTO_FRAGMENT);
    //    transaction.addToBackStack(STACK);
        transaction.commit();
    }

    @Override
    public void OnClickContinueToApp() {
        //TODO
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment preferenceFragment = fragmentManager.findFragmentByTag(ACCOUNT_PREFERENCES_FRAGMENT);
     //   fragmentManager.popBackStack();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(preferenceFragment);
        transaction.commit();
        navigationView.getMenu().getItem(0).setChecked(true);
        fetchProfileData();
    }

    @Override
    public void onLoginToApp() {
        FragmentManager fragmentManager = getSupportFragmentManager();
       // fragmentManager.popBackStack();
      /*  Fragment loginFragment = fragmentManager.findFragmentByTag(LOGIN_FRAGMENT);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(loginFragment);
        transaction.commit();*/
      //  clearStack(null);

     fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(LOGIN_FRAGMENT)).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
        fetchProfileData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_filter,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(Gravity.START);
        }
        if(item.getItemId() == R.id.filter_id){
            if(!mViewModel.isLoginAsGuest()) {
                openDialogFragment();
            }
            else{
                guestNotAllowedDialog(true);
            }

        }


        return super.onOptionsItemSelected(item);
    }

    private void openDialogFragment()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertView = inflater.inflate(R.layout.filter_dialog, null);
        alertDialog.setView(alertView);
        CheckBox sportCb = alertView.findViewById(R.id.filter_sport_cb);
        CheckBox foodCb = alertView.findViewById(R.id.filter_food_cb);
        CheckBox cultureCb = alertView.findViewById(R.id.filter_culture_cb);
        CheckBox musicCb = alertView.findViewById(R.id.filter_music_cb);
        Button confirmBtn = alertView.findViewById(R.id.confirm_btn);
        Button cancelBtn = alertView.findViewById(R.id.cancel_btn);
        final CheckBox[] checkBoxes  = new CheckBox[]{sportCb,foodCb,cultureCb,musicCb};
        show = alertDialog.show();
        for (int i = 0; i <checkBoxes.length;i++)
        {
            checkBoxes[i].setChecked(mViewModel.getIsCategoryChecked()[i]);
        }

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] chekeds = mViewModel.getIsCategoryChecked();
                for (int i = 0; i <checkBoxes.length;i++) {
                    chekeds[i] = checkBoxes[i].isChecked();
                }
                SwipeFragment fragment = (SwipeFragment) getSupportFragmentManager().findFragmentByTag(SWIPE_FRAGMENT);
                fragment.updateCategories(mViewModel.getIsCategoryChecked());
                show.dismiss();

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
            }
        });
    }

    public void moveToProfileFragment() {
//      ProfileFragment profileFragment = ProfileFragment.newInstance(mViewModel.getProfile(),mViewModel.getPictureUri().toString());
        ProfileFragment profileFragment = ProfileFragment.newInstance(mViewModel.getMyProfile());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
       // transaction.add(R.id.flContent, profileFragment, ACCOUNT_PROFILE_FRAGMENT);
        transaction.replace(R.id.flContent,profileFragment,ACCOUNT_PROFILE_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
        setTitle(getString(R.string.my_profile));
    }

    public void moveToQuestionsFragment() {
        QuestionsFragment questionsFragment = QuestionsFragment.newInstance(mViewModel.getMyProfile());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
      //  transaction.add(R.id.flContent, questionsFragment, QUESTIONS_FRAGMENT);
        transaction.replace(R.id.flContent,questionsFragment,QUESTIONS_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
        setTitle(getString(R.string.questions));

    }

    private void moveToMatchesFragment() {
        MatchesFragment matchesFragment = MatchesFragment.newInstance(mViewModel.getMyProfile());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
       // transaction.add(R.id.flContent, matchesFragment, MATCHES_FRAGMENT);
        transaction.replace(R.id.flContent,matchesFragment,MATCHES_FRAGMENT);
        transaction.addToBackStack(null);

        transaction.commit();

        setTitle(getString(R.string.your_matches));

        navigationView.getMenu().getItem(2).setChecked(true);
    }
    private void moveToMatchesFragment(String matcherUid) {
        MatchesFragment matchesFragment = MatchesFragment.newInstance(mViewModel.getMyProfile());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
      //  transaction.add(R.id.flContent, matchesFragment, MATCHES_FRAGMENT);
        transaction.replace(R.id.flContent,matchesFragment,MATCHES_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
        setTitle(getString(R.string.your_matches));

    }

    @Override
    public void onUpdateProfile(Profile profile) {
        mViewModel.setProfile(profile);
    }

    private void getUserLocation() {
        //   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
       // turnGPSOn();


             mLocationObserver= new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                try {
                    List<Address> addressList = mGeoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    mCityName = addressList.get(0).getLocality();
                    if (mCityName != null) {
                      //  mViewModel.getProfile().setCity(mCityName);
                      //  mViewModel.getProfile().setLocation(new LocationPoint(location.getLatitude(),location.getLongitude()));
                        //   moveToSwipeFragment();
                        mViewModel.updateCityName(mCityName);
                        mViewModel.updateLocation(new LocationPoint(location.getLatitude(),location.getLongitude()));

                        Log.d("loc",mCityName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mViewModel.isFirstLocation()) {
                    Log.d("call","1");
                    moveToSwipeFragment();
                    mViewModel.setFirstLocation(false);
                }

            }
        };
        LocationViewModel.getInstance(getApplicationContext()).observe(this, mLocationObserver);


    }

    /*private void turnGPSOn() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        sendBroadcast(intent);
    }
*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                //  getUserLocation();
                startLocation();

            }
            else{
                Log.d("call","2");
                moveToSwipeFragment();
             //   startLocation();
            //    requestLocationPermissions();

            }
        }
    }

    private void onMessageTokenReceived() {
        tokenReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mViewModel.setToken(intent.getStringExtra("token"));
            }
        };

        IntentFilter filter = new IntentFilter("token_changed");
        LocalBroadcastManager.getInstance(this).registerReceiver(tokenReceiver, filter);
    }

    private void getTokenWhenLogin() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Toast.makeText(MainActivity.this, newToken, Toast.LENGTH_SHORT).show();
                mViewModel.setToken(newToken);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(tokenReceiver);
    }

    public void moveToChat(Profile myProfile, Profile otherProfile, String chatid) {
        ChatFragment chatFragment = ChatFragment.newInstance(myProfile, otherProfile, chatid);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flContent, chatFragment, CHAT_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
        setTitle("Chat");
        navigationView.getMenu().getItem(2).setChecked(true);

    }

    @Override
    public void OnClickMoveToChat(Profile myProfile, Profile otherProfile, String chatid) {
        moveToChat(myProfile, otherProfile, chatid);
    }

    @Override
    public void onClickMoveToProfilePreview(Profile otherProfile,int compability) {
        onMoveToProfilePreview(otherProfile,compability);
    }

    @Override
    public void onClickMoveToProfilePreviewFromChat(Profile otherProfile, int compability) {
        onMoveToProfilePreview(otherProfile,compability);
    }

    @Override
    public void OnMoveToProfilePreviewFromLikes(Profile otherProfile, int compability) {
        onMoveToProfilePreview(otherProfile,compability);
    }
    private void onMoveToProfilePreview(Profile otherProfile, int compability){
        ProfilePreviewFragment profilePreviewFragment = ProfilePreviewFragment.newInstance(otherProfile,compability);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flContent, profilePreviewFragment, PROFILE_PREVIEW_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
        setTitle(otherProfile.getFirstName()+" "+otherProfile.getLastName());
    }

    @Override
    public void onClickMoveToPhotoPreviewListener(String uri) {
        PhotoPreviewFragment photoPreviewFragment = PhotoPreviewFragment.newInstance(uri);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flContent, photoPreviewFragment, PHOTO_PREVIEW_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void handleReturnFromNotif() {
        Bundle bundle = getIntent().getExtras();// add these lines of code to get data from notification
       // Log.d("other_match_uid",getIntent().getAction()+" "+getIntent().getAction().substring(3));

        String action=getIntent().getAction();
        final Menu menu = navigationView.getMenu();

       /* if (bundle != null&&bundle.containsKey("chat_id")) {
       //     Log.d("other_match_uid",bundle.getString("other_match_uid")+"");
            clearStack(null);
            menu.getItem(2).setChecked(true);
            mViewModel.getOtherProfile(bundle.getString("chat_id"));
            }*/
        if (action.startsWith("&s&")) {
            Log.d("other_match_uid",action.substring(3)+"");
            clearStack(null);
            menu.getItem(2).setChecked(true);
            mViewModel.getOtherProfile(action.substring(3));
        }
            else if(action.startsWith("&k&")){
                Log.d("call","3");
                moveToSwipeFragment();
                clearStack(null);
                moveToMatchesFragment(action.substring(3));
                menu.getItem(2).setChecked(true);
            }
            else {
                requestLocationPermissions();
            }



    }



    public void requestLocationPermissions() {
        mGeoCoder = new Geocoder(this, Locale.getDefault());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasLocationPremission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasLocationPremission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            } else {
                startLocation();
                //getUserLocation();
            }
        } else {
            startLocation();
            //getUserLocation();
        }
    }

    private void startLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationCallback = new LocationCallback() ;
        }
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {
                super.onLocationResult(locationResult);
                /*mHandler = new Handler();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<Address> addressList = mGeoCoder.getFromLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), 1);
                            mCityName = addressList.get(0).getLocality();
                            if (mCityName != null) {
                                    mHandler.removeCallbacks(this);
                                    mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                                    mViewModel.getProfile().setCity(mCityName);
                                    mViewModel.getProfile().setLocation(new LocationPoint(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude()));
                                    moveToSwipeFragment();
                                    mViewModel.writeProfile();

                                    Log.d("locc",mCityName);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });*/
                mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                getUserLocation();
            }
        };
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(500);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);


        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
                }
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        //TODO:make a custom window.
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK)
                startLocation();
               // getUserLocation();
            else {
                Log.d("call","4");
                moveToSwipeFragment();
            }
        }
    }
    private void clearStack(String stackName){
        FragmentManager fm =getSupportFragmentManager();

        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack(stackName,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void onBackPressed() {
        final Menu menu = navigationView.getMenu();
        if (menu.getItem(0).isChecked()||menu.getItem(5).isChecked()) {
            ProfilePreviewFragment profilePreviewFragment = (ProfilePreviewFragment) getSupportFragmentManager().findFragmentByTag(PROFILE_PREVIEW_FRAGMENT);
            if (profilePreviewFragment != null && profilePreviewFragment.isVisible()) {
                setTitle(R.string.app_name);
            }
            super.onBackPressed();
        }

        else{
            ChatFragment chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentByTag(CHAT_FRAGMENT);
            ProfilePreviewFragment profilePreviewFragment = (ProfilePreviewFragment) getSupportFragmentManager().findFragmentByTag(PROFILE_PREVIEW_FRAGMENT);
            if (profilePreviewFragment != null && profilePreviewFragment.isVisible()) {
                setTitle("Chat");
                super.onBackPressed();
            }
            else if (chatFragment != null && chatFragment.isVisible()) {
                MatchesFragment matchesFragment = (MatchesFragment) getSupportFragmentManager().findFragmentByTag(MATCHES_FRAGMENT);
                if(matchesFragment!=null&&matchesFragment.isVisible()){
                    setTitle("Matches");
                }
                else{
                    setTitle("Home");
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
                super.onBackPressed();

            }
            else {
                clearStack(null);
                navigationView.getMenu().getItem(0).setChecked(true);
                setTitle(R.string.app_name);
            }
            }
        }
    @Override
    protected void onNewIntent(Intent intent) {
        this.setIntent(intent);

        String matcherUid = intent.getAction();
        String action=matcherUid;


        Log.d("teest", getIntent().getAction()+"");
        if (action.startsWith("&s&")) {//intent.hasExtra("chat_id")
          //  mViewModel.getOtherProfile(intent.getStringExtra("chat_id"));
            mViewModel.getOtherProfile(action.substring(3));
        }
        else if(matcherUid!=null&&matcherUid.startsWith("&k&")){
            moveToMatchesFragment(matcherUid.substring(3));
        }
        super.onNewIntent(intent);
    }

    public void moveToSettingFragment() {
        SettingsFragment settingsFragment = SettingsFragment.newInstance(mViewModel.getMyProfile());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flContent,settingsFragment,SETTINGS_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
        setTitle(getString(R.string.settings));
    }

    @Override
    public void onShowToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoginAsGuest() {
        mViewModel.setLoginAsGuest(true);
        setTitle(getString(R.string.app_name));
        navigationView.getMenu().getItem(0).setChecked(true);
        Glide.with(MainActivity.this).load(R.drawable.man_profile).error(R.drawable.man_profile).into(mProfileIv);
        mNameTv.setText(getString(R.string.hello_guest));
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag(LOGIN_FRAGMENT)).commit();
        Log.d("call","5");
        moveToSwipeFragment();


    }
    private void guestNotAllowedDialog(boolean isFilterPressed){
        String dialogMessage;
        if(isFilterPressed){
            dialogMessage=getString(R.string.cant_filter_guest);
        }
        else{
            dialogMessage=getString(R.string.cant_menu_guest);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(dialogMessage)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onLogoutFromSwipeFragment() {
        logoutUser();

    }

    @Override
    public void onMoveToNewChat(Profile profile, String chatId,Profile otherProfile) {
        moveToChat(profile,otherProfile,chatId);
    }
}




