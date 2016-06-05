package com.startogamu.zikobot.module.spotify_auth;

import android.content.Context;


import com.startogamu.zikobot.R;
import com.startogamu.zikobot.module.retrofit.RetrofitBaseModule;
import com.startogamu.zikobot.module.spotify_auth.manager.SpotifyAuthManager;
import com.startogamu.zikobot.module.spotify_auth.resource.SpotifyAuthInterceptor;

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
