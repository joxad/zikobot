package com.joxad.zikobot.data.module.spotify_api.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by josh on 29/03/16.
 */
class SpotifyTrack {
    @SerializedName("id")
    val id: String? = null
    @SerializedName("name")
    val name: String? = null
    @SerializedName("album")
    val album: SpotifyAlbum? = null
    @SerializedName("artists")
    val artists: ArrayList<SpotifyArtist>? = null
    @SerializedName("type")
    val type: String? = null
    @SerializedName("duration_ms")
    val duration: Long = 0


}
