package com.startogamu.musicalarm.module.music.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.joxad.android_easy_spotify.SpotifyPlayerManager;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.startogamu.musicalarm.core.service.MediaPlayerService;
import com.startogamu.musicalarm.core.utils.AppPrefs;
import com.startogamu.musicalarm.module.alarm.model.Alarm;
import com.startogamu.musicalarm.module.alarm.model.AlarmTrack;

import javax.inject.Singleton;

/**
 * {@link PlayerMusicManager} will handle the change of track according to the type of alarm track that is used
 * If a spotify track is finished and the next one is a local one, it is his job to deal with it
 */
@Singleton
public class PlayerMusicManager {

    private static final String TAG = PlayerMusicManager.class.getSimpleName();
    private final Context context;
    private Alarm alarm = null;
    int currentSong = 0;


    private MediaPlayerService mediaPlayerService;
    private boolean mediaPlayerServiceBound = false;
    //connect to the service
    private Intent playIntent;

    /***
     * @param context
     */
    public PlayerMusicManager(Context context) {
        this.context = context;
        initMediaPlayer(context);
        initSpotifyPlayer(context);
    }

    /***
     * Method called when the token is updated for spotify
     */
    public void refreshAccessTokenPlayer() {
        SpotifyPlayerManager.updateToken(AppPrefs.getSpotifyAccessToken());
    }

    /***
     * service connexion that will handle the binding with the {@link MediaPlayerService} service
     */
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MediaPlayerServiceBinder binder = (MediaPlayerService.MediaPlayerServiceBinder) service;
            //get service
            mediaPlayerService = binder.getService();
            mediaPlayerService.setOnCompletionListener(mp -> next());
            //pass list
            mediaPlayerServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mediaPlayerServiceBound = false;
        }
    };

    /***
     * @param context
     */
    public void initMediaPlayer(Context context) {
        if (playIntent == null) {
            playIntent = new Intent(context, MediaPlayerService.class);
            context.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            context.startService(playIntent);
        }
    }

    /***
     * Initialize the SpotifyPlayer according to the data found with accesstoken
     *
     * @param context
     */
    private void initSpotifyPlayer(Context context) {

        SpotifyPlayerManager.startPlayer(context, AppPrefs.getSpotifyAccessToken(), new Player.InitializationObserver() {
            @Override
            public void onInitialized(Player player) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, new PlayerNotificationCallback() {
            @Override
            public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
                if (eventType == EventType.PAUSE) {
                    Log.d(TAG, "PAUSE");
                }
                if (eventType == EventType.PLAY) {
                    Log.d(TAG, "PLAY");
                }
                if (eventType == EventType.TRACK_CHANGED) {
                    Log.d(TAG, "TRACK_CHANGED");
                }
                if (eventType == EventType.SKIP_NEXT) {
                    Log.d(TAG, "SKIP_NEXT");
                }
                if (eventType == EventType.SKIP_PREV) {
                    Log.d(TAG, "SKIP_PREV");
                }
                if (eventType == EventType.TRACK_END) {
                    Log.d(TAG, "TRACK END");
                }
                Log.d(TAG, String.format("Player state : current duration %d total duration %s", playerState.positionInMs, playerState.durationInMs));
            }

            @Override
            public void onPlaybackError(ErrorType errorType, String s) {

            }
        }, null);
    }


    /***
     * @param alarmTrack
     */
    public void playAlarmTrack(final AlarmTrack alarmTrack) {
        switch (alarmTrack.getType()) {
            case AlarmTrack.TYPE.LOCAL:
                //TODO call media player service
                mediaPlayerService.playSong(Uri.parse(alarmTrack.getRef()));
                break;
            case AlarmTrack.TYPE.SPOTIFY:

                SpotifyPlayerManager.play(alarmTrack.getRef());
                break;
        }

    }


    /***
     *
     */
    public void next() {
        currentSong++;
        if (alarm.getTracks().size() > currentSong)
            playAlarmTrack(alarm.getTracks().get(currentSong));
    }


    public void startAlarm(Alarm alarm) {
        currentSong = 0;
        this.alarm = alarm;
        playAlarmTrack(this.alarm.getTracks().get(currentSong));
    }


    public void stop() {
        SpotifyPlayerManager.pause();
        SpotifyPlayerManager.clear();
        mediaPlayerService.stop();
    }

}
