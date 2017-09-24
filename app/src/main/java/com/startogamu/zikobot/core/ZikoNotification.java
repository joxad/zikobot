package com.startogamu.zikobot.core;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.joxad.zikobot.data.db.model.ZikoAlarm;
import com.joxad.zikobot.data.db.model.ZikoTrack;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.receiver.NotificationDismissedReceiver;
import com.startogamu.zikobot.core.receiver.PausePlayerReceiver;
import com.startogamu.zikobot.core.receiver.ResumePlayerReceiver;
import com.startogamu.zikobot.core.receiver.StopPlayerReceiver;
import com.startogamu.zikobot.home.HomeActivity;

import java.util.ArrayList;

/**
 * Created by linux on 9/20/17.
 */

public class ZikoNotification {
    private final Context context;
    private final PendingIntent viewPendingIntent;
    private NotificationManagerCompat notificationManager;
    private final ArrayList<NotificationCompat.Action> actions;
    private int notificationId = 42;
    private RemoteViews playerViewSmall;

    public ZikoNotification(Context context) {
        this.context = context;
        // Get an instance of the NotificationManager service
        notificationManager = NotificationManagerCompat.from(context);

        Intent viewIntent = new Intent(context, HomeActivity.class);
        viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, 0);
        Intent intentPause = new Intent(context, PausePlayerReceiver.class);
        PendingIntent pIntentPause = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent intentResume = new Intent(context, ResumePlayerReceiver.class);
        PendingIntent pIntentResume = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intentResume, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent intentStop = new Intent(context, StopPlayerReceiver.class);
        PendingIntent pIntentStop = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intentStop, PendingIntent.FLAG_UPDATE_CURRENT);
        actions = new ArrayList<>();
        actions.add(new NotificationCompat.Action.Builder(R.drawable.ic_pause_notif, "Pause", pIntentPause).build());
        actions.add(new NotificationCompat.Action.Builder(R.drawable.ic_play_white, "Play", pIntentResume).build());
        actions.add(new NotificationCompat.Action.Builder(R.drawable.ic_clear_white, "Arrêter", pIntentStop).build());

    }

    public void showNotificationAlarm(ZikoAlarm alarm) {
        alarm.load();
        int id = alarm.getId();
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, "zikobot")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(alarm.getZikoPlaylist().getName())
                        .setContentText("Wake up !")
                        .setPriority(Notification.PRIORITY_MAX)
                        .setAutoCancel(false);

        notificationManager.notify(id, notificationBuilder.build());
    }

    public void prepareNotification(ZikoTrack track) {
// Build intent for notification content
        track.getZikoArtist().load();
        NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender()
                .setDisplayIntent(viewPendingIntent).addActions(actions)
                .setCustomSizePreset(Notification.WearableExtender.SIZE_FULL_SCREEN);
        Glide.with(context)
                .load(track.getZikoArtist().getImage())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        showNotification(track, extender, viewPendingIntent, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_vinyl));
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        showNotification(track, extender, viewPendingIntent, resource);
                    }
                });

    }


    void showNotification(ZikoTrack track, NotificationCompat.WearableExtender extender, PendingIntent intent, @Nullable Bitmap bitmap) {
        Intent intentStop = new Intent(context, StopPlayerReceiver.class);
        PendingIntent pIntentStop = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intentStop, PendingIntent.FLAG_UPDATE_CURRENT);

        initSmallNotification(pIntentStop, track, bitmap);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, "zikobot")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(track.getName())
                        .setContentText(track.getZikoArtist().getName())
                        .setPriority(Notification.PRIORITY_MAX)
                        .setOngoing(true)
                        .setContent(playerViewSmall)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(false)
                        .setDeleteIntent(createOnDismissedIntent(context, notificationId))
                        .setLargeIcon(bitmap)
                        .extend(extender)
                        .setContentIntent(intent);

        notificationManager.notify(notificationId, notificationBuilder.build());
    }


    private PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("notificationId", notificationId);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        notificationId, intent, 0);
        return pendingIntent;
    }


    /***
     * @param pIntent
     * @param track
     */
    private void initSmallNotification(PendingIntent pIntent, ZikoTrack track, Bitmap bitmap) {
        playerViewSmall = new RemoteViews(context.getPackageName(), R.layout.view_notification_player_small);
        playerViewSmall.setOnClickPendingIntent(R.id.iv_stop, pIntent);
        playerViewSmall.setTextViewText(R.id.tv_artist, track.getZikoArtist().getName());
        playerViewSmall.setTextViewText(R.id.tv_track, track.getName());
        playerViewSmall.setImageViewBitmap(R.id.remote_view_iv_track, bitmap);
    }


    public void cancel() {
        notificationManager.cancel(notificationId);
    }


}
