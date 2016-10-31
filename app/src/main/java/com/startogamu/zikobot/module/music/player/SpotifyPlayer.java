package com.startogamu.zikobot.module.music.player;

import android.content.Context;

import com.joxad.android_easy_spotify.SpotifyPlayerManager;
import com.orhanobut.logger.Logger;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.startogamu.zikobot.core.event.player.EventNextTrack;
import com.startogamu.zikobot.core.utils.AppPrefs;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by josh on 28/08/16.
 */
public class SpotifyPlayer implements IMusicPlayer {

    private final Context context;
    int currentPosition;

    public SpotifyPlayer(Context context) {
        this.context = context;
        init();
    }

    @Override
    public void init() {
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
                currentPosition = playerState.positionInMs;
                if (playerState.positionInMs >= playerState.durationInMs && playerState.durationInMs > 0)
                    next();
                Logger.d(String.format("Player state %s - activeDevice %s : current duration %d total duration %s", playerState.trackUri, playerState.activeDevice, playerState.positionInMs, playerState.durationInMs));
            }

            @Override
            public void onPlaybackError(ErrorType errorType, String s) {

            }
        }, null);
    }

    /***
     * Method called when the token is updated for spotify
     */
    public void refreshAccessTokenPlayer() {
        SpotifyPlayerManager.updateToken(AppPrefs.getSpotifyAccessToken());
    }


    @Override
    public void play(String ref) {
        SpotifyPlayerManager.play(ref);
    }

    @Override
    public void pause() {
        SpotifyPlayerManager.pause();
    }

    @Override
    public void resume() {
        SpotifyPlayerManager.resume();
    }

    @Override
    public void stop() {
        SpotifyPlayerManager.pause();

    }

    @Override
    public void seekTo(int position) {
        SpotifyPlayerManager.player().seekToPosition(position);
    }

    @Override
    public float position() {
        return currentPosition;
    }

    @Override
    public void next() {
        EventBus.getDefault().post(new EventNextTrack());
    }
}
