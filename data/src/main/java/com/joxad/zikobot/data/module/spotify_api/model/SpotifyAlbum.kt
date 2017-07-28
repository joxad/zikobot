package com.joxad.zikobot.data.module.spotify_api.model

import com.google.gson.annotations.SerializedName
import java.util.*

class SpotifyAlbum {

    @SerializedName("images")
    var images: List<Image> = ArrayList()
    @SerializedName("artists")
    var artists: List<SpotifyArtist> = ArrayList()
    @SerializedName("limit")
    var limit: Int = 0
    @SerializedName("total")
    var total: Int = 0
    @SerializedName("name")
    var name: String? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("album_type")
    var albumType: String? = null
    @SerializedName("id")
    val id: String? = null
}
