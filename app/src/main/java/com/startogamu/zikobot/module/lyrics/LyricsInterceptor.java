package com.startogamu.zikobot.module.lyrics;

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
public class LyricsInterceptor implements Interceptor {

    private final Context context;

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
