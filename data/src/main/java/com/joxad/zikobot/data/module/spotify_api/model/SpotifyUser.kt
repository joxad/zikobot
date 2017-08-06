package com.joxad.zikobot.data.module.spotify_api.model

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

/**
 * Created by josh on 11/03/16.
 */
class SpotifyUser {

    @SerializedName("country")
    var country: String? = null
    @SerializedName("display_name")
    var displayName: String? = null
    @SerializedName("href")
    var href: String? = null
    @SerializedName("id")
    var id: String? = null
    @SerializedName("images")
    var images: List<Image> = ArrayList()
    @SerializedName("product")
    var product: String? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("uri")
    var uri: String? = null



}
