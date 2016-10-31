package com.startogamu.zikobot.module.spotify_auth.resource;

import android.util.Base64;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/***
 *
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SpotifyAuthInterceptor implements Interceptor {

    private final String clientId;
    private final String clientSecret;


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String header = "Basic ";

        String id_secret = clientId + ":" + clientSecret;
        String b64 = Base64.encodeToString(id_secret.getBytes("UTF-8"), Base64.NO_WRAP);
        header += b64;

        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", header)
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
