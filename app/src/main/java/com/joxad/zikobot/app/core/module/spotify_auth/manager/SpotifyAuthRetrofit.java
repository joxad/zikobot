package com.joxad.zikobot.app.core.module.spotify_auth.manager;

import android.support.annotation.Nullable;

import com.joxad.zikobot.app.core.ws.RetrofitBase;

import okhttp3.Interceptor;

/**
 * Created by Jocelyn on 31/10/2016.
 */

public class SpotifyAuthRetrofit extends RetrofitBase {
    public SpotifyAuthRetrofit(String url, @Nullable Interceptor interceptor) {
        super(url, interceptor);
    }
}
