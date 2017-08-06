package com.joxad.zikobot.data.module.spotify_auth.manager

import android.content.Context
import android.util.Base64
import com.joxad.zikobot.data.AppPrefs
import com.joxad.zikobot.data.R
import com.joxad.zikobot.data.module.spotify_auth.model.SpotifyRefreshToken
import com.joxad.zikobot.data.module.spotify_auth.model.SpotifyToken
import com.joxad.zikobot.data.module.spotify_auth.resource.SpotifyAuthInterceptor
import com.joxad.zikobot.data.module.spotify_auth.resource.SpotifyAuthService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/****
 * [SpotifyAuthManager] will handle the connexion to the auth api of spotify
 */

enum class SpotifyAuthManager {
    INSTANCE;

    internal lateinit var spotifyAuthService: SpotifyAuthService

    fun init(context: Context, appId: Int, secret: Int) {
        val authRetrofit = SpotifyAuthRetrofit(context.getString(R.string.spotify_base_auth_url),
                SpotifyAuthInterceptor(context.getString(appId), context.getString(secret)))
        spotifyAuthService = authRetrofit.create(SpotifyAuthService::class.java)
    }

    fun requestToken(code: String, grant_type: String, redirect: String): Observable<SpotifyToken> {
        val contentType = "application/x-www-form-urlencoded"
        return spotifyAuthService.requestToken(contentType, code, grant_type, redirect)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun refreshToken(context: Context): Observable<SpotifyToken> {

        val spotifyRefreshToken = SpotifyRefreshToken("refresh_token", AppPrefs.getRefreshToken(),
                context.getString(R.string.api_spotify_callback_web_view),
                context.getString(R.string.api_spotify_id),
                context.getString(R.string.api_spotify_secret))
        var header = "Basic "
        val id_secret = spotifyRefreshToken.cliendId + ":" + spotifyRefreshToken.clientSecret
        val b64 = Base64.encodeToString(id_secret.toByteArray(charset("UTF-8")), Base64.NO_WRAP)
        header += b64
        return spotifyAuthService.refreshToken(
                spotifyRefreshToken.refreshToken,
                spotifyRefreshToken.grantType,
                spotifyRefreshToken.redirectUri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }


    /**
     * Listener to get info about the refresh token
     */
    interface Listener {
        fun onDone(newToken: String, tokenIdentical: Boolean)
    }


}
