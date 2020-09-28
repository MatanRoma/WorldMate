package com.example.androidsecondproject.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.view.MainActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;
import java.util.Random;

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
        Log.d("servicee","service");
        if(messageDataMap!=null&&messageDataMap.get("sender")!=null){

            Log.d("notifi",messageDataMap.get("match_uid"));

           NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            String channelId="channel_one";
            String channelName="Match_Channel";
            if(Build.VERSION.SDK_INT>=26) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(null, null);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this,channelId);

            Intent activityIntent = new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
            String x_id=System.currentTimeMillis()+"";
            activityIntent.setAction(messageDataMap.get("match_uid"));

            PendingIntent activityPendingIntent = PendingIntent.getActivity(this,
                    5, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            activityIntent.putExtra(x_id,messageDataMap.get("match_uid"));//"other_match_uid"
            builder.setContentIntent(activityPendingIntent);
            builder.setContentTitle(messageDataMap.get("you matched with "+messageDataMap.get("sender")));
            builder.setContentText("you matched with "+messageDataMap.get("sender"));
            builder.setSmallIcon(R.drawable.ic_messages_icon);
            notificationManager.notify(1,builder.build());

        }


    }

}
