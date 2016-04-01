package com.startogamu.musicalarm.model.spotify;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by josh on 01/04/16.
 */
@Data
public class SpotifyArtist {

    @SerializedName("href")
    private String href;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("uri")
    private String uri;
}
