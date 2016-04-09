package com.startogamu.musicalarm.di.manager;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.joxad.android_easy_spotify.SpotifyPlayerManager;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.startogamu.musicalarm.core.utils.SpotifyPrefs;
import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.model.AlarmTrack;

import java.io.IOException;

/**
 * {@link PlayerMusicManager} will handle the change of track according to the type of alarm track that is used
 * If a spotify track is finished and the next one is a local one, it is his job to deal with it
 */
public class PlayerMusicManager {

    private static final String TAG = PlayerMusicManager.class.getSimpleName();
    private final Alarm alarm;
    MediaPlayer mediaPlayer;
    int currentSong = 0;
    boolean spotifyPlayer = false;

    public PlayerMusicManager(Context context, MediaPlayer mediaPlayer, Alarm alarm) {
        this.mediaPlayer = mediaPlayer;
        this.alarm = alarm;
        setListener();

        SpotifyPlayerManager.startPlayer(context, SpotifyPrefs.getAcccesToken(), new Player.InitializationObserver() {
            @Override
            public void onInitialized(Player player) {
                spotifyPlayer = true;
            }

            @Override
            public void onError(Throwable throwable) {
                spotifyPlayer = false;
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
                try {
                    mediaPlayer.setDataSource(alarmTrack.getRef());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case AlarmTrack.TYPE.SPOTIFY:
                if (spotifyPlayer)
                    SpotifyPlayerManager.play(alarmTrack.getRef());
                break;
        }

    }

    /***
     * Add listener on both spotify player and normal player in order to go to the next song
     */
    public void setListener() {
        mediaPlayer.setOnCompletionListener(mp -> {
            playNextSong();
        });

    }

    /***
     *
     */
    private void playNextSong() {
        currentSong++;
        playAlarmTrack(alarm.getTracks().get(currentSong));
    }


    public void startAlarm() {
        currentSong = 0;
        playAlarmTrack(alarm.getTracks().get(currentSong));
    }
}
