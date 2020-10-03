package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

public class SettingsViewModel extends AndroidViewModel {

    private Repository mRepository;
    private Profile mProfile;


    public SettingsViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }


    public Profile getmProfile() {
        return mProfile;
    }

    public void setmProfile(Profile mProfile) {
        this.mProfile = mProfile;
    }

    public void updateDiscovery(boolean isDiscoveryOn){
        mProfile.setDiscovery(isDiscoveryOn);
        mRepository.updateProfile(mProfile.getUid(),"discovery",isDiscoveryOn);
    }
}