package com.joxad.zikobot.data.module.spotify_api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by blackbird-linux on 07/11/17.
 */

public class SpotifyArtists {

    @SerializedName("artists")
    public List<SpotifyArtist> artists;
}
