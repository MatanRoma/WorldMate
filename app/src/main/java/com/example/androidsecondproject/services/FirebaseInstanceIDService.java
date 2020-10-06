package com.example.androidsecondproject.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.request.target.NotificationTarget;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.view.ChatFragment;
import com.example.androidsecondproject.view.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

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
        final Map<String,String> messageDataMap=remoteMessage.getData();
        Log.d("servicee","service");
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isMatchNotifAllowed=sharedPreferences.getBoolean("match_pref",true);
        boolean isMessageNotifAllowed=sharedPreferences.getBoolean("message_pref",true);
        boolean isVibrateNotif=sharedPreferences.getBoolean("vibrate_pref",true);


        if(messageDataMap.get("sender")!=null&&isMatchNotifAllowed) {
            final int NOTIF_ID = 1;
            Log.d("other_match_uid", messageDataMap.get("match_uid"));

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String channelId;
            if(isVibrateNotif)
                channelId = "channel_two";
            else
                channelId="channel_three";
            String channelName = "Match_Channel";
            if (Build.VERSION.SDK_INT >= 26) {

                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                if (isVibrateNotif)
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                else
                    notificationChannel.setVibrationPattern(new long[]{0});
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);

            Intent activityIntent = new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activityIntent.setAction("&k&" + messageDataMap.get("match_uid"));
            final int notifId = messageDataMap.get("match_uid").hashCode();

            PendingIntent activityPendingIntent = PendingIntent.getActivity(this,
                    5, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.match_notif_layout);
            remoteViews.setTextViewText(R.id.title_tv, getString(R.string.new_match_with) +" "+ messageDataMap.get("sender"));

    /*        final NotificationTarget notificationTarget = new NotificationTarget(this, R.id.profile_image_notif, remoteViews, builder.build(), NOTIF_ID);
*//*            Bitmap bitmap = new Bitmap();
            Glide.with(this).asBitmap().load(messageDataMap.get("image")).error(R.drawable.man_profile).into(notificationTarget);*//*

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getApplicationContext()).asBitmap()
                            .load(messageDataMap.get("image"))
                            .error(R.drawable.man_profile)
                            .into(notificationTarget);
                }
            });*/

            builder.setCustomContentView(remoteViews);

            builder.setAutoCancel(true);
            builder.setContentIntent(activityPendingIntent);

            builder.setSmallIcon(R.drawable.ic_world_mate_icon_bw);
            Notification notification = builder.build();
            NotificationTarget notificationTarget = new NotificationTarget(this, R.id.profile_image_notif, remoteViews, notification, notifId);
            //Glide.with(this).asBitmap().load(messageDataMap.get("image")).error(R.drawable.man_profile).into(notificationTarget);
            notificationManager.notify(notifId, notification);




        }
        else if(messageDataMap.get("chat_id")!=null&&isMessageNotifAllowed){
            if(!(ChatFragment.chatId!=null&&ChatFragment.chatId.equals(messageDataMap.get("chat_id")))) {
                final int NOTIF_ID = 2;
                Log.d("chat_notif", messageDataMap.get("fullname"));
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String channelId;
                if (isVibrateNotif)
                    channelId = "channel_two";
                else
                    channelId = "channel_three";

                String channelName = "Match_Channel";
                if (Build.VERSION.SDK_INT >= 26) {

                    int importance = NotificationManager.IMPORTANCE_HIGH;

                    NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    Log.d("vib", isVibrateNotif + "");
                    if (isVibrateNotif)
                        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    else
                        notificationChannel.setVibrationPattern(new long[]{0L});

                    notificationManager.createNotificationChannel(notificationChannel);
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);

                Intent activityIntent = new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activityIntent.setAction("&s&" + messageDataMap.get("chat_id"));

                PendingIntent activityPendingIntent = PendingIntent.getActivity(this,
                        1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                activityIntent.putExtra("chat_id", messageDataMap.get("chat_id"));
                final int notifId = messageDataMap.get("chat_id").hashCode();

                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.match_notif_layout);
                remoteViews.setTextViewText(R.id.title_tv, messageDataMap.get("fullname")+" "+ getString(R.string.sent_you));
                if(messageDataMap.get("text_message").length()>15)
                {
                    String subLastMessage = messageDataMap.get("text_message").substring(0,15) + "...";
                    remoteViews.setTextViewText(R.id.text_id, subLastMessage);
                }
                else
                {
                    remoteViews.setTextViewText(R.id.text_id, messageDataMap.get("text_message"));
                }

                //NotificationTarget notificationTarget = new NotificationTarget(this, R.id.profile_image_notif, remoteViews, builder.build(), NOTIF_ID);

                builder.setCustomContentView(remoteViews);

                builder.setAutoCancel(true);
                builder.setContentIntent(activityPendingIntent);


                builder.setSmallIcon(R.drawable.ic_world_mate_icon_bw);
                Notification notification = builder.build();
                NotificationTarget notificationTarget = new NotificationTarget(this, R.id.profile_image_notif, remoteViews, notification, notifId);
                //Glide.with(this).asBitmap().load(R.drawable.ic_world_mate_icon).error(R.drawable.man_profile).into(notificationTarget);
                notificationManager.notify(notifId, notification);

            }
        }


    }
    public boolean foregrounded() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE);
    }

}
