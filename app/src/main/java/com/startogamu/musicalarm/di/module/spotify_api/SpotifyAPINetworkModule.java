package com.startogamu.musicalarm.di.module.spotify_api;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *  Provide an instance of Retrofit based on url of Spotify API using {@link SpotifyAPIRetrofitProvider}
 */
@Module
public class SpotifyAPINetworkModule {
    @Provides
    @Singleton
    public SpotifyAPIRetrofitProvider provideRetrofitSpotifyAPI(final Application context) {
        return new SpotifyAPIRetrofitProvider(context);
    }
}
