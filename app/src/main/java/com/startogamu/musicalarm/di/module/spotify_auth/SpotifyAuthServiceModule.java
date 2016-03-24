package com.startogamu.musicalarm.di.module.spotify_auth;


import com.startogamu.musicalarm.network.spotify_auth.SpotifyAuthService;

import dagger.Module;
import dagger.Provides;

/**
 * Provide the auth service that will be used in {@link com.startogamu.musicalarm.di.manager.spotify_auth.SpotifyAuthManager}
 */
@Module
public class SpotifyAuthServiceModule {

    @Provides
    SpotifyAuthService provideAuthService(SpotifyAuthRetrofitProvider spotifyAuthRetrofit) {
        return spotifyAuthRetrofit.retrofit.create(SpotifyAuthService.class);
    }

}
