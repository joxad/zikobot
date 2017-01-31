
package com.joxad.zikobot.app.core.module.soundcloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Parcel
@Data
public class SoundCloudTrack {

    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("duration")
    @Expose
    public Integer duration;
    @SerializedName("commentable")
    @Expose
    public Boolean commentable;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("original_content_size")
    @Expose
    public Integer originalContentSize;
    @SerializedName("sharing")
    @Expose
    public String sharing;
    @SerializedName("tag_list")
    @Expose
    public String tagList;
    @SerializedName("permalink")
    @Expose
    public String permalink;
    @SerializedName("streamable")
    @Expose
    public Boolean streamable;
    @SerializedName("embeddable_by")
    @Expose
    public String embeddableBy;
    @SerializedName("downloadable")
    @Expose
    public Boolean downloadable;
    @SerializedName("genre")
    @Expose
    public String genre;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("label_name")
    @Expose
    public String labelName;
    @SerializedName("release")
    @Expose
    public String release;
    @SerializedName("track_type")
    @Expose
    public String trackType;
    @SerializedName("key_signature")
    @Expose
    public String keySignature;
    @SerializedName("isrc")
    @Expose
    public String isrc;

    @SerializedName("original_format")
    @Expose
    public String originalFormat;
    @SerializedName("license")
    @Expose
    public String license;
    @SerializedName("uri")
    @Expose
    public String uri;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("created_with")
    @Expose
    public CreatedWith createdWith;
    @SerializedName("permalink_url")
    @Expose
    public String permalinkUrl;
    @SerializedName("artwork_url")
    @Expose
    public String artworkUrl;
    @SerializedName("waveform_url")
    @Expose
    public String waveformUrl;
    @SerializedName("stream_url")
    @Expose
    public String streamUrl;
    @SerializedName("playback_count")
    @Expose
    public Integer playbackCount;
    @SerializedName("download_count")
    @Expose
    public Integer downloadCount;
    @SerializedName("favoritings_count")
    @Expose
    public Integer favoritingsCount;
    @SerializedName("comment_count")
    @Expose
    public Integer commentCount;
    @SerializedName("attachments_uri")
    @Expose
    public String attachmentsUri;
    @SerializedName("download_url")
    @Expose
    public String downloadUrl;

}
