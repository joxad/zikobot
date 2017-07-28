package com.joxad.zikobot.data.module.spotify_api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


import java.util.ArrayList

import lombok.AllArgsConstructor
import lombok.Data

class Item {

    @SerializedName("collaborative")
    @Expose
    var collaborative: Boolean? = null
    @SerializedName("external_urls")
    @Expose
    var externalUrls: ExternalUrls? = null
    @SerializedName("href")
    @Expose
    var href: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("images")
    @Expose
    var images: List<Image> = ArrayList()
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("owner")
    @Expose
    var owner: Owner? = null
    @SerializedName("public")
    @Expose
    var _public: Boolean? = null
    @SerializedName("snapshot_id")
    @Expose
    var snapshotId: String? = null
    @SerializedName("tracks")
    @Expose
    var tracks: Tracks? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("uri")
    @Expose
    var uri: String? = null


}
