package com.joxad.zikobot.data.module.spotify_api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by Jocelyn on 25/02/2017.
 */
@Data
public class SpotifyTopTracks {
    @SerializedName("tracks")
    private List<SpotifyTrack> tracks;
}
