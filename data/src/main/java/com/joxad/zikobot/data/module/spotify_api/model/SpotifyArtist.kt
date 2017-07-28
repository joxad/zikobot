package com.joxad.zikobot.data.module.spotify_api.model

import com.google.gson.annotations.SerializedName

/**
 * Created by josh on 01/04/16.
 */
class SpotifyArtist {

    @SerializedName("href")
    val href: String? = null
    @SerializedName("id")
    val id: String? = null
    @SerializedName("name")
    val name: String? = null
    @SerializedName("type")
    val type: String? = null
    @SerializedName("uri")
    val uri: String? = null
    @SerializedName("images")
    val images: List<Image>? = null
}
