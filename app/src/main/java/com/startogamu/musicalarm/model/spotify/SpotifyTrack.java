package com.startogamu.musicalarm.model.spotify;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by josh on 29/03/16.
 */
@Data
public class SpotifyTrack {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
}
