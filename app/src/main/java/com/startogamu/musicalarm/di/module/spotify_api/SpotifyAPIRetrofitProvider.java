package com.startogamu.musicalarm.di.module.spotify_api;

import android.content.Context;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.di.module.BaseRetrofitProfider;
import com.startogamu.musicalarm.network.spotify_api.HeaderInterceptor;

/**
 * Generate the instance needed of Retrofit in order to call the {@link com.startogamu.musicalarm.network.spotify_api.SpotifyAPIService}
 */
public class SpotifyAPIRetrofitProvider extends BaseRetrofitProfider {

    /***
     * Generate the retrofit instance needed to make call to spotify pai
     *
     * @param context
     */
    public SpotifyAPIRetrofitProvider(Context context) {
        super(context.getString(R.string.api_spotify_url), new HeaderInterceptor());

    }
}
