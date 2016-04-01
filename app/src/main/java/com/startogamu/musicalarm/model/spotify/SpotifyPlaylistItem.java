package com.startogamu.musicalarm.model.spotify;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Created by josh on 29/03/16.
 */
@Data
public class SpotifyPlaylistItem {

    @SerializedName("track")
    public SpotifyTrack track;
}
