package com.startogamu.musicalarm.module.spotifyauth;

import android.content.Context;

import com.phoceis.kioskcultura_core.R;
import com.phoceis.kioskcultura_core.module.retrofit.RetrofitBaseModule;
import com.phoceis.kioskcultura_core.module.spotifyauth.manager.SpotifyAuthManager;
import com.phoceis.kioskcultura_core.module.spotifyauth.resource.SpotifyAuthInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Fabien Baron
 * @company Phoceis
 * @email fbaron@phoceis.com
 * @date 11/04/2016
 */
@Module
public class SpotifyAuthModule extends RetrofitBaseModule {
    SpotifyAuthManager spotifyAuthManager;

    public SpotifyAuthModule(Context context) {
        super(context.getString(R.string.spotify_base_auth_url),
                new SpotifyAuthInterceptor(context.getString(R.string.api_spotify_id), context.getString(R.string.api_spotify_secret)));
        spotifyAuthManager = new SpotifyAuthManager(context, retrofit());
    }

    @Provides
    @Singleton
    SpotifyAuthManager spotifyAuthManager() {
        return spotifyAuthManager;
    }
}
