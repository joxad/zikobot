package com.startogamu.zikobot.core.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.receiver.ClearPlayerReceiver;
import com.startogamu.zikobot.core.receiver.NextPlayerReceiver;
import com.startogamu.zikobot.core.receiver.NotificationPauseResumeReceiver;
import com.startogamu.zikobot.core.receiver.PreviousPlayerReceiver;
import com.startogamu.zikobot.core.model.Track;

/**
 * Created by josh on 13/06/16.
 */
public class PlayerNotification {

    private int notificationId = 42;

    private Context context;
    private NotificationManager notificationManager;
    private RemoteViews playerViewSmall;
    private RemoteViews playerViewLarge;
    private Notification notification;
    private Handler handler;

    public PlayerNotification(Context context) {
        handler = new Handler(Looper.getMainLooper());
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    }
    /***
     * @param track
     */
    public void show(final Track track) {
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
    private void loadSmallImage(Notification notification, Track track) {
        NotificationTarget notificationTarget = new NotificationTarget(
                context,
                playerViewSmall,
                R.id.remote_view_iv_track,
                notification,
                notificationId);
        handler.post(() -> Glide.with(context)
                .load(track.getImageUrl())
                .asBitmap()
                .into(notificationTarget));
    }

    /**
     * @param notification
     * @param track
     */
    private void loadLargeImage(Notification notification, Track track) {
        NotificationTarget notificationTarget = new NotificationTarget(
                context,
                playerViewLarge,
                R.id.remote_view_iv_track,
                notification,
                notificationId);
        handler.post(() -> Glide.with(context)
                .load(track.getImageUrl())
                .asBitmap()
                .into(notificationTarget));
    }

    /***
     * @param pIntent
     * @param track
     */
    private void initLargeNotification(PendingIntent pIntent, Track track) {
        playerViewLarge = new RemoteViews(context.getPackageName(), R.layout.view_notification_player);
        playerViewLarge.setOnClickPendingIntent(R.id.iv_stop, pIntent);
        playerViewLarge.setTextViewText(R.id.tv_artist, track.getArtistName());
        playerViewLarge.setTextViewText(R.id.tv_track, track.getName());

        Intent intentPause = new Intent(context, NotificationPauseResumeReceiver.class);
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
    public void updatePlayStatus(boolean showPlay) {
        playerViewLarge.setImageViewResource(R.id.iv_play, showPlay ? R.drawable.ic_play_arrow : R.drawable.ic_pause);
        if (showPlay) {
            Intent intentResume = new Intent(context, NotificationPauseResumeReceiver.class);
            PendingIntent pIntentResume = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intentResume, PendingIntent.FLAG_UPDATE_CURRENT);
            playerViewLarge.setOnClickPendingIntent(R.id.iv_play, pIntentResume);
        } else {
            Intent intentPause = new Intent(context, NotificationPauseResumeReceiver.class);
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
    private void initSmallNotification(PendingIntent pIntent, Track track) {
        playerViewSmall = new RemoteViews(context.getPackageName(), R.layout.view_notification_player_small);
        playerViewSmall.setOnClickPendingIntent(R.id.iv_stop, pIntent);
        playerViewSmall.setTextViewText(R.id.tv_artist, track.getArtistName());
        playerViewSmall.setTextViewText(R.id.tv_track, track.getName());
    }


    /***
     *
     */
    public void cancel() {
        notificationManager.cancel(notificationId);
    }
}
