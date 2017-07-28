package com.joxad.zikobot.data.module.soundcloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


import java.util.ArrayList

import lombok.Data

class SoundCloudPlaylist {

    @SerializedName("kind")
    @Expose
    var kind: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("user_id")
    @Expose
    var userId: Int? = null
    @SerializedName("duration")
    @Expose
    var duration: Int? = null
    @SerializedName("sharing")
    @Expose
    var sharing: String? = null
    @SerializedName("tag_list")
    @Expose
    var tagList: String? = null
    @SerializedName("permalink")
    @Expose
    var permalink: String? = null
    @SerializedName("track_count")
    @Expose
    var trackCount: Int? = null
    @SerializedName("streamable")
    @Expose
    var streamable: Boolean? = null
    @SerializedName("downloadable")
    @Expose
    var downloadable: Boolean? = null
    @SerializedName("embeddable_by")
    @Expose
    var embeddableBy: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("playlist_type")
    @Expose
    var playlistType: String? = null
    @SerializedName("ean")
    @Expose
    var ean: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("genre")
    @Expose
    var genre: String? = null
    @SerializedName("release")
    @Expose
    var release: String? = null
    @SerializedName("label_name")
    @Expose
    var labelName: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("license")
    @Expose
    var license: String? = null
    @SerializedName("uri")
    @Expose
    var uri: String? = null
    @SerializedName("permalink_url")
    @Expose
    var permalinkUrl: String? = null
    @SerializedName("artwork_url")
    @Expose
    var artworkUrl: String? = null
    @SerializedName("user")
    @Expose
    var user: SoundCloudUser? = null
    @SerializedName("tracks")
    @Expose
    var soundCloudTracks: List<SoundCloudTrack> = ArrayList()

    companion object {

        fun favorite(user: SoundCloudUser, soundCloudTracks: ArrayList<SoundCloudTrack>): SoundCloudPlaylist {
            val soundCloudPlaylist = SoundCloudPlaylist()
            soundCloudPlaylist.user =(user)
            soundCloudPlaylist.title = "Favoris"
            soundCloudPlaylist.artworkUrl = user.avatarUrl
            soundCloudPlaylist.soundCloudTracks =(soundCloudTracks)
            soundCloudPlaylist.trackCount = (soundCloudTracks.size)
            return soundCloudPlaylist
        }
    }
}
