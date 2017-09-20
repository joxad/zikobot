package com.startogamu.zikobot.core;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.startogamu.zikobot.R;

/**
 * Created by linux on 9/20/17.
 */

public class ZikoNotification {
    private final Context context;
    private NotificationManagerCompat notificationManager;

    public ZikoNotification(Context context) {
        this.context = context;
        // Get an instance of the NotificationManager service
        notificationManager = NotificationManagerCompat.from(context);

    }

    public void showNotificationAlarm() {

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Wek Hup")
                        .setContentText("Time to move bitch !")
                        .setPriority(Notification.PRIORITY_MAX)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(false);


        // GoogleApiManager.INSTANCE.sendTrack(track, bitmap);
// Build the notification and issues it with notification manager.
        notificationManager.notify(1, notificationBuilder.build());
    }



}
