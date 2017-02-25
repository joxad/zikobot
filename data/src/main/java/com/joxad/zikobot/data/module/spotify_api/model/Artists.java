package com.joxad.zikobot.data.module.spotify_api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by josh on 26/08/16.
 */
public class Artists {

    @SerializedName("items")
    public List<SpotifyArtist> items;
}
