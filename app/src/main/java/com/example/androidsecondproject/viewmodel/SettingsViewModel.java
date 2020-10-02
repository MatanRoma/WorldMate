package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

public class SettingsViewModel extends AndroidViewModel {

    private Repository mRepository;


    public SettingsViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }

    public void readProfile() {
        mRepository.readProfile(mRepository.getCurrentUserId());
    }
    public void writeProfile(Profile profile){
        mRepository.writeMyProfile(profile);
    }

}