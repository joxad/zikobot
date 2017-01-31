package com.joxad.zikobot.app.core.module.retrofit;

import android.support.annotation.Nullable;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by josh on 11/04/16.
 */
public class RetrofitBaseModule {

    private Retrofit retrofit;

    public RetrofitBaseModule(String url) {
        this(url, null);
    }

    public RetrofitBaseModule(String url, @Nullable Interceptor interceptor) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(httpLoggingInterceptor);
        if (interceptor != null)
            builder.addInterceptor(interceptor);


        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public Retrofit retrofit() {
        return retrofit;
    }
}
