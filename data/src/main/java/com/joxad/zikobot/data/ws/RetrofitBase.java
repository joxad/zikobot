package com.joxad.zikobot.data.ws;

import android.support.annotation.Nullable;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by josh on 11/04/16.
 */
public class RetrofitBase {

    private Retrofit retrofit;

    public RetrofitBase(String url) {
        this(url, null, null);
    }

    public RetrofitBase(String url, Interceptor interceptor) {
        this(url, interceptor, null);
    }

    public RetrofitBase(String url, @Nullable Interceptor interceptor, @Nullable Authenticator authenticator) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(httpLoggingInterceptor);

        if (authenticator != null)
            builder.authenticator(authenticator);
        if (interceptor != null)
            builder.addInterceptor(interceptor);


        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Retrofit retrofit() {
        return retrofit;
    }

    public <T> T create(Class<T> tClass) {
        return retrofit.create(tClass);
    }

}
