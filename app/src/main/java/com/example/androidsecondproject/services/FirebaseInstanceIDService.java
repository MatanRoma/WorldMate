package com.example.androidsecondproject.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.view.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseInstanceIDService extends FirebaseMessagingService {
    RemoteViews remoteViews;
    final int NOTIF_ID = 1;
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

            Log.d("other_match_uid",messageDataMap.get("match_uid"));

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
            activityIntent.setAction("&k&"+messageDataMap.get("match_uid"));

            PendingIntent activityPendingIntent = PendingIntent.getActivity(this,
                    5, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews = new RemoteViews(getPackageName(),R.layout.match_notif_layout);
            remoteViews.setTextViewText(R.id.title_tv,"you matched with "+messageDataMap.get("sender"));

           // Notification notification=builder.build();





            NotificationTarget notificationTarget = new NotificationTarget(this,R.id.profile_image_notif,remoteViews,builder.build(),NOTIF_ID);

            Glide.with(FirebaseInstanceIDService.this).asBitmap().load(messageDataMap.get("image")).error(R.drawable.man_profile).into(notificationTarget);
        //    builder.setCustomContentView(remoteViews);
        //    remoteViews.setImageViewUri(R.id.profile_image_notif,Uri.parse(messageDataMap.get("image")));

            builder.setAutoCancel(true);
            builder.setContentIntent(activityPendingIntent);

            builder.setSmallIcon(R.drawable.ic_messages_icon);


            notificationManager.notify(NOTIF_ID,builder.build());

        }


    }

}
