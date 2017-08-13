package com.joxad.zikobot.data.player;

import android.content.Context;
import android.util.Log;

import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.R;
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;

import java.net.UnknownHostException;

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
            lastRef = null;
        }

        @Override
        public void onError(Error error) {
            switch (error) {
                case kSpErrorNotActiveDevice:
                    tryAgain();
                    break;
                case kSpErrorNeedsPremium:
                    //   EventBus.getDefault().post(new EventShowError(context.getString(R.string.error_spotify_premium)));
                    break;
                case kSpErrorCorruptTrack:
                    //   EventBus.getDefault().post(new EventShowError(context.getString(R.string.error_corrupt_track)));
                    break;
                default:
                    tryAgain();
                    //    EventBus.getDefault().post(new EventShowError(context.getString(R.string.error_generic_error)));
                    break;
            }
        }
    };

    private int nbFail;

    private void tryAgain() {
        nbFail++;
        if (nbFail < 10) {
            play(lastRef);
        } else {
            //   EventBus.getDefault().post(new EventSpotifyFail());
        }
    }

    Player.NotificationCallback notificationCallback = new Player.NotificationCallback() {
        @Override
        public void onPlaybackEvent(PlayerEvent playerEvent) {
            if (playerEvent == PlayerEvent.kSpPlaybackNotifyAudioDeliveryDone) {

            }
            //  EventBus.getDefault().post(new EventNextTrack());
        }

        @Override
        public void onPlaybackError(Error error) {
            Log.e(SpotifyPlayer.class.getSimpleName(), error.name());
        }
    };

    private ConnectionStateCallback connexionStateCallback = new ConnectionStateCallback() {
        @Override
        public void onLoggedIn() {
            //  Logger.d("loggedin");
            if (lastRef != null) {
                player.playUri(mOperationCallback, lastRef, 0, 0);
            }
        }

        @Override
        public void onLoggedOut() {
            player.login(AppPrefs.getSpotifyAccessToken());
        }

        @Override
        public void onLoginFailed(Error error) {

            switch (error) {
                case kSpErrorNeedsPremium:
                    //  EventBus.getDefault().post(new EventShowError(context.getString(R.string.error_spotify_premium)));
                    break;
                default:
                    tryAgain();
                    break;
            }
        }


        @Override
        public void onTemporaryError() {
            nbFail++;
            if (nbFail > 5) {
                //     EventBus.getDefault().post(new EventSpotifyFail());
            }
            // Logger.d("onTemporaryError");
        }

        @Override
        public void onConnectionMessage(String s) {
            //       Logger.d("onConnectionMessage %s", s);
        }
    };


    public SpotifyPlayer(Context context) {
        this.context = context;
    }

    @Override
    public void init() {
        nbFail = 0;

    }

    private void refreshPlayer() {
        Config playerConfig = new Config(context, AppPrefs.getSpotifyAccessToken(), context.getString(R.string.api_spotify_id));
        Spotify.getPlayer(playerConfig, this, new com.spotify.sdk.android.player.SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(com.spotify.sdk.android.player.SpotifyPlayer pl) {
                player = pl;
                nbFail = 0;
                player.addNotificationCallback(notificationCallback);
                player.addConnectionStateCallback(connexionStateCallback);
                if (lastRef != null)
                    player.playUri(mOperationCallback, lastRef, 0, 0);
            }

            @Override
            public void onError(Throwable throwable) {
                nbFail++;
                Log.e(SpotifyPlayer.class.getSimpleName(), "Could not initialize player: " + throwable.getMessage());

            }
        });
    }

    @Override
    public void play(String ref) {
        lastRef = ref;

        SpotifyAuthManager.INSTANCE.refreshToken().subscribe(spotifyToken -> {
            String oldToken = AppPrefs.getSpotifyAccessToken();
            String newToken = spotifyToken.getAccessToken();
            AppPrefs.saveAccessToken(newToken);
            if (!oldToken.equals(newToken)) {
                hasRefreshed = true;
                refreshPlayer();
            }
        }, throwable -> {
            if (throwable instanceof UnknownHostException) {
                // NO INTERNET => handle this case
                //  EventBus.getDefault().post(new EventSpotifyFail());
                // EventBus.getDefault().post(new EventNoInternet());
            } else {

            }
            //  Logger.e(SpotifyPlayer.class.getName(), throwable.getLocalizedMessage());
        });


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