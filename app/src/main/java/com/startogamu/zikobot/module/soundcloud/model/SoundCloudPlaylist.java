
package com.startogamu.zikobot.module.soundcloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Parcel
@Data
public class SoundCloudPlaylist {

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
    @SerializedName("sharing")
    @Expose
    public String sharing;
    @SerializedName("tag_list")
    @Expose
    public String tagList;
    @SerializedName("permalink")
    @Expose
    public String permalink;
    @SerializedName("track_count")
    @Expose
    public Integer trackCount;
    @SerializedName("streamable")
    @Expose
    public Boolean streamable;
    @SerializedName("downloadable")
    @Expose
    public Boolean downloadable;
    @SerializedName("embeddable_by")
    @Expose
    public String embeddableBy;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("playlist_type")
    @Expose
    public String playlistType;
    @SerializedName("ean")
    @Expose
    public String ean;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("genre")
    @Expose
    public String genre;
    @SerializedName("release")
    @Expose
    public String release;
    @SerializedName("label_name")
    @Expose
    public String labelName;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("license")
    @Expose
    public String license;
    @SerializedName("uri")
    @Expose
    public String uri;
    @SerializedName("permalink_url")
    @Expose
    public String permalinkUrl;
    @SerializedName("artwork_url")
    @Expose
    public String artworkUrl;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("tracks")
    @Expose
    public List<SoundCloudTrack> soundCloudTracks = new ArrayList<SoundCloudTrack>();

}
