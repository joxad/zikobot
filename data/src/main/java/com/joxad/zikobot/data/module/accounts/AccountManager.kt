package com.joxad.zikobot.data.module.accounts

import com.joxad.zikobot.data.AppPrefs
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyUser
import com.joxad.zikobot.data.module.spotify_auth.model.SpotifyToken
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by Jocelyn on 06/08/2017.
 */

enum class AccountManager {

    INSTANCE;

    lateinit var spotifyUserBehaviorSubject: BehaviorSubject<SpotifyUser>

    fun init() {
        spotifyUserBehaviorSubject = BehaviorSubject.create()
    }

    fun onSpotifyLogged(spotifyUser: SpotifyUser) {
        AppPrefs.spotifyUser(spotifyUser)

        spotifyUserBehaviorSubject.onNext(spotifyUser)
    }


    fun clearSpotify() {
        AppPrefs.spotifyUser(null)
        AppPrefs.saveAccessToken(null)
        AppPrefs.saveRefreshToken(null)
        spotifyUserBehaviorSubject.onNext(SpotifyUser())
    }

    fun onSpotifyReceiveToken(token: SpotifyToken?) {
        AppPrefs.saveAccessToken(token?.accessToken)
        AppPrefs.saveRefreshToken(token?.refreshToken)
    }

}
