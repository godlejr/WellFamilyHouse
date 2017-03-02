package com.demand.well_family.well_family.fcm;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private int badge_count = 0;

    @Override
    public void onCreate() {
        super.onCreate();

//        ActivityManager am = (ActivityManager) getSystemService(getApplicationContext().ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> info = am.getRunningTasks(10);
//        Log.e("info.size", info.size() + "");

//        if (info.size() == 1 && info.get(0).topActivity.getClassName().equals("com.sec.android.app.launcher.activities.LauncherActivity")) {
//            set_badge();
//        }

//        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
//        for (int i = 0; i < processes.size(); i++)
//            Log.e("프로세스", "("+i+")"+ processes.get(i).importance + "/ " + ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
//                    + ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND);

//        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if ("com.demand.well_family.well_family.fcm.FirebaseMessageService".equals(service.service.getClassName())) {
//                Log.e("Service", service.service.getClassName());
//            }
//        }

        set_badge();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

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


        sendNotification(remoteMessage.getFrom(), remoteMessage.getNotification().getBody());

    }

    private void sendNotification(String from, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("웰패밀리 하우스")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void set_badge() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("badge", Activity.MODE_PRIVATE);
        badge_count = pref.getInt("badge_count", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("badge_count", badge_count + 1);
        editor.apply();
        badge_count = pref.getInt("badge_count", badge_count);


        Intent intent = new Intent();
        intent.setAction("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count_package_name", "com.demand.well_family.well_family");
        intent.putExtra("badge_count_class_name", "com.demand.well_family.well_family.intro.IntroActivity");
        intent.putExtra("badge_count", badge_count);
        getApplicationContext().sendBroadcast(intent);
        Log.e("뱃지2", badge_count + "");


    }

}
