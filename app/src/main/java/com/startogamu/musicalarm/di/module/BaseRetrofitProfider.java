package com.startogamu.musicalarm.di.module;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by josh on 24/03/16.
 */
public class BaseRetrofitProfider {

    public Retrofit retrofit;
    OkHttpClient client;

    /**
     * Retrofit provider constructor used to create api + auth methods in spotify
     *
     * @param url
     * @param headerInterceptor
     */
    public BaseRetrofitProfider(final String url, final Interceptor headerInterceptor) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        if (headerInterceptor == null)
            client = builder.build();
        else
            client = builder.addInterceptor(headerInterceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


}
