package com.startogamu.musicalarm.di.manager;

import com.joxad.android_easy_spotify.SpotifyPlayerManager;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.startogamu.musicalarm.model.AlarmTrack;

import java.util.ArrayList;

/**
 * {@link PlayerMusicManager} will handle the change of track according to the type of alarm track that is used
 * If a spotify track is finished and the next one is a local one, it is his job to deal with it
 */
public class PlayerMusicManager {

    SpotifyPlayerManager spotifyPlayerManager;

    /***
     *
     * @param alarmTrack
     */
    public void playAlarmTrack(final AlarmTrack alarmTrack) {
        switch (alarmTrack.getType()){
            case AlarmTrack.TYPE.LOCAL:
                break;
            case AlarmTrack.TYPE.SPOTIFY:
                SpotifyPlayerManager.play(alarmTrack.getRef());
        }

    }


    public void setListener() {
        SpotifyPlayerManager.setPlayerNotificationCallback(new PlayerNotificationCallback() {
            @Override
            public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
                if (eventType == EventType.PAUSE) {

                }
            }

            @Override
            public void onPlaybackError(ErrorType errorType, String s) {

            }
        });
    }


}
