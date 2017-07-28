package com.joxad.zikobot.data.module.spotify_auth.model

import com.google.gson.annotations.SerializedName

class SpotifyRefreshToken(@SerializedName("client_secret") val clientSecret: String? = null, @SerializedName("client_id") val cliendId: String? = null, @SerializedName("grant_type") val grantType: String? = null, @SerializedName("refresh_token") val refreshToken: String? = null, @SerializedName("redirect_uri") val redirectUri: String? = null) {

}
