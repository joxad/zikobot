package com.startogamu.zikobot.module.soundcloud.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by josh on 15/06/16.
 */
@Data
public class SoundCloudTrack {

    @SerializedName("title")
    private String title;

    @SerializedName("id")
    private int id;

    @SerializedName("stream_url")
    private String streamURL;

    @SerializedName("artwork_url")
    private String artworkURL;
}
