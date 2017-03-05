package com.joxad.zikobot.app.player.player;

import android.content.Context;
import android.util.Log;

import com.joxad.android_easy_spotify.SpotifyManager;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.player.event.EventNextTrack;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager;
import com.orhanobut.logger.Logger;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by josh on 28/08/16.
 */
public class SpotifyPlayer implements IMusicPlayer {

    private final Context context;

    protected Player player;
    PlayerNotificationCallback playerNotificationCallback = new PlayerNotificationCallback() {
        @Override
        public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
            if (playerState.positionInMs >= playerState.durationInMs && playerState.durationInMs > 0)
                EventBus.getDefault().post(new EventNextTrack());
            Logger.d(String.format("Player state %s - activeDevice %s : current duration %d total duration %s", playerState.trackUri, playerState.activeDevice, playerState.positionInMs, playerState.durationInMs));
        }

        @Override
        public void onPlaybackError(ErrorType errorType, String s) {
            Logger.e(errorType.name() + " " + s);
        }
    };
    private ConnectionStateCallback connexionStateCallback = new ConnectionStateCallback() {
        @Override
        public void onLoggedIn() {
            Logger.d("LoggedIn");
        }

        @Override
        public void onLoggedOut() {
            Logger.d("LoggedOut");
        }

        @Override
        public void onLoginFailed(Throwable throwable) {
            Logger.d("onLoginFailed %s", throwable.getLocalizedMessage());
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

    @Override
    public void play(String ref) {
        player.play(ref);
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void resume() {
        player.resume();
    }

    @Override
    public void stop() {
        player.pause();

    }

    @Override
    public void seekTo(float percent) {

    }

    @Override
    public void seekTo(int position) {
        player.seekToPosition(position);
    }

    public Observable<Boolean> updateToken() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    SpotifyAuthManager.getInstance().refreshToken(context, (newToken, tokenIdentical) -> {
                        if (!tokenIdentical) {
                            Config playerConfig = new Config(context, newToken, context.getString(R.string.api_spotify_id));
                            Spotify.getPlayer(playerConfig, context, new Player.InitializationObserver() {
                                @Override
                                public void onInitialized(Player pl) {
                                    player = pl;
                                    subscriber.onNext(true);
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    subscriber.onError(throwable);
                                }
                            });
                        } else {
                            subscriber.onNext(true);
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    subscriber.onError(new Throwable("UnsupportedEncodingException"));
                }
            }
        });
    }
}
