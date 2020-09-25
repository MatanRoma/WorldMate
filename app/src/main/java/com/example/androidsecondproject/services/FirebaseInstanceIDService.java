package com.example.androidsecondproject.services;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;

public class FirebaseInstanceIDService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("token",s);
        Intent intent=new Intent("token_changed");
        intent.putExtra("token",s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
