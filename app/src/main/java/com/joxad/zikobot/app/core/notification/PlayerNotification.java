package com.joxad.zikobot.app.core.notification;

import android.app.Notification;
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
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.receiver.NotificationDismissedReceiver;
import com.joxad.zikobot.app.core.receiver.PausePlayerReceiver;
import com.joxad.zikobot.app.core.receiver.ResumePlayerReceiver;
import com.joxad.zikobot.app.core.receiver.StopPlayerReceiver;
import com.joxad.zikobot.app.home.ActivityMain;
import com.joxad.zikobot.data.model.Track;

import java.util.ArrayList;

import lombok.Getter;

/**
 * Created by josh on 13/06/16.
 */
public class PlayerNotification {
    private final PendingIntent viewPendingIntent;
    private final ArrayList actions;
    @Getter
    private int notificationId = 42;
    @Getter
    private int notificationIdWear = 43;

    private Context context;
    private NotificationManagerCompat notificationManager;
    private RemoteViews playerViewSmall;

    public PlayerNotification(Context context) {
        this.context = context;
        // Get an instance of the NotificationManager service
        notificationManager = NotificationManagerCompat.from(context);
        Intent viewIntent = new Intent(context, ActivityMain.class);
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

    public void prepareNotification(Track track) {
// Build intent for notification content

        NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender()
                .setDisplayIntent(viewPendingIntent).addActions(actions)
                .setCustomSizePreset(Notification.WearableExtender.SIZE_FULL_SCREEN);
        Glide.with(context)
                .load(track.getImageUrl())
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

    private PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("notificationId", notificationId);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        notificationId, intent, 0);
        return pendingIntent;
    }


    private void showNotification(Track track, NotificationCompat.WearableExtender extender, PendingIntent intent, @Nullable Bitmap bitmap) {
        Intent intentStop = new Intent(context, StopPlayerReceiver.class);
        PendingIntent pIntentStop = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intentStop, PendingIntent.FLAG_UPDATE_CURRENT);

        initSmallNotification(pIntentStop, track, bitmap);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(track.getName())
                        .setContentText(track.getArtistName())
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContent(playerViewSmall)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(false)
                        .setDeleteIntent(createOnDismissedIntent(context, notificationId))
                        .setLargeIcon(bitmap)
                        .extend(extender)
                        .setContentIntent(intent);


        // GoogleApiManager.INSTANCE.sendTrack(track, bitmap);
// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }


    /***
     * @param pIntent
     * @param track
     */
    private void initSmallNotification(PendingIntent pIntent, Track track, Bitmap bitmap) {
        playerViewSmall = new RemoteViews(context.getPackageName(), R.layout.view_notification_player_small);
        playerViewSmall.setOnClickPendingIntent(R.id.iv_stop, pIntent);
        playerViewSmall.setTextViewText(R.id.tv_artist, track.getArtistName());
        playerViewSmall.setTextViewText(R.id.tv_track, track.getName());
        playerViewSmall.setImageViewBitmap(R.id.remote_view_iv_track, bitmap);
    }


    /***
     *
     */
    public void cancel() {
        notificationManager.cancel(notificationId);
    }
}
