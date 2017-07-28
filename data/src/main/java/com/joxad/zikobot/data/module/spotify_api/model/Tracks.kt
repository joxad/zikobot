package com.joxad.zikobot.data.module.spotify_api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


class Tracks {

    @SerializedName("href")
    @Expose
    var href: String? = null
    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("items")
    var items: ArrayList<SpotifyTrack>? = null
}
