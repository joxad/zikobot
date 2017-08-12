package com.joxad.zikobot.data.module.spotify_api.model

import com.google.gson.annotations.SerializedName

import lombok.AllArgsConstructor
import lombok.Data

/**
 * Created by josh on 29/03/16.
 */
class SpotifyPlaylistItem {

    @SerializedName("track")
    var track: SpotifyTrack? = null
}
