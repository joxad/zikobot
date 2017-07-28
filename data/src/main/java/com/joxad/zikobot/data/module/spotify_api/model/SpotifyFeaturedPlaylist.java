package com.joxad.zikobot.data.module.spotify_api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SpotifyFeaturedPlaylist {

    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("playlists")
    SpotifyPlaylist spotifyPlaylist;
}
