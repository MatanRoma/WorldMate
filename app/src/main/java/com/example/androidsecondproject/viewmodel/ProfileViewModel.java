package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.app.Person;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.AuthRepository;
import com.example.androidsecondproject.repository.Database;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<Profile> profileLiveData;
    private Database database;

    public ProfileViewModel() {
        database=new Database();
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
        database.readProfile(uid);
    }

    public void writeProfile(String uid ,Profile profile){
        database.writeProfile(uid,profile);
    }
    private void loadProfileData() {
        database.setProfileListener(new Database.ProfileListener() {
            @Override
            public void onProfileDataChangeSuccess(Profile profile) {
                profileLiveData.setValue(profile);
            }

            @Override
            public void onProfileDataChangeFail(String error) {

            }
        });
    }

}
