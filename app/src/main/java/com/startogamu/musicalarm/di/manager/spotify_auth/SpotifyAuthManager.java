package com.startogamu.musicalarm.di.manager.spotify_auth;

import android.content.Context;
import android.util.Base64;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.model.spotify.SpotifyRefreshToken;
import com.startogamu.musicalarm.model.spotify.SpotifyRequestToken;
import com.startogamu.musicalarm.model.spotify.SpotifyToken;
import com.startogamu.musicalarm.network.spotify_auth.SpotifyAuthService;
import com.startogamu.musicalarm.utils.SpotifyPrefs;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;
import lombok.Setter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Manage that will handle the connexion to spotify to give the access token
 */
@Singleton
public class SpotifyAuthManager {

    private SpotifyAuthService spotifyAuthService;

    @Getter
    @Setter
    private String code;

    /***
     * @param spotifyAuthService
     */
    @Inject
    public SpotifyAuthManager(SpotifyAuthService spotifyAuthService) {
        this.spotifyAuthService = spotifyAuthService;
    }

    /***
     * Ask a token using the code given by Spotify (works only one time)
     *
     * @param spotifyRequestToken
     * @param subscriber
     * @throws UnsupportedEncodingException
     */
    public void requestToken(final SpotifyRequestToken spotifyRequestToken, Subscriber<SpotifyToken> subscriber) throws UnsupportedEncodingException {

        String header = "Basic ";
        String id_secret = spotifyRequestToken.getCliendId() + ":" + spotifyRequestToken.getClientSecret();
        String b64 = Base64.encodeToString(id_secret.getBytes("UTF-8"), Base64.NO_WRAP);
        header += b64;
        spotifyAuthService.requestToken(header.trim(),
                spotifyRequestToken.getCode(),
                spotifyRequestToken.getGrantType(),
                spotifyRequestToken.getRedirectUri())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }


    public void refreshToken(Context context) throws UnsupportedEncodingException {

        SpotifyRefreshToken spotifyRefreshToken = new SpotifyRefreshToken("refresh_token", SpotifyPrefs.getRefreshToken(),
                context.getString(R.string.api_spotify_callback_musics),
                context.getString(R.string.api_spotify_id),
                context.getString(R.string.api_spotify_secret));
        String header = "Basic ";
        String id_secret = spotifyRefreshToken.getCliendId() + ":" + spotifyRefreshToken.getClientSecret();
        String b64 = Base64.encodeToString(id_secret.getBytes("UTF-8"), Base64.NO_WRAP);
        header += b64;
        spotifyAuthService.refreshToken(header.trim(),
                spotifyRefreshToken.getRefreshToken(),
                spotifyRefreshToken.getGrantType(),
                spotifyRefreshToken.getRedirectUri())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SpotifyToken>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SpotifyToken spotifyToken) {
                        SpotifyPrefs.saveAccessToken(spotifyToken.getAccessToken());
                    }
                });
    }
}
