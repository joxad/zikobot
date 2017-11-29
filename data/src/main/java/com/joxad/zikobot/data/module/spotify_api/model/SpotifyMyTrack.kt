package com.joxad.zikobot.data.module.spotify_api.model

import com.google.gson.annotations.SerializedName

/**
 * Created by josh on 29/03/16.
 */
class SpotifyMyTrack {
    @SerializedName("added_at")
    var added_at: String = ""
    @SerializedName("track")
    var track: SpotifyTrack? = null

}
