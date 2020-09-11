package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.app.Person;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

public class PreferencesViewModel extends AndroidViewModel {

    private MutableLiveData<Profile> profileMutableLiveData;
    private Repository repository;

    public PreferencesViewModel(@NonNull Application application) {
        super(application);
        repository=Repository.getInstance(application.getApplicationContext());
    }

    public MutableLiveData<Profile> getProfileResultSuccess(){
        if (profileMutableLiveData == null) {
            profileMutableLiveData = new MutableLiveData<>();
            loadProfileData();
        }
        return profileMutableLiveData;
    }

    private void loadProfileData(){
        repository.setProfileListener(new Repository.ProfileListener() {
            @Override
            public void onProfileDataChangeSuccess(Profile profile) {
                profileMutableLiveData.setValue(profile);
            }

            @Override
            public void onProfileDataChangeFail(String error) {

            }
        });
    }

    public void readProfile() {
        repository.readProfile(repository.getCurrentUserId());
    }
    public void writeProfile(Profile profile){
        repository.writeProfile(profile);
    }
}
