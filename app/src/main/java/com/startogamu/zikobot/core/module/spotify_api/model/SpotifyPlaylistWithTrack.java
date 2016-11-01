package com.startogamu.zikobot.core.module.spotify_api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 29/03/16.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SpotifyPlaylistWithTrack {

    @SerializedName("items")
    public ArrayList<SpotifyPlaylistItem> items;

}
