package com.startogamu.musicalarm.model.spotify;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Data;

/**
 * Created by josh on 29/03/16.
 */
@Data
public class SpotifyPlaylistWithTrack {

    @SerializedName("items")
    public ArrayList<SpotifyPlaylistItem> items;

    @SerializedName("name")
    public String name;
}
