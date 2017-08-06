package com.joxad.zikobot.data.module.accounts

import com.joxad.zikobot.data.module.spotify_api.model.SpotifyUser

/**
 * Created by Jocelyn on 06/08/2017.
 */

class ZikoAccount(var userName: String?, var avatar: String?) {
    companion object {

        fun fromSpotify(spotifyUser: SpotifyUser?): ZikoAccount {
            var url = ""
            if (spotifyUser != null) {
                if (spotifyUser.images.isNotEmpty())
                    url = spotifyUser.images[0].url.toString()
            }
            val account = ZikoAccount(spotifyUser?.displayName, url)
            return account
        }
    }


}
