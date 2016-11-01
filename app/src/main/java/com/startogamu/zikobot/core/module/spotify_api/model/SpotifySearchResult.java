package com.startogamu.zikobot.core.module.spotify_api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by josh on 26/08/16.
 */
public class SpotifySearchResult {
    @SerializedName("tracks")
    public Tracks tracks;
    @SerializedName("artists")
    public Artists artists;
    @SerializedName("albums")
    public Albums albums;
}
