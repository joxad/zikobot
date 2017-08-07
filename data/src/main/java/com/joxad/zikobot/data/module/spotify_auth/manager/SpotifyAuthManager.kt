package com.joxad.zikobot.data.module.spotify_auth.manager

import android.content.Context
import com.joxad.zikobot.data.AppPrefs
import com.joxad.zikobot.data.R
import com.joxad.zikobot.data.module.accounts.AccountManager
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

    fun requestToken(context: Context): Observable<SpotifyToken> {
        val code = AppPrefs.getSpotifyAccessCode()
        val grant_type = "authorization_code"
        val redirect = context.getString(R.string.zikobot_callback)
        return spotifyAuthService.requestToken(code, grant_type, redirect)
                .flatMap({
                    AccountManager.INSTANCE.onSpotifyReceiveToken(it)
                    return@flatMap Observable.just(it)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun refreshToken(): Observable<SpotifyToken> {
        val refreshToken = AppPrefs.getRefreshToken()
        return spotifyAuthService.refreshToken(refreshToken,
                "refresh_token")
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
