package com.example.androidsecondproject.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.androidsecondproject.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseInstanceIDService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("token",s);
        Intent intent=new Intent("token_changed");
        intent.putExtra("token",s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String> messageDataMap=remoteMessage.getData();
        if(messageDataMap!=null){
            Log.d("service",messageDataMap.get("message"));

           NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            String channelId="channel_one";
            String channelName="Music_Channel";
            if(Build.VERSION.SDK_INT>=26) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(null, null);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this,channelId);
            builder.setContentTitle(messageDataMap.get("message"));
            builder.setSmallIcon(R.drawable.ic_messages_icon);
            notificationManager.notify(1,builder.build());



        }

    }
}
