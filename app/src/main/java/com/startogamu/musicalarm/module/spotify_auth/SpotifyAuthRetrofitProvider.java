package com.startogamu.musicalarm.module.spotify_auth;

import android.content.Context;

import com.startogamu.musicalarm.R;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Instance of Retrofit needed to make the call to Spotify API
 */
public class SpotifyAuthRetrofitProvider {

    public Retrofit retrofit;

    public SpotifyAuthRetrofitProvider(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.api_spotify_auth_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
