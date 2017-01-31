package com.joxad.zikobot.data.module.spotify_api.resource;


import com.joxad.zikobot.data.AppPrefs;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by josh on 11/04/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SpotifyApiInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer " + AppPrefs.getSpotifyAccessToken())
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
