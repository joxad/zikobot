package com.joxad.zikobot.data.module.accounts

import com.joxad.zikobot.data.AppPrefs
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyUser
import com.joxad.zikobot.data.module.spotify_auth.model.SpotifyToken
import io.reactivex.subjects.PublishSubject

/**
 * Created by Jocelyn on 06/08/2017.
 */

enum class AccountManager {

    INSTANCE;

    lateinit var spotifyUserSubject: PublishSubject<SpotifyUser>

    fun init() {
        spotifyUserSubject = PublishSubject.create()
    }

    fun onSpotifyLogged(spotifyUser: SpotifyUser) {
        AppPrefs.spotifyUser(spotifyUser)

        spotifyUserSubject.onNext(spotifyUser)
    }


    fun clearSpotify() {
        AppPrefs.spotifyUser(null)
        AppPrefs.saveAccessToken(null)
        AppPrefs.saveRefreshToken(null)
        spotifyUserSubject.onNext(SpotifyUser())
    }

    fun onSpotifyReceiveToken(token: SpotifyToken?) {
        if (token?.accessToken != null)
            AppPrefs.saveAccessToken(token.accessToken)
        if (token?.refreshToken != null)
            AppPrefs.saveRefreshToken(token.refreshToken)
    }

}
