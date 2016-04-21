package com.startogamu.musicalarm.core.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.receiver.StopReceiver;
import com.startogamu.musicalarm.core.utils.REQUEST;

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

        Intent clearIntent = new Intent();
        clearIntent.setAction(StopReceiver.TAG);
        intentClear = PendingIntent.getBroadcast(context, REQUEST.NOTIFICATION_CLEAR, clearIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_alarm)
                .addAction(R.drawable.ic_pause, "Pause", intentPause)
                .addAction(R.drawable.ic_next, "Next", intentNext)
                .addAction(R.drawable.ic_clear, "Clear", intentClear)
                .setOngoing(true).build();

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
