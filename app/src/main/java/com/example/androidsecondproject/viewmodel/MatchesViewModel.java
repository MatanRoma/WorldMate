package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Chat;
import com.example.androidsecondproject.model.Match;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class MatchesViewModel extends AndroidViewModel {
    private Repository mRepository;
    private Profile mProfile;
    private Adapter mMatchesAdapter;
    private List<Profile> matches;
    private List<Profile> mProfiles;
    private Chat mChat;

    private MutableLiveData<List<Profile>> mProfilesMutableLiveData;
    private MutableLiveData<Chat> mChatMutableLiveData;

    public MatchesViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }

    public MutableLiveData<List<Profile>> getProfilesResultSuccess(){
        if (mProfilesMutableLiveData == null) {
            mProfilesMutableLiveData = new MutableLiveData<>();
            loadProfilesData();
        }
        return mProfilesMutableLiveData;
    }

    public MutableLiveData<Chat> getChatResultSuccess(String chatId){
        if (mChatMutableLiveData == null) {
            mChatMutableLiveData = new MutableLiveData<>();
            readChat(chatId);
        }
        return mChatMutableLiveData;
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

    public void readProfiles(){
        mRepository.readProfiles(mProfile);
    }

    public Adapter getmMatchesAdapter() {
        return mMatchesAdapter;
    }

    public void setmMatchesAdapter(Adapter mMatchesAdapter) {
        this.mMatchesAdapter = mMatchesAdapter;
    }

    public List<Profile> getMatches() {
/*        for (String email: mProfile.getMatches()) {
        }*/
        return matches;
    }

    public void setMatches(List<Profile> matches) {
        this.matches = matches;
    }

    public void writeProfile(){
        mRepository.writeMyProfile(mProfile);
    }

    public void setProfile(Profile profile) {
        this.mProfile = profile;
    }

    public Profile getProfile()
    {
        return mProfile;
    }

    public List<Profile> getmProfiles() {
        return mProfiles;
    }

    public void setmProfiles(List<Profile> mProfiles) {
        this.mProfiles = mProfiles;

    }

    public void calculateMatches()
    {
        matches = new ArrayList<>();
        for (Match match:mProfile.getMatches()) {
            for (Profile profile : mProfiles) {
                if(match.getEmail().equals(profile.getEmail()))
                {
                    matches.add(profile);
                }
            }
        }
    }

    public void readChat(String chatId)
    {
        mRepository.setChatListener(new Repository.ChatListener() {
            @Override
            public void onChatDataChangeSuccess(Chat chat) {
                mChat = chat;
            }

            @Override
            public void onChatDataChangeFail(String error) {

            }
        });
        mRepository.readChat(chatId);
    }
}
