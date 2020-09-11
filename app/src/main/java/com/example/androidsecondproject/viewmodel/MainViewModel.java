package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;
import com.example.androidsecondproject.repository.StorageRepository;

public class MainViewModel extends AndroidViewModel {
    Repository mRepository;
    private MutableLiveData<Uri> mPictureDownloadSuccess;
    private MutableLiveData<Profile> mProfileLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository=Repository.getInstance(application.getApplicationContext());
    }
    public MutableLiveData<Profile> getProfileResultSuccess(){
        if (mProfileLiveData == null) {
            mProfileLiveData = new MutableLiveData<>();
            loadProfileData();
            //      database.readProfileFrom();
        }
        return mProfileLiveData;
    }

    private void loadProfileData() {
        mRepository.setProfileListener(new Repository.ProfileListener() {
            @Override
            public void onProfileDataChangeSuccess(Profile profile) {
                mProfileLiveData.setValue(profile);
            }

            @Override
            public void onProfileDataChangeFail(String error) {

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
    public void setDownloadListener(){
        mRepository.setDownloadListener(new StorageRepository.StorageDownloadPicListener() {
            @Override
            public void onSuccessDownloadPic(Uri uri) {
                mPictureDownloadSuccess.setValue(uri);
            }

            @Override
            public void onFailedDownloadPic(String error) {

            }
        });
    }
    public void logout(){
        mRepository.logout();
    }

    public void getNavigationHeaderProfile() {
        Log.d("prof","tst1");
        mRepository.readProfile(mRepository.getCurrentUserId());
    }

    public void getNavigationHeaderImage() {
        mRepository.readMyProfilePictureFromStorage();
    }

    public String getuser() {
        return mRepository.getCurrentUserId();
    }
}
