package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

public class ProfileViewModel extends AndroidViewModel {

    private MutableLiveData<Profile> profileLiveData;
    private Repository repository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository=Repository.getInstance(application.getApplicationContext());
    }

    public MutableLiveData<Profile> getProfileResultSuccess(){
        if (profileLiveData == null) {
            profileLiveData = new MutableLiveData<>();
            loadProfileData();
      //      database.readProfileFrom();
        }
        return profileLiveData;
    }

    public void readProfile(String uid){
        repository.readProfile(uid);
    }

    public void writeProfile(String uid ,Profile profile){
        repository.writeProfile(profile);
    }
    private void loadProfileData() {
        repository.setProfileListener(new Repository.ProfileListener() {
            @Override
            public void onProfileDataChangeSuccess(Profile profile) {
                profileLiveData.setValue(profile);
            }

            @Override
            public void onProfileDataChangeFail(String error) {
                //TODO
            }
        });
    }

}
