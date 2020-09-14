package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.net.Uri;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;
import com.example.androidsecondproject.repository.StorageRepository;

public class MainViewModel extends AndroidViewModel {
    Repository mRepository;
    private MutableLiveData<Uri> mPictureDownloadSuccess;
    private MutableLiveData<String> mPictureDownloadFailed;
    private MutableLiveData<Profile> mProfileSuccessLiveData;
    private MutableLiveData<String> mProfileFailedLiveData;


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

    public MutableLiveData<Uri> getDownloadResultSuccess(){
        if (mPictureDownloadSuccess == null) {
            mPictureDownloadSuccess = new MutableLiveData<>();
            setDownloadListener();
        }
        return mPictureDownloadSuccess;
    }
    public MutableLiveData<String> getDownloadResultFailed(){
        if (mPictureDownloadFailed == null) {
            mPictureDownloadFailed = new MutableLiveData<>();
            setDownloadListener();
        }
        return mPictureDownloadFailed;
    }
    public void setDownloadListener(){
        mRepository.setDownloadListener(new StorageRepository.StorageDownloadPicListener() {
            @Override
            public void onSuccessDownloadPic(Uri uri) {
                mPictureDownloadSuccess.setValue(uri);
            }

            @Override
            public void onFailedDownloadPic(String error) {
                mPictureDownloadFailed.setValue(error);
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

    public void getNavigationHeaderImage() {
        setDownloadListener();
        mRepository.readMyProfilePictureFromStorage();
    }

    public String getuser() {
        return mRepository.getCurrentUserId();
    }

    public String getGender() {
       return mProfileSuccessLiveData.getValue().getGender();
    }
}
