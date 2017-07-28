package com.joxad.zikobot.data.module.soundcloud.resource;

import android.content.Context;

import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.R;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by josh on 15/06/16.
 */
public class SoundCloudApiInterceptor implements Interceptor {

    private final Context context;

    public SoundCloudApiInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        HttpUrl originalHttpUrl = original.url();
        HttpUrl.Builder urlBuilder = originalHttpUrl.newBuilder();
        if (AppPrefs.getSoundCloundAccessToken().equals("")) {
            urlBuilder.addQueryParameter("client_id", context.getString(R.string.soundcloud_id));
        } else {
            urlBuilder.addQueryParameter("oauth_token", AppPrefs.getSoundCloundAccessToken());
        }
        HttpUrl url = urlBuilder.build();

        Request.Builder requestBuilder = original.newBuilder()
                .url(url)
                .headers(original.headers())
                .method(original.method(), original.body());
        Request request = requestBuilder.build();

        return chain.proceed(request);
    }
}
