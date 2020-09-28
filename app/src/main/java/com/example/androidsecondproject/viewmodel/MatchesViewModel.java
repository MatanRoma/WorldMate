package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Chat;
import com.example.androidsecondproject.model.Match;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

import java.util.List;

public class MatchesViewModel extends AndroidViewModel {
    private Repository mRepository;
    private Profile mProfile;
    private int currentChatPosition;
    private String newMatchUid;


    private MutableLiveData<List<Profile>> mMatchesMutableLiveData;
    private MutableLiveData<Profile> mMyProfileMutableLiveData;


    public MatchesViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }

    public MutableLiveData<List<Profile>> getProfilesResultSuccess(){
        if (mMatchesMutableLiveData == null) {
            mMatchesMutableLiveData = new MutableLiveData<>();
            loadProfilesData();
        }
        return mMatchesMutableLiveData;
    }
    public MutableLiveData<Profile> getMyProfileResultSuccess(){
        if (mMyProfileMutableLiveData == null) {
            mMyProfileMutableLiveData = new MutableLiveData<>();
            loadMyProfileData();
        }
        return mMyProfileMutableLiveData;
    }

    private void loadMyProfileData() {
        mRepository.setProfileListener(new Repository.ProfileListener() {
            @Override
            public void onProfileDataChangeSuccess(Profile profile) {
                mMyProfileMutableLiveData.setValue(profile);
            }

            @Override
            public void onProfileDataChangeFail(String error) {

            }
        });
    }


    private void loadProfilesData() {
        mRepository.setProfilesListener(new Repository.ProfilesListener() {
            @Override
            public void onProfilesDataChangeSuccess(List<Profile> profiles) {
                mMatchesMutableLiveData.setValue(profiles);
            }

            @Override
            public void onProfilesDataChangeFail(String error) {

            }
        });
    }

    public void readMatches(){
        mRepository.readMatches(mMyProfileMutableLiveData.getValue());
    }






    public List<Profile> getMatches() {
/*        for (String email: mProfile.getMatches()) {
        }*/
        return mMatchesMutableLiveData.getValue();
    }



    public void setProfile(Profile profile) {
        this.mProfile = profile;
    }

   /* public Profile getProfile()
    {
        return mProfile;
    }*/



    public int getCurrentChatPosition() {
        return currentChatPosition;
    }

    public void setCurrentChatPosition(int currentChatPosition) {
        this.currentChatPosition = currentChatPosition;
    }

    public String getChatId(String otherUid) {
        for(Match match:mMyProfileMutableLiveData.getValue().getMatches()){
            if(match.getOtherUid().equals(otherUid))
                return match.getId();
        }
        return "";

    }

    public Profile getMyProfile() {
        return mMyProfileMutableLiveData.getValue();
    }

    public void readMyProfile() {
        mRepository.readProfile(mRepository.getCurrentUserId());
    }

    public void setNewMatchUid(String newMatchUid) {
        this.newMatchUid=newMatchUid;
    }

    public String getNewMatchUid() {
        return newMatchUid;
    }
}
