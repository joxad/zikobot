package com.startogamu.musicalarm.core.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import com.startogamu.musicalarm.R;

/**
 * Created by josh on 10/04/16.
 */
public class MusicNotification {

    private static PendingIntent intentPlay;
    private static PendingIntent intentPause;
    private static PendingIntent intentNext;
    private static PendingIntent intentPrevious;
    private static PendingIntent intentClear;

    private static String title;
    private static Context context;
    private static NotificationManager notificationManager;


    public static void init(final Context context) {
        MusicNotification.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /***
     */
    public static void show(final String title) {
        MusicNotification.title = title;
        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_alarm)
                .addAction(R.drawable.ic_pause, "Pause", intentPause)
                .addAction(R.drawable.ic_next, "Next", intentNext)
                .addAction(R.drawable.ic_clear, "Clear", intentClear)
                .setAutoCancel(false).build();

        notificationManager.notify(0, notification);
    }


    /**
     * @param track
     */
    public static void update(final String track) {

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_alarm)
                .addAction(R.drawable.ic_pause, "Pause", intentPause)
                .addAction(R.drawable.ic_next, "Next", intentNext)
                .addAction(R.drawable.ic_clear, "Clear", intentClear)
                .setAutoCancel(false).build();

        notificationManager.notify(0, notification);
    }


    public static void cancel() {
        notificationManager.cancel(0);
    }
}
