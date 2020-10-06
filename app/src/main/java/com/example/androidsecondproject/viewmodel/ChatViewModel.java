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
import com.google.firebase.database.Query;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatViewModel extends AndroidViewModel {
    private Repository mRepository;
    private String mChatId;
    private Profile mMyProfile;
    private Profile mOtherProfile;
    private Context context;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }

    public Query readAllMessages() {
        return mRepository.readAllMessages(mChatId);
    }

    public void setChatId(String chatId) {
        this.mChatId =chatId;
    }

    public String getChatId() {
        return mChatId;
    }

    public void writeMessage(String text) {
        mRepository.writeMessage(mChatId,new Message(mRepository.getCurrentUserId(),text, mOtherProfile.getUid()));
    }

    public String getMyUid() {
        return mRepository.getCurrentUserId();
    }
    public void onMessageSentSuccess(){
        mRepository.setMessageListener(new Repository.MessageListener() {
            @Override
            public void onMessageSentSuccess(Message message) {

                JSONObject rootObject=new JSONObject();
                JSONObject dataObject=new JSONObject();
                try {
                    Log.d("message","chat");
                    rootObject.put("to", mOtherProfile.getMessageToken());
                    dataObject.put("other_uid", mMyProfile.getUid());
                    dataObject.put("chat_id", mChatId);
                    dataObject.put("text_message",message.getText());
                    dataObject.put("fullname", mMyProfile.getFirstName()+" "+ mMyProfile.getLastName());
                    dataObject.put("image", mMyProfile.getProfilePictureUri());
                    rootObject.put("data",dataObject);

                    NotificationManager.sendNotification(context,rootObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

               /* String text=message.getText();
                final JSONObject rootObject=new JSONObject();
                JSONObject notificationObject=new JSONObject();
                JSONObject dataObject=new JSONObject();

                try {
                    rootObject.put("to",otherProfile.getMessageToken());// for test
                    Log.d("token",otherProfile.getMessageToken());
                    notificationObject.put("title",myProfile.getFirstName()+" "+myProfile.getLastName());
                    notificationObject.put("body",text);
                    notificationObject.put("tag",myProfile.getEmail());

                    //notificationObject.put("icon", R.drawable.ic_messages_icon);
             //       notificationObject.put("icon", R.drawable.ic_messages_icon);
                 //   notificationObject.put("image",myProfile.getProfilePictureUri());
                    dataObject.put("chat_id",chatId);
                    rootObject.put("notification",notificationObject);
                    rootObject.put("data",dataObject);
                    NotificationManager.sendNotification(context,rootObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

            }
        });
    }

    public Profile getMyProfile() {
        return mMyProfile;
    }

    public void setMyProfile(Profile myProfile) {
        this.mMyProfile = myProfile;
    }

    public Profile getOtherProfile() {
        return mOtherProfile;
    }
    public void setContext(Context context){
        this.context=context;
        onMessageSentSuccess();
    }

    public void setOtherProfile(Profile otherProfile) {
        this.mOtherProfile = otherProfile;
    }
}
