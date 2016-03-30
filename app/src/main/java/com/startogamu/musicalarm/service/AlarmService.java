package com.startogamu.musicalarm.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.joxad.android_easy_spotify.SpotifyManager;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.di.manager.spotify_auth.SpotifyAuthManager;
import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.utils.EXTRA;
import com.startogamu.musicalarm.utils.SpotifyPrefs;

import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

/**
 * Created by josh on 28/03/16.
 */
public class AlarmService extends Service {


    private static final String TAG = AlarmService.class.getSimpleName();
    SpotifyManager spotifyManager;

    @Inject
    SpotifyAuthManager spotifyAuthManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        MusicAlarmApplication.get(this).netComponent.inject(this);
        Alarm alarm = Parcels.unwrap(intent.getParcelableExtra(EXTRA.ALARM));
        showNotification(alarm);
        try {
            spotifyAuthManager.refreshToken(getApplicationContext(), () -> {
                SpotifyManager.startPlayer(SpotifyPrefs.getAcccesToken(), new ConnectionStateCallback() {
                    @Override
                    public void onLoggedIn() {
                        Log.d(TAG, "Logged in");
                    }

                    @Override
                    public void onLoggedOut() {
                        Log.d(TAG, "Logged out");

                    }

                    @Override
                    public void onLoginFailed(Throwable throwable) {
                        Log.d(TAG, "Logged failed");

                    }

                    @Override
                    public void onTemporaryError() {
                        Log.d(TAG, "temp error");

                    }

                    @Override
                    public void onConnectionMessage(String s) {
                        Log.d(TAG, "Connection message " + s);

                    }
                }, new PlayerNotificationCallback() {
                    @Override
                    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {

                    }

                    @Override
                    public void onPlaybackError(ErrorType errorType, String s) {

                    }
                });
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return Service.START_STICKY;
    }

    /***
     * Alarm notification
     *
     * @param alarm
     */
    private void showNotification(Alarm alarm) {

        Notification n = new Notification.Builder(this)
                .setContentTitle(alarm.getName())
                .setContentText(String.format("%d H %2d", alarm.getHour(), alarm.getMinute()))
                .setSmallIcon(R.drawable.ic_queue_music)
                .setAutoCancel(false)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
