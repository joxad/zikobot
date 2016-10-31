package com.oxylane.decathlon.core.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;


/**
 * Created by josh on 10/04/16.
 */
public class AppNotification {

    private static PendingIntent intentClear;

    private static String title;
    private static Context context;
    private static NotificationManager notificationManager;


    public static void init(final Context context) {
        AppNotification.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /***
     */
    public static void show(final int drawable , final String title) {
        AppNotification.title = title;

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(drawable)
                .setOngoing(true).build();

        notificationManager.notify(0, notification);
    }

    public static void cancel() {
        notificationManager.cancel(0);
    }
}
