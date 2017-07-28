package com.joxad.zikobot.data.module.spotify_auth.model

import com.google.gson.annotations.SerializedName


class SpotifyRequestToken {
    @SerializedName("grant_type")
    val grantType: String? = null
    @SerializedName("code")
    val code: String? = null
    @SerializedName("redirect_uri")
    val redirectUri: String? = null
    @SerializedName("client_id")
    val cliendId: String? = null
    @SerializedName("client_secret")
    val clientSecret: String? = null
}
