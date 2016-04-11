package com.startogamu.musicalarm.core.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.joxad.android_easy_spotify.SpotifyManager;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;
import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.notification.MusicNotification;
import com.startogamu.musicalarm.module.alarm.AlarmManager;
import com.startogamu.musicalarm.di.manager.spotify_auth.SpotifyAuthManager;
import com.startogamu.musicalarm.module.alarm.Alarm;
import com.startogamu.musicalarm.module.alarm.AlarmTrack;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.core.utils.SpotifyPrefs;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by josh on 28/03/16.
 */
public class AlarmService extends Service {

    Alarm alarm;

    private static final String TAG = AlarmService.class.getSimpleName();
    SpotifyManager spotifyManager;

    @Inject
    SpotifyAuthManager spotifyAuthManager;

    /***
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        MusicAlarmApplication.get(this).netComponent.inject(this);
        long alarmId = intent.getLongExtra(EXTRA.ALARM_ID, -1);
        AlarmManager.getAlarmById(alarmId).subscribe((alarm) -> {
            AlarmService.this.alarm = alarm;
            MusicNotification.show(alarm.getName());
            startPlayer();
        });
        return Service.START_STICKY;
    }

    /***
     *
     */
    private void startPlayer() {
        try {
            spotifyAuthManager.refreshToken(getApplicationContext(), () -> {
                Config playerConfig = new Config(AlarmService.this.getApplicationContext(), SpotifyPrefs.getAcccesToken(), getString(R.string.api_spotify_id));

                Spotify.getPlayer(playerConfig, AlarmService.this.getApplicationContext(), new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player pl) {
                        ArrayList<String> uris = new ArrayList<String>();
                        for (AlarmTrack alarmTrack : alarm.getTracks()) {
                            uris.add(alarmTrack.getRef());
                        }
                        pl.play(uris);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e(SpotifyManager.class.getSimpleName(), "Could not initialize player: " + throwable.getMessage());

                    }
                });

            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
