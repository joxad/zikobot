package com.joxad.zikobot.app.core.notification;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.joxad.zikobot.app.R;
import com.tonyodev.fetch.listener.FetchListener;

/**
 * Created by josh on 13/06/16.
 */
public class DownloadNotification {

    public final NotificationCompat.Builder mBuilder;
    private Context context;
    private NotificationManagerCompat notificationManager;
    private long id;

    public DownloadNotification(Context context, String track, long id) {
        this.context = context;
        // Get an instance of the NotificationManager service
        notificationManager = NotificationManagerCompat.from(context);
        this.id = id;
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("Download track")
                .setContentText(track + "Download in progress")
                .setProgress(100, 0, false)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify((int) id, mBuilder.build());

    }

    public void updateProgress(int progress){
        mBuilder.setProgress(100,progress,false);
        notificationManager.notify((int) id, mBuilder.build());
    }

    /***
     *
     */
    public void cancel() {
        notificationManager.cancel((int) id);
    }
}
