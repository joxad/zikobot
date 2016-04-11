package com.startogamu.musicalarm.module.spotify_api;

import android.content.Context;


import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.module.retrofit.RetrofitBaseModule;
import com.startogamu.musicalarm.module.spotify_api.manager.SpotifyApiManager;
import com.startogamu.musicalarm.module.spotify_api.resource.SpotifyApiInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by josh on 11/04/16.
 */
@Module
public class SpotifyApiModule extends RetrofitBaseModule {

    private SpotifyApiManager spotifyApiManager;

    public SpotifyApiModule(Context context) {
        super(context.getString(R.string.spotify_base_api_url), new SpotifyApiInterceptor());
        spotifyApiManager = new SpotifyApiManager(context, retrofit());
    }

    @Provides
    @Singleton
    SpotifyApiManager manager() {
        return spotifyApiManager;
    }

}
