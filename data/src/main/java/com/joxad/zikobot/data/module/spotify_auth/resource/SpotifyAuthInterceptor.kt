package com.joxad.zikobot.data.module.spotify_auth.resource

import android.util.Base64

import java.io.IOException

import lombok.AllArgsConstructor
import lombok.Data
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/***

 */
class SpotifyAuthInterceptor(private val clientId: String, private val clientSecret: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        var header = "Basic "

        val id_secret = clientId + ":" + clientSecret
        val b64 = Base64.encodeToString(id_secret.toByteArray(), Base64.NO_WRAP)
        header += b64

        val requestBuilder = original.newBuilder()
                .header("Authorization", header)
                .method(original.method(), original.body())

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
