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
import com.startogamu.zikobot.core.receiver.NextPlayerReceiver;
import com.startogamu.zikobot.core.receiver.PausePlayerReceiver;
import com.startogamu.zikobot.core.receiver.PreviousPlayerReceiver;
import com.startogamu.zikobot.core.receiver.ResumePlayerReceiver;
import com.startogamu.zikobot.module.zikobot.model.Track;

/**
 * Created by josh on 13/06/16.
 */
public class PlayerNotification {

    private static int notificationId = 42;

    private static Context context;
    private static NotificationManager notificationManager;
    private static RemoteViews playerViewSmall;
    private static RemoteViews playerViewLarge;
    private static Notification notification;

    /***
     * @param context
     */
    public static void init(final Context context) {
        PlayerNotification.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    /***
     * @param track
     */
    public static void show(final Track track) {
        // prepare intent which is triggered if the
// notification is selected

        long when = System.currentTimeMillis();
        Intent intent = new Intent(context, ClearPlayerReceiver.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, 0);
        initSmallNotification(pIntent, track);
        initLargeNotification(pIntent, track);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_music)
                .setContent(playerViewSmall)
                .setContentTitle(track.getName())
                .setOngoing(true)
                .setAutoCancel(false)
                .setWhen(when);

        notification = notificationBuilder.build();
        notification.bigContentView = playerViewLarge;

        loadSmallImage(notification, track);
        loadLargeImage(notification, track);

        notificationManager.notify(notificationId, notification);

    }

    /**
     * @param notification
     * @param track
     */
    private static void loadSmallImage(Notification notification, Track track) {
        NotificationTarget notificationTarget = new NotificationTarget(
                context,
                playerViewSmall,
                R.id.remote_view_iv_track,
                notification,
                notificationId);
        Glide.with(context)
                .load(track.getImageUrl())
                .asBitmap()
                .into(notificationTarget);
    }

    /**
     * @param notification
     * @param track
     */
    private static void loadLargeImage(Notification notification, Track track) {
        NotificationTarget notificationTarget = new NotificationTarget(
                context,
                playerViewLarge,
                R.id.remote_view_iv_track,
                notification,
                notificationId);
        Glide.with(context)
                .load(track.getImageUrl())
                .asBitmap()
                .into(notificationTarget);
    }

    /***
     * @param pIntent
     * @param track
     */
    private static void initLargeNotification(PendingIntent pIntent, Track track) {
        playerViewLarge = new RemoteViews(context.getPackageName(), R.layout.view_notification_player);
        playerViewLarge.setOnClickPendingIntent(R.id.iv_stop, pIntent);
        playerViewLarge.setTextViewText(R.id.tv_artist, track.getArtistName());
        playerViewLarge.setTextViewText(R.id.tv_track, track.getName());

        Intent intentPause = new Intent(context, PausePlayerReceiver.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntentPause = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
        playerViewLarge.setOnClickPendingIntent(R.id.iv_play, pIntentPause);
        playerViewLarge.setImageViewResource(R.id.iv_play, R.drawable.ic_pause);
        Intent intentNext = new Intent(context, NextPlayerReceiver.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntentNext = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
        playerViewLarge.setOnClickPendingIntent(R.id.iv_next, pIntentNext);


        Intent intentPrevious = new Intent(context, PreviousPlayerReceiver.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntentPrevious = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);
        playerViewLarge.setOnClickPendingIntent(R.id.iv_previous, pIntentPrevious);


    }

    /***
     * @param showPlay
     */
    public static void updatePlayStatus(boolean showPlay) {
        playerViewLarge.setImageViewResource(R.id.iv_play, showPlay ? R.drawable.ic_play_arrow : R.drawable.ic_pause);
        if (showPlay) {
            Intent intentResume = new Intent(context, ResumePlayerReceiver.class);
            PendingIntent pIntentResume = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intentResume, PendingIntent.FLAG_UPDATE_CURRENT);
            playerViewLarge.setOnClickPendingIntent(R.id.iv_play, pIntentResume);
        } else {
            Intent intentPause = new Intent(context, PausePlayerReceiver.class);
            PendingIntent pIntentPause = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            playerViewLarge.setOnClickPendingIntent(R.id.iv_play, pIntentPause);
        }
        notification.bigContentView = playerViewLarge;
        notificationManager.notify(notificationId, notification);

    }


    /***
     * @param pIntent
     * @param track
     */
    private static void initSmallNotification(PendingIntent pIntent, Track track) {
        playerViewSmall = new RemoteViews(context.getPackageName(), R.layout.view_notification_player_small);
        playerViewSmall.setOnClickPendingIntent(R.id.iv_stop, pIntent);
        playerViewSmall.setTextViewText(R.id.tv_artist, track.getArtistName());
        playerViewSmall.setTextViewText(R.id.tv_track, track.getName());
    }


    /***
     *
     */
    public static void cancel() {
        notificationManager.cancel(notificationId);
    }
}
