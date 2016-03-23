package com.startogamu.musicalarm.module.spotify_auth;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module that will generate the retrofit class to handle the Oauth to Spotify
 */
@Module(complete = false, library = true)
public class SpotifyAuthNetworkModule {

    @Provides
    @Singleton
    public SpotifyAuthRetrofitProvider provideRetrofitSpotifyAuth(final Application context) {
        return new SpotifyAuthRetrofitProvider(context);
    }
}
