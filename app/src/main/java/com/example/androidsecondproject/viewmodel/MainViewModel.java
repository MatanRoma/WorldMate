package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.LocationPoint;
import com.example.androidsecondproject.model.Match;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;


public class MainViewModel extends AndroidViewModel {
    private Repository mRepository;
    private MutableLiveData<Profile> mProfileSuccessLiveData;
    private MutableLiveData<String> mProfileFailedLiveData;
    private MutableLiveData<Profile> mOtherProfileSuccessLiveData;
    private boolean isFirstTime=true;
    private String messageToken;
    private boolean isFirstLocation=true;
    private boolean isFirstReadOtherProfile=true;
    private boolean[] isCategoryChecked;


    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository=Repository.getInstance(application.getApplicationContext());
        isCategoryChecked = new boolean[]{true,true,true};
    }
    public MutableLiveData<Profile> getProfileResultSuccess(){
        if (mProfileSuccessLiveData == null) {
            mProfileSuccessLiveData = new MutableLiveData<>();
            loadProfileData();
            //      database.readProfileFrom();
        }
        return mProfileSuccessLiveData;
    }
    public MutableLiveData<String> getProfileResultFailed(){
        if (mProfileFailedLiveData == null) {
            mProfileFailedLiveData = new MutableLiveData<>();
            loadProfileData();
            //      database.readProfileFrom();
        }
        return mProfileFailedLiveData;
    }

    public MutableLiveData<Profile> getOtherProfileResultSuccess(){
        if (mOtherProfileSuccessLiveData == null) {
            mOtherProfileSuccessLiveData = new MutableLiveData<>();
            loadOtherProfile();
            //      database.readProfileFrom();
        }
        return mOtherProfileSuccessLiveData;
    }



    private void loadProfileData() {
        mRepository.setProfileListener(new Repository.ProfileListener() {
            @Override
            public void onProfileDataChangeSuccess(Profile profile) {
                mProfileSuccessLiveData.setValue(profile);
            }

            @Override
            public void onProfileDataChangeFail(String error) {
                mProfileFailedLiveData.setValue(error);
            }
        });
    }

    public void logout()  {
        mRepository.logout();
        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getNavigationHeaderProfile() {
        loadProfileData();
        mRepository.readProfile(mRepository.getCurrentUserId());
    }



    public Profile getProfile(){
        return mProfileSuccessLiveData.getValue();
    }

    public void setProfile(Profile profile)
    {
        mProfileSuccessLiveData.setValue(profile);
    }


    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }



    public boolean checkIfAuth() {
        return mRepository.checkIfAuth();
    }


    public void setToken(String token) {
        Profile profile=mProfileSuccessLiveData.getValue();
        if(profile!=null){
            profile.setMessageToken(token);
            messageToken=null;
            mRepository.writeMyProfile(profile);
        }
        else{
            messageToken=token;
        }
    }

    public void setToken(){
        if(messageToken!=null){
            setToken(messageToken);
        }
    }
    private void loadOtherProfile(){
        mRepository.setOtherProfileListener(new Repository.ReadOtherProfileListener() {
            @Override
            public void onOtherProfileChange(Profile profile) {
                mOtherProfileSuccessLiveData.setValue(profile);
            }
        });
    }

    public void getOtherProfile(String chatId) {
        for(Match match:mProfileSuccessLiveData.getValue().getMatches()){
            if(match.getId().equals(chatId)){
                mRepository.readOtherProfile(match.getOtherUid());
                return;
            }
        }
    }

    public void writeProfile() {
        mRepository.writeMyProfile(mProfileSuccessLiveData.getValue());
    }

    public boolean isFirstLocation() {
        return isFirstLocation;
    }

    public void setFirstLocation(boolean firstLocation) {
        isFirstLocation = firstLocation;
    }

    public boolean isFirstReadOtherProfile() {
        return isFirstReadOtherProfile;
    }

    public void setFirstReadOtherProfile(boolean firstReadOtherProfile) {
        isFirstReadOtherProfile = firstReadOtherProfile;
    }




    public boolean[] getIsCategoryChecked() {
        return isCategoryChecked;
    }

    public void setIsCategoryChecked(boolean[] isCategoryChecked) {
        this.isCategoryChecked = isCategoryChecked;
    }

        public void updateCityName(String cityName){
            mProfileSuccessLiveData.getValue().setCity(cityName);
            mRepository.updateProfile(mProfileSuccessLiveData.getValue().getUid(), "city", cityName);
        }

        public void updateLocation(LocationPoint locationPoint){
            mProfileSuccessLiveData.getValue().setLocation(locationPoint);
            mRepository.updateProfile(mProfileSuccessLiveData.getValue().getUid(), "location", locationPoint);

        }

}
