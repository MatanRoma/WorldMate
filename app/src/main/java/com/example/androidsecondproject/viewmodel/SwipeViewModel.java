package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

import java.util.List;

public class SwipeViewModel extends AndroidViewModel {

    private Repository mRepository;
    private Profile mProfile;
    MutableLiveData<List<Profile>> mProfilesMutableLiveData;

    public SwipeViewModel(@NonNull Application application) {
        super(application);
        mRepository=Repository.getInstance(application.getApplicationContext());
    }
    public MutableLiveData<List<Profile>> getProfilesResultSuccess(){
        if (mProfilesMutableLiveData == null) {
            mProfilesMutableLiveData = new MutableLiveData<>();
            loadProfilesData();
        }
        return mProfilesMutableLiveData;
    }
    public void readProfiles(){
        mRepository.readProfiles();
    }
    public void setUserProfile(Profile profile){
        this.mProfile=profile;
    }

    private void loadProfilesData() {
        mRepository.setProfilesListener(new Repository.ProfilesListener() {
            @Override
            public void onProfilesDataChangeSuccess(List<Profile> profiles) {
                mProfilesMutableLiveData.setValue(profiles);
            }

            @Override
            public void onProfilesDataChangeFail(String error) {

            }
        });
    }

}
