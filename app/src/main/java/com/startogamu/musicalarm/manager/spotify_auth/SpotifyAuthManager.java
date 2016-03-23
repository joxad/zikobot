package com.startogamu.musicalarm.manager.spotify_auth;

import android.util.Base64;

import com.startogamu.musicalarm.model.SpotifyRequestToken;
import com.startogamu.musicalarm.model.SpotifyToken;
import com.startogamu.musicalarm.network.spotify_auth.SpotifyAuthService;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Manage that will handle the connexion to spotify to give the access token
 */
@Singleton
public class SpotifyAuthManager {

    private SpotifyAuthService spotifyAuthService;

    /***
     * @param spotifyAuthService
     */
    @Inject
    public SpotifyAuthManager(SpotifyAuthService spotifyAuthService) {
        this.spotifyAuthService = spotifyAuthService;
    }

    /***
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
}
