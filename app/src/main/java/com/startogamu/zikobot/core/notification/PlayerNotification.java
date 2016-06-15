package com.startogamu.zikobot.core.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.receiver.ClearPlayerReceiver;
import com.startogamu.zikobot.module.alarm.model.Track;

/**
 * Created by josh on 13/06/16.
 */
public class PlayerNotification {

    private static int notificationId = 42;

    private static Context context;
    private static NotificationManager notificationManager;

    public static void init(final Context context) {
        PlayerNotification.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    public static void show(final Track track) {
        // prepare intent which is triggered if the
// notification is selected

        long when = System.currentTimeMillis();

        Intent intent = new Intent(context, ClearPlayerReceiver.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);


        RemoteViews playerView = new RemoteViews(context.getPackageName(), R.layout.view_notification_player);


        playerView.setOnClickPendingIntent(R.id.ll_stop, pIntent);
        playerView.setTextViewText(R.id.tv_artist, track.getArtistName());
        playerView.setTextViewText(R.id.tv_track, track.getName());

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_music)
                .setContent(playerView)

                .setContentTitle(track.getName())
                .setOngoing(true)
                .setAutoCancel(false)
                .setWhen(when);


        Notification notification = notificationBuilder.build();
        notification.bigContentView = playerView;

        NotificationTarget notificationTarget = new NotificationTarget(
                context,
                playerView,
                R.id.remote_view_iv_track,
                notification,
                notificationId);
        Glide.with(context)
                .load(track.getImageUrl())
                .asBitmap()
                .into(notificationTarget);
        notificationManager.notify(notificationId, notification);

    }

    public static void cancel() {
        notificationManager.cancel(notificationId);
    }
}
