package com.example.androidsecondproject.viewmodel;

import android.app.Application;

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
    private int currentChatPosition;
    private String newMatchUid;
    private List<Chat> mChats;


    private MutableLiveData<List<Profile>> mMatchesMutableLiveData;
 //   private MutableLiveData<Profile> mMyProfileMutableLiveData;
    private MutableLiveData<List<Chat>> mChatMutableLiveData;



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
    public MutableLiveData<List<Chat>> getChatDataChange(){
        if (mChatMutableLiveData == null) {
            mChatMutableLiveData = new MutableLiveData<>();
            mChats=new ArrayList<>();
            loadChatData();
        }
        return mChatMutableLiveData;
    }

    private void loadChatData() {
        mRepository.setChatListener(new Repository.ChatListener() {
            @Override
            public void onChatDataChanged(List<Chat> chats) {
                mChats.clear();
                mChats.addAll(chats);
                mChatMutableLiveData.setValue(chats);
            }
        });
    }

 /*   public MutableLiveData<Profile> getMyProfileResultSuccess(){
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
    }*/


    private void loadProfilesData() {
       mRepository.setMatchesListener(new Repository.MatchesListener() {
           @Override
           public void onMatchesDataChangeSuccess(List<Profile> matches) {
               mMatchesMutableLiveData.setValue(matches);
           }

           @Override
           public void onMatchesDataChangeFail(String error) {

           }
       });
    }

    public void readMatches(){
        mRepository.readMatches(mProfile);
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
       /* for(Match match:mMyProfileMutableLiveData.getValue().getMatches()){
            if(match.getOtherUid().equals(otherUid))
                return match.getId();
        }
        return "";*/
       if(mProfile.getUid().compareTo(otherUid)>0){
            return mProfile.getUid()+otherUid;
       }
       else {
            return otherUid+mProfile.getUid();
       }
    }

    public Profile getMyProfile() {
        return mProfile;
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

    public void readChats() {
       mRepository.readChats(mProfile);
    }

    public List<Chat> getChats() {
        return mChats;
    }
}
