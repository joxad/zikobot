package com.joxad.zikobot.data.module.spotify_api.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Jocelyn on 25/02/2017.
 */
class SpotifyResultTracks {
    @SerializedName("items")
    val tracks: List<SpotifyTrack>? = emptyList()
    @SerializedName("limit")
    val limit: Int = 0
    @SerializedName("total")
    val total: Int = 0
    @SerializedName("next")
    val next: String? = null

    @SerializedName("offset")
    val offset: Int = 0
}
