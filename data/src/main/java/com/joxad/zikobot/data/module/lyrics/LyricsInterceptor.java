package com.joxad.zikobot.data.module.lyrics;

import android.content.Context;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by josh on 15/06/16.
 */
public class LyricsInterceptor implements Interceptor {

    private final Context context;

    public LyricsInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        HttpUrl originalHttpUrl = original.url();
        HttpUrl.Builder urlBuilder = originalHttpUrl.newBuilder();

        HttpUrl url = urlBuilder.build();

        Request.Builder requestBuilder = original.newBuilder()
                .url(url)
                .headers(original.headers())
                .method(original.method(), original.body());
        Request request = requestBuilder.build();

        return chain.proceed(request);
    }
}
