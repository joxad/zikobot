package com.startogamu.musicalarm.module.spotify_api;

import android.content.Context;

import com.startogamu.musicalarm.R;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Generate the instance needed of Retrofit in order to call the {@link com.startogamu.musicalarm.network.spotify_api.SpotifyAPIService}
 */
public class SpotifyAPIRetrofitProvider {

    Retrofit retrofit;

    /***
     * Generate the retrofit instance needed to make call to spotify pai
     *
     * @param context
     */
    public SpotifyAPIRetrofitProvider(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.api_spotify_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
