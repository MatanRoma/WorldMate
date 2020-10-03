package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

public class PreferencesViewModel extends AndroidViewModel {

    private MutableLiveData<Profile> mProfileMutableLiveData;
    private Repository mRepository;
    private int mMaxAge;
    private int mMinAge;

    public PreferencesViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }

    public MutableLiveData<Profile> getProfileResultSuccess(){
        if (mProfileMutableLiveData == null) {
            mProfileMutableLiveData = new MutableLiveData<>();
            loadProfileData();
        }
        return mProfileMutableLiveData;
    }

    private void loadProfileData(){
        mRepository.setProfileListener(new Repository.ProfileListener() {
            @Override
            public void onProfileDataChangeSuccess(Profile profile) {
                mProfileMutableLiveData.setValue(profile);
            }

            @Override
            public void onProfileDataChangeFail(String error) {

            }
        });
    }

    public void readProfile() {
        mRepository.readProfile(mRepository.getCurrentUserId());
    }
    public void writeProfile(Profile profile){
        mRepository.writeMyProfile(profile);
    }

    public int getmMaxAge() {
        return mMaxAge;
    }

    public void setmMaxAge(int mMaxAge) {
        this.mMaxAge = mMaxAge;
    }

    public int getmMinAge() {
        return mMinAge;
    }

    public void setmMinAge(int mMinAge) {
        this.mMinAge = mMinAge;
    }
}
