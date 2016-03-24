package com.startogamu.musicalarm.module.spotify_api;


import com.startogamu.musicalarm.network.spotify_api.SpotifyAPIService;

import dagger.Module;
import dagger.Provides;

/**
 * Provide the api service that will be used in
 * {@link com.startogamu.musicalarm.manager.spotify_api.SpotifyAPIManager}
 */
@Module
public class SpotifyAPIServiceModule {

    @Provides
    SpotifyAPIService provideAPIService(SpotifyAPIRetrofitProvider spotifyAPIRetrofit) {
        return spotifyAPIRetrofit.retrofit.create(SpotifyAPIService.class);
    }

}
