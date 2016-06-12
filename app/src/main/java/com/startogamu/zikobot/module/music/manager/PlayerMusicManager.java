package com.startogamu.zikobot.module.music.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.joxad.android_easy_spotify.SpotifyPlayerManager;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.startogamu.zikobot.core.event.TrackChangeEvent;
import com.startogamu.zikobot.core.service.MediaPlayerService;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.module.alarm.model.Alarm;
import com.startogamu.zikobot.module.alarm.model.Track;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;

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


    private ArrayList<Track> tracks = new ArrayList<>();
    private MediaPlayerService mediaPlayerService;
    private boolean mediaPlayerServiceBound = false;
    //connect to the service
    private Intent playIntent;
    private boolean enable = false;

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
            mediaPlayerService.setOnCompletionListener(mp -> {
                if (enable) next();
            });
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
                if (playerState.positionInMs >= playerState.durationInMs && playerState.durationInMs > 0 && pauseToHandle)
                    next();
                Log.d(TAG, String.format("Player state %s - activeDevice %s : current duration %d total duration %s", playerState.trackUri, playerState.activeDevice, playerState.positionInMs, playerState.durationInMs));
            }

            @Override
            public void onPlaybackError(ErrorType errorType, String s) {

            }
        }, null);
    }


    Handler handler = new Handler();
    private boolean pauseToHandle = true;

    /***
     * @param track
     */
    public void playTrack(final Track track) {
        EventBus.getDefault().post(new TrackChangeEvent(track));
        switch (track.getType()) {
            case Track.TYPE.LOCAL:
                pauseToHandle = false;
                SpotifyPlayerManager.pause();
                SpotifyPlayerManager.clear();
                handler.postDelayed(() -> pauseToHandle = true, 500);
                mediaPlayerService.playSong(Uri.parse(track.getRef()));
                break;
            case Track.TYPE.SPOTIFY:
                mediaPlayerService.stop();
                SpotifyPlayerManager.play(track.getRef());
                break;
        }

    }


    /***
     *
     */
    public void next() {
        currentSong++;
        if (alarm.getTracks().size() > currentSong)
            playTrack(alarm.getTracks().get(currentSong));
    }

    /***
     * @param alarm
     */
    public void startAlarm(Alarm alarm) {
        enable = true;
        currentSong = 0;
        this.alarm = alarm;
        this.tracks.clear();
        this.tracks.addAll(alarm.getTracks());
        if (alarm.getRandomTrack() == 1) {
            Collections.shuffle(tracks);
        }
        playTrack(tracks.get(currentSong));
    }

    /***
     * Stop all the players
     */
    public void stop() {
        enable = false;
        SpotifyPlayerManager.pause();
        mediaPlayerService.stop();
        currentSong = 0;

    }

    /***
     *
     */
    public void resume() {
        mediaPlayerService.resume();
        SpotifyPlayerManager.resume();
    }

    /**
     * pause all the players
     */
    public void pause() {
        mediaPlayerService.pause();
        SpotifyPlayerManager.pause();
    }
}
