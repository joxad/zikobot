package com.joxad.zikobot.data.module.spotify_api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import lombok.AllArgsConstructor
import lombok.Data

class Image {

    @SerializedName("height")
    @Expose
    var height: Int? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("width")
    @Expose
    var width: Int? = null
}
