
package com.joxad.zikobot.app.core.module.spotify_api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Data;

@Generated("org.jsonschema2pojo")
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SpotifyFeaturedPlaylist {

    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("playlists")
    SpotifyPlaylist spotifyPlaylist;
}
