package com.startogamu.musicalarm.network.spotify_api;

import com.startogamu.musicalarm.core.utils.AppPrefs;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Createald by josh on 31/03/16.
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        if (originalRequest.header("Bearer ") != null) {
            return chain.proceed(originalRequest);
        }
        Request request = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + AppPrefs.getAccessToken())
                .build();
        return chain.proceed(request);
    }
}
