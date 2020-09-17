package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.Question;
import com.example.androidsecondproject.repository.Repository;




public class MainViewModel extends AndroidViewModel {
    private Repository mRepository;
    private MutableLiveData<Profile> mProfileSuccessLiveData;
    private MutableLiveData<String> mProfileFailedLiveData;
    private boolean isFirstTime=true;


    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository=Repository.getInstance(application.getApplicationContext());
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

    public void logout(){
        mRepository.logout();
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


}
