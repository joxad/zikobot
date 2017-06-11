package com.joxad.zikobot.app.player.player;

import android.content.Context;
import android.util.Log;

import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.player.event.EventNextTrack;
import com.joxad.zikobot.app.player.event.EventShowError;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager;
import com.orhanobut.logger.Logger;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

/**
 * Created by josh on 28/08/16.
 */
public class SpotifyPlayer implements IMusicPlayer {

    private final Context context;

    boolean hasRefreshed = false;
    private String lastRef;
    protected Player player;
    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            Logger.d("OK");
        }

        @Override
        public void onError(Error error) {
            Logger.e("OK");
            switch (error) {
                case kSpErrorNotActiveDevice:
                    player.removeConnectionStateCallback(connexionStateCallback);
                    player.removeNotificationCallback(notificationCallback);
                    player.addNotificationCallback(notificationCallback);
                    player.addConnectionStateCallback(connexionStateCallback);
                    player.login(AppPrefs.getSpotifyAccessToken());
                    break;
                case kSpErrorNeedsPremium:
                    EventBus.getDefault().post(new EventShowError(context.getString(R.string.error_spotify_premium)));
                    break;
                case kSpErrorCorruptTrack:
                    EventBus.getDefault().post(new EventShowError(context.getString(R.string.error_corrupt_track)));
                    break;
                default:
                    EventBus.getDefault().post(new EventShowError(context.getString(R.string.error_generic_error)));
                    break;
            }
        }
    };

    Player.NotificationCallback notificationCallback = new Player.NotificationCallback() {
        @Override
        public void onPlaybackEvent(PlayerEvent playerEvent) {
            if (playerEvent == PlayerEvent.kSpPlaybackNotifyAudioDeliveryDone)
                EventBus.getDefault().post(new EventNextTrack());
        }

        @Override
        public void onPlaybackError(Error error) {

        }
    };

    private ConnectionStateCallback connexionStateCallback = new ConnectionStateCallback() {
        @Override
        public void onLoggedIn() {
            Logger.d("loggedin");
            if (lastRef != null) {
                player.playUri(mOperationCallback, lastRef, 0, 0);
                lastRef = null;
            }
        }

        @Override
        public void onLoggedOut() {
            player.login(AppPrefs.getSpotifyAccessToken());

            Logger.d("LoggedOut");
        }

        @Override
        public void onLoginFailed(Error error) {
            Logger.d("onLoginFailed %s", error.toString());

            switch (error) {
                case kSpErrorNeedsPremium:
                    EventBus.getDefault().post(new EventShowError(context.getString(R.string.error_spotify_premium)));
                    break;
            }
        }


        @Override
        public void onTemporaryError() {
            Logger.d("onTemporaryError");
        }

        @Override
        public void onConnectionMessage(String s) {
            Logger.d("onConnectionMessage %s", s);
        }
    };


    public SpotifyPlayer(Context context) {
        this.context = context;
    }

    @Override
    public void init() {
        Config playerConfig = new Config(context, AppPrefs.getSpotifyAccessToken(), context.getString(R.string.api_spotify_id));
        Spotify.getPlayer(playerConfig, this, new com.spotify.sdk.android.player.SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(com.spotify.sdk.android.player.SpotifyPlayer pl) {
                player = pl;
                player.addNotificationCallback(notificationCallback);
                player.addConnectionStateCallback(connexionStateCallback);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(SpotifyPlayer.class.getSimpleName(), "Could not initialize player: " + throwable.getMessage());

            }
        });

    }

    @Override
    public void play(String ref) {
        lastRef = ref;
        try {
            SpotifyAuthManager.getInstance().refreshToken(context, (newToken, tokenIdentical) -> {
                if (!tokenIdentical) {
                    hasRefreshed = true;
                    player.playUri(mOperationCallback, ref, 0, 0);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void pause() {
        player.pause(mOperationCallback);
    }

    @Override
    public void resume() {
        player.resume(mOperationCallback);
    }

    @Override
    public void stop() {
        if (player != null)
            player.pause(mOperationCallback);

    }

    @Override
    public void seekTo(float percent) {

    }

    @Override
    public void seekTo(int position) {
        player.seekToPosition(mOperationCallback, position);
    }

}
