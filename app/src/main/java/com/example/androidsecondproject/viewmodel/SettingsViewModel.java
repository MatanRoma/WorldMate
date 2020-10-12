package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Preferences;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        readSwipeProfiles();
    }

    public Set<String> getLookingFor() {
        Set<String> lookingForSet=new HashSet<>();
        if(mProfile.getPreferences().isLookingForWomen()){
            lookingForSet.add("female");
        }
        if(mProfile.getPreferences().isLookingForMen()){
            lookingForSet.add("male");
        }
        return lookingForSet;
    }
    public void readSwipeProfiles(){
        mRepository.readProfiles(mProfile);
    }

    public void updateLookingFor(Set<String> lookingForSet) {
        Preferences preferences=mProfile.getPreferences();
        if(lookingForSet.contains("male")){
            preferences.setLookingForMen(true);
        }
        else {
            preferences.setLookingForMen(false);
        }
        if(lookingForSet.contains("female")){
            preferences.setLookingForWomen(true);
        }
        else {
            preferences.setLookingForWomen(false);
        }
        mRepository.updateProfile(mProfile.getUid(),"preferences",preferences);
        readSwipeProfiles();
    }

    public void updateMaxDistance(int max_distance) {
        Preferences preferences=mProfile.getPreferences();
        preferences.setMaxDistance(max_distance);
        mRepository.updateProfile(mProfile.getUid(),"preferences",preferences);
        readSwipeProfiles();
    }

    public void updateMinMaxAge(int min, int max) {
        Preferences preferences=mProfile.getPreferences();
        preferences.setMinAge(min);
        preferences.setMaxAge(max);
        mRepository.updateProfile(mProfile.getUid(),"preferences",preferences);
        readSwipeProfiles();
    }
}