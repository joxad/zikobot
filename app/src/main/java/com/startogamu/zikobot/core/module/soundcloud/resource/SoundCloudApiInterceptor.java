package com.startogamu.zikobot.core.module.soundcloud.resource;

import android.content.Context;

import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.AppPrefs;

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
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SoundCloudApiInterceptor implements Interceptor {

    private final Context context;

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
