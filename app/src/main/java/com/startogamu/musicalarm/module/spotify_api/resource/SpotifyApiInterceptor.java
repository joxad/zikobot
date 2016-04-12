package com.startogamu.musicalarm.module.spotify_api.resource;

import com.startogamu.musicalarm.core.utils.AppPrefs;

import java.io.IOException;

import lombok.Data;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by josh on 11/04/16.
 */
@Data
public class SpotifyApiInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer " + AppPrefs.getAcccesToken())
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
