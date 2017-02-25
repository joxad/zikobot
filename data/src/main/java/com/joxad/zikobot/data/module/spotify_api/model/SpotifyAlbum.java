
package com.joxad.zikobot.data.module.spotify_api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SpotifyAlbum {

    @SerializedName("id")
    private String id;

    @SerializedName("images")
    public List<Image> images = new ArrayList<Image>();

    @SerializedName("artists")
    public List<SpotifyArtist> artists = new ArrayList<SpotifyArtist>();

    @SerializedName("limit")
    public int limit;

    @SerializedName("total")
    public int total;
    @SerializedName("name")
    public String name;
    @SerializedName("type")
    public String type;
    @SerializedName("album_type")
    protected String albumType;
}
