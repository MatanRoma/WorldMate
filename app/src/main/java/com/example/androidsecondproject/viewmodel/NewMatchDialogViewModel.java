package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.androidsecondproject.model.Message;
import com.example.androidsecondproject.model.NotificationManager;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

import org.json.JSONException;
import org.json.JSONObject;

public class NewMatchDialogViewModel extends AndroidViewModel {

    private Repository mRepository;
    private Profile mMyProfile;
    private Profile mOtherProfile;
    private String mChatId;
    private Context mContext;


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

    public void setContext(Context context) {
        this.mContext=context;
        onMessageSentSuccess();
    }
    private void onMessageSentSuccess(){
        mRepository.setMessageListener(new Repository.MessageListener() {
            @Override
            public void onMessageSentSuccess(Message message) {
                JSONObject rootObject=new JSONObject();
                JSONObject dataObject=new JSONObject();
                try {
                    Log.d("message","match");
                    rootObject.put("to",mOtherProfile.getMessageToken());
                    dataObject.put("other_uid",mMyProfile.getUid());
                    dataObject.put("chat_id", mChatId);
                    dataObject.put("text_message",message.getText());
                    dataObject.put("fullname",mMyProfile.getFirstName()+" "+mMyProfile.getLastName());
                    dataObject.put("image",mMyProfile.getProfilePictureUri());
                    dataObject.put("date",message.getFormattedDate());
                    rootObject.put("data",dataObject);

                    NotificationManager.sendNotification(mContext,rootObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
