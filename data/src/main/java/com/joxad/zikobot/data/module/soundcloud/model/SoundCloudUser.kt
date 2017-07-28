package com.joxad.zikobot.data.module.soundcloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


import lombok.AllArgsConstructor
import lombok.Data

/**
 * Created by josh on 15/06/16.
 */
class SoundCloudUser {


    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("kind")
    @Expose
    var kind: String? = null
    @SerializedName("permalink")
    @Expose
    var permalink: String? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("uri")
    @Expose
    var uri: String? = null
    @SerializedName("permalink_url")
    @Expose
    var permalinkUrl: String? = null
    @SerializedName("avatar_url")
    @Expose
    var avatarUrl: String? = null
}
