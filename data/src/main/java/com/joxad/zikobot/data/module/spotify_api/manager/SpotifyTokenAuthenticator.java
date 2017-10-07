package com.joxad.zikobot.data.module.spotify_api.manager;

import android.content.Context;
import android.support.annotation.Nullable;

import com.joxad.zikobot.data.module.accounts.AccountManager;
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager;
import com.joxad.zikobot.data.module.spotify_auth.model.SpotifyToken;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class SpotifyTokenAuthenticator implements Authenticator {

    private Context context;

    public SpotifyTokenAuthenticator(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        // Refresh your access_token using a synchronous api request
        SpotifyToken spotifyToken = SpotifyAuthManager.INSTANCE.refreshToken().toObservable().blockingFirst();
        AccountManager.INSTANCE.onSpotifyReceiveToken(spotifyToken);
        // Add new header to rejected request and retry it
        return response.request().newBuilder()
                .header("Authorization", "Bearer " + spotifyToken.getAccessToken())
                .build();
    }
}