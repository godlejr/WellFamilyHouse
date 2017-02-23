package com.demand.well_family.well_family.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.demand.well_family.well_family.MainActivity;
import com.demand.well_family.well_family.R;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Dev-0 on 2017-02-21.
 */

public class FirebaseMessageService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("dsfd", "From: " + remoteMessage.getFrom());
        Log.e("dsfd", "tag: " + remoteMessage.getNotification().getTag());
        Log.e("dsfd", "title: " + remoteMessage.getNotification().getTitle());


        Log.e("dsfd", "label: " + remoteMessage.getData().get("message"));
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e("dsfd", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e("dsfd", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        sendNotification(remoteMessage.getFrom(),remoteMessage.getNotification().getBody());
    }

    private void sendNotification(String from ,String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(from)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
