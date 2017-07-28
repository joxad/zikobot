package com.joxad.zikobot.data.module.soundcloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SoundCloudTrack {

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
    @SerializedName("commentable")
    @Expose
    var commentable: Boolean? = null
    @SerializedName("state")
    @Expose
    var state: String? = null
    @SerializedName("original_content_size")
    @Expose
    var originalContentSize: Int? = null
    @SerializedName("sharing")
    @Expose
    var sharing: String? = null
    @SerializedName("tag_list")
    @Expose
    var tagList: String? = null
    @SerializedName("permalink")
    @Expose
    var permalink: String? = null
    @SerializedName("streamable")
    @Expose
    var streamable: Boolean? = null
    @SerializedName("embeddable_by")
    @Expose
    var embeddableBy: String? = null
    @SerializedName("downloadable")
    @Expose
    var downloadable: Boolean? = null
    @SerializedName("genre")
    @Expose
    var genre: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("label_name")
    @Expose
    var labelName: String? = null
    @SerializedName("release")
    @Expose
    var release: String? = null
    @SerializedName("track_type")
    @Expose
    var trackType: String? = null
    @SerializedName("key_signature")
    @Expose
    var keySignature: String? = null
    @SerializedName("isrc")
    @Expose
    var isrc: String? = null

    @SerializedName("original_format")
    @Expose
    var originalFormat: String? = null
    @SerializedName("license")
    @Expose
    var license: String? = null
    @SerializedName("uri")
    @Expose
    var uri: String? = null
    @SerializedName("user")
    @Expose
    var user: SoundCloudUser? = null
    @SerializedName("created_with")
    @Expose
    var createdWith: CreatedWith? = null
    @SerializedName("permalink_url")
    @Expose
    var permalinkUrl: String? = null
    @SerializedName("artwork_url")
    @Expose
    var artworkUrl: String? = null
    @SerializedName("waveform_url")
    @Expose
    var waveformUrl: String? = null
    @SerializedName("stream_url")
    @Expose
    var streamUrl: String? = null
    @SerializedName("playback_count")
    @Expose
    var playbackCount: Int? = null
    @SerializedName("download_count")
    @Expose
    var downloadCount: Int? = null
    @SerializedName("favoritings_count")
    @Expose
    var favoritingsCount: Int? = null
    @SerializedName("comment_count")
    @Expose
    var commentCount: Int? = null
    @SerializedName("attachments_uri")
    @Expose
    var attachmentsUri: String? = null
    @SerializedName("download_url")
    @Expose
    var downloadUrl: String? = null

}
