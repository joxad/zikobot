package com.joxad.zikobot.app.core.module.music.player;

import android.content.Context;
import android.util.Log;

import com.joxad.android_easy_spotify.SpotifyManager;
import com.joxad.android_easy_spotify.SpotifyPlayerManager;
import com.orhanobut.logger.Logger;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.event.player.EventNextTrack;
import com.joxad.zikobot.app.core.utils.AppPrefs;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by josh on 28/08/16.
 */
public class SpotifyPlayer implements IMusicPlayer {

    private final Context context;
    protected int currentPosition;

    protected Player player;

    public SpotifyPlayer(Context context) {
        this.context = context;
        init();
    }

    @Override
    public void init() {
        Config playerConfig = new Config(context, AppPrefs.getSpotifyAccessToken(), context.getString(R.string.api_spotify_id));
        Spotify.getPlayer(playerConfig, context, new Player.InitializationObserver() {
            @Override
            public void onInitialized(Player pl) {
                player = pl;
                player.addConnectionStateCallback(connexionStateCallback);
                player.addPlayerNotificationCallback(playerNotificationCallback);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(SpotifyManager.class.getSimpleName(), "Could not initialize player: " + throwable.getMessage());

            }
        });
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
    public int position() {
        return currentPosition;
    }

    @Override
    public int positionMax() {
        return 0;
    }

    @Override
    public void next() {
        EventBus.getDefault().post(new EventNextTrack());
    }

    @Override
    public void previous() {

    }


    PlayerNotificationCallback playerNotificationCallback = new PlayerNotificationCallback() {
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
    };

    private ConnectionStateCallback connexionStateCallback = new ConnectionStateCallback() {
        @Override
        public void onLoggedIn() {

        }

        @Override
        public void onLoggedOut() {

        }

        @Override
        public void onLoginFailed(Throwable throwable) {

        }

        @Override
        public void onTemporaryError() {

        }

        @Override
        public void onConnectionMessage(String s) {

        }
    };

}
