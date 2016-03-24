package com.startogamu.musicalarm.di.module.spotify_auth;

import android.content.Context;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.di.module.BaseRetrofitProfider;

/**
 * Instance of Retrofit needed to make the call to Spotify API
 */
/**
 * Instance of Retrofit needed to make the call to Spotify API
 */
public class SpotifyAuthRetrofitProvider extends BaseRetrofitProfider {

    /***
     * Generate the retrofit instance needed to make call to spotify pai
     *
     * @param context
     */
    public SpotifyAuthRetrofitProvider(Context context) {
        super(context.getString(R.string.api_spotify_auth_url));
    }
}
