package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.androidsecondproject.model.Message;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

public class NewMatchDialogViewModel extends AndroidViewModel {

    private Repository mRepository;
    private Profile mMyProfile;
    private Profile mOtherProfile;
    private String mChatId;


    public NewMatchDialogViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }


    public Profile getmMyProfile() {
        return mMyProfile;
    }

    public void setmMyProfile(Profile mMyProfile) {
        this.mMyProfile = mMyProfile;
    }

    public Profile getmOtherProfile() {
        return mOtherProfile;
    }

    public void setmOtherProfile(Profile mOtherProfile) {
        this.mOtherProfile = mOtherProfile;
    }

    public String getmChatId() {
        return mChatId;
    }

    public void setmChatId() {
        this.mChatId = calculateChatId();
    }


    public String calculateChatId()
    {
        String chatKeyId,myUid=mMyProfile.getUid(),otherUid=mOtherProfile.getUid();
        if(myUid.compareTo(otherUid)>0){
            chatKeyId=myUid+otherUid;
        }
        else{
            chatKeyId=otherUid+myUid;
        }
        return chatKeyId;
    }

    public void writeMessage(String text)
    {
        mRepository.writeMessage(mChatId,new Message(mMyProfile.getUid(),text,mOtherProfile.getUid()));
    }
}
