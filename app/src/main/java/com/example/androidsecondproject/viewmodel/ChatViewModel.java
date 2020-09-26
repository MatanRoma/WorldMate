package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Chat;
import com.example.androidsecondproject.model.Message;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;
import com.google.firebase.database.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChatViewModel extends AndroidViewModel {
    private Repository mRepository;
    private String chatId;
    private Profile myProfile;
    private Profile otherProfile;
    private Context context;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }

    public Query readAllMessages() {
        return mRepository.readAllMessages(chatId);
    }

    public void setChatId(String chatId) {
        this.chatId=chatId;
    }

    public void writeMessage(String text) {
        mRepository.writeMessage(chatId,new Message(mRepository.getCurrentUserId(),text));
    }

    public String getMyUid() {
        return mRepository.getCurrentUserId();
    }
    public void onMessageSentSuccess(){
        mRepository.setMessageListener(new Repository.MessageListener() {
            @Override
            public void onMessageSentSuccess(Message message) {

                      /*{
            "message":{
            "token":"bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1...",
                    "data":{
                "Nick" : "Mario",
                        "body" : "great match!",
                        "Room" : "PortugalVSDenmark"
            }
        }
        }*/

                String text=message.getText();
                final JSONObject rootObject=new JSONObject();
                JSONObject notificationObject=new JSONObject();
                JSONObject dataObject=new JSONObject();

                try {
                    rootObject.put("to",otherProfile.getMessageToken());
                    Log.d("token",otherProfile.getMessageToken());
                    notificationObject.put("title",myProfile.getFirstName()+" "+myProfile.getLastName());
                    notificationObject.put("body",text);
                    notificationObject.put("tag",myProfile.getEmail());
                    notificationObject.put("icon", R.drawable.ic_messages_icon);
                 //   notificationObject.put("image",myProfile.getProfilePictureUri());
                    dataObject.put("chat_id",chatId);
                    rootObject.put("notification",notificationObject);
                    rootObject.put("data",dataObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String url = "https://fcm.googleapis.com/fcm/send";
                final String kkk="AAAABbrOweI:APA91bEnV1kmFrDiPTZrN_tGxxi8H8rbwiohInI2qa1WUyqHR_MSxZOMSeZ_DQ5zwJS6HZiSDjM-j_14zgeXdDI7FfTdgqYLCpc3HKohtRMRIFhQafmJ19X_znhIkcRkZ9fq4x4YGI41";

                RequestQueue queue= Volley.newRequestQueue(context);
                StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> headers=new HashMap<>();
                        headers.put("Content-Type","application/json");
                        headers.put("Authorization","key="+kkk);
                        return headers;
                     }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return rootObject.toString().getBytes();
                    }
                };
                queue.add(request);
                queue.start();


            }
        });
    }

    public Profile getMyProfile() {
        return myProfile;
    }

    public void setMyProfile(Profile myProfile) {
        this.myProfile = myProfile;
    }

    public Profile getOtherProfile() {
        return otherProfile;
    }
    public void setContext(Context context){
        this.context=context;
        onMessageSentSuccess();
    }

    public void setOtherProfile(Profile otherProfile) {
        this.otherProfile = otherProfile;
    }
}
