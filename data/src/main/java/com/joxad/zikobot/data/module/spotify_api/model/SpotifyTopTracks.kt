package com.joxad.zikobot.data.module.spotify_api.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Jocelyn on 25/02/2017.
 */
class SpotifyTopTracks {
    @SerializedName("tracks")
    val tracks: List<SpotifyTrack>? = null
}
