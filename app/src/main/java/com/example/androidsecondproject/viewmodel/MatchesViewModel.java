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


    private MutableLiveData<List<Profile>> mMatchesMutableLiveData;
    private MutableLiveData<Chat> mChatMutableLiveData;

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

    public MutableLiveData<Chat> getChatResultSuccess(){
        if (mChatMutableLiveData == null) {
            mChatMutableLiveData = new MutableLiveData<>();
            loadChat();
        }
        return mChatMutableLiveData;
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
        mRepository.readMatches(mProfile);
    }
    public void readChat(){
        mRepository.readChat(mChatMutableLiveData.getValue().getId());
    }





    public List<Profile> getMatches() {
/*        for (String email: mProfile.getMatches()) {
        }*/
        return mMatchesMutableLiveData.getValue();
    }



    public void setProfile(Profile profile) {
        this.mProfile = profile;
    }

    public Profile getProfile()
    {
        return mProfile;
    }




/*
    public void calculateMatches()
    {
        matches = new ArrayList<>();
        for (Match match:mProfile.getMatches()) {
            for (Profile profile : mProfiles) {
                if(match.getOtherUid().equals(profile.getUid()))
                {
                    matches.add(profile);
                }
            }
        }
    }
*/

    public void loadChat()
    {
        mRepository.setChatListener(new Repository.ChatListener() {
            @Override
            public void onChatDataChangeSuccess(Chat chat) {
                mChatMutableLiveData.setValue(chat);
            }

            @Override
            public void onChatDataChangeFail(String error) {

            }
        });
    }

    public int getCurrentChatPosition() {
        return currentChatPosition;
    }

    public void setCurrentChatPosition(int currentChatPosition) {
        this.currentChatPosition = currentChatPosition;
    }

    public String getChatId(String otherUid) {
        for(Match match:mProfile.getMatches()){
            if(match.getOtherUid().equals(otherUid))
                return match.getId();
        }
        return "";

    }
}
