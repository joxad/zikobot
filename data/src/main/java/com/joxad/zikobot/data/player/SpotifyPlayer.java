package com.joxad.zikobot.data.player;

import android.content.Context;
import android.util.Log;

import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.R;
import com.joxad.zikobot.data.db.CurrentPlaylistManager;
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;

import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by josh on 28/08/16.
 */
public class SpotifyPlayer implements IMusicPlayer {

    private final Context context;

    private boolean hasRefreshed = false;
    private String lastRef;
    protected Player player;
    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            Log.d("ZIKOBOT", "mOperationCallback success");

        }

        @Override
        public void onError(Error error) {
            Log.d("ZIKOBOT", "mOperationCallback error" + error);

            switch (error) {
                case kSpErrorNotActiveDevice:
                    break;
                case kSpErrorNeedsPremium:
                    //   EventBus.getDefault().post(new EventShowError(context.getString(R.string.error_spotify_premium)));
                    break;
                case kSpErrorCorruptTrack:
                    //   EventBus.getDefault().post(new EventShowError(context.getString(R.string.error_corrupt_track)));
                    break;
                default:
                    //    EventBus.getDefault().post(new EventShowError(context.getString(R.string.error_generic_error)));
                    break;
            }
        }
    };

    Player.NotificationCallback notificationCallback = new Player.NotificationCallback() {
        @Override
        public void onPlaybackEvent(PlayerEvent playerEvent) {
            Log.d("ZIKOBOT", "notificationCallback playerEvent" + playerEvent.name());

            if (playerEvent == PlayerEvent.kSpPlaybackNotifyAudioDeliveryDone) {
                CurrentPlaylistManager.INSTANCE.next();
            }
        }

        @Override
        public void onPlaybackError(Error error) {
            Log.d("ZIKOBOT", "notificationCallback playerError" + error.name());

            Log.e(SpotifyPlayer.class.getSimpleName(), error.name());
        }
    };

    private ConnectionStateCallback connexionStateCallback = new ConnectionStateCallback() {
        @Override
        public void onLoggedIn() {
            Log.d("ZIKOBOT", "connexionStateCallback spotify login");
            if (lastRef != null)
                player.playUri(mOperationCallback, lastRef, 0, 0);

        }

        @Override
        public void onLoggedOut() {
            if (AppPrefs.getSpotifyAccessToken() != null)
                player.login(AppPrefs.getSpotifyAccessToken());
        }

        @Override
        public void onLoginFailed(Error error) {
            Log.d("ZIKOBOT", "connexionStateCallback spotify message error" + error.name());

            switch (error) {
                case kSpErrorNeedsPremium:
                    //  EventBus.getDefault().post(new EventShowError(context.getString(R.string.error_spotify_premium)));
                    break;
                default:
                    break;
            }
        }


        @Override
        public void onTemporaryError() {
            Log.d("ZIKOBOT", "connexionStateCallback spotify message temporary error");

        }

        @Override
        public void onConnectionMessage(String s) {
            Log.d("ZIKOBOT", "connexionStateCallback  message " + s);
        }
    };


    public SpotifyPlayer(Context context) {
        this.context = context;
    }

    @Override
    public void init() {

    }

    private void refreshPlayer() {
        Config playerConfig = new Config(context, AppPrefs.getSpotifyAccessToken(), context.getString(R.string.api_spotify_id));
        Spotify.getPlayer(playerConfig, this, new com.spotify.sdk.android.player.SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(com.spotify.sdk.android.player.SpotifyPlayer pl) {
                Log.d("ZIKOBOT", "InitializationObserver  message , logged ? " + pl.isLoggedIn() + " init ? " + pl.isInitialized());

                player = pl;
                player.addNotificationCallback(notificationCallback);
                player.addConnectionStateCallback(connexionStateCallback);
                if (lastRef != null && pl.isLoggedIn())
                    player.playUri(mOperationCallback, lastRef, 0, 0);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("ZIKOBOT", "InitializationObserver" + throwable.getLocalizedMessage());

            }
        });
    }

    @Override
    public void play(String ref) {
        lastRef = ref;
        long ts = new Date().getTime();
        if (ts < AppPrefs.saveSpotifyTokenExpiresIn()) {
            player.playUri(mOperationCallback, lastRef, 0, 0);
        } else {
            SpotifyAuthManager.INSTANCE.refreshToken().subscribe(spotifyToken -> {
                String oldToken = AppPrefs.getSpotifyAccessToken();
                String newToken = spotifyToken.getAccessToken();
                int timeStampToken = spotifyToken.getExpiresIn();
                AppPrefs.saveSpotifyTokenExpiresIn((int) ts + timeStampToken);
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