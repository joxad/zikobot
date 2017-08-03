package com.joxad.zikobot.data.module.lastfm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Albummatches {

    @SerializedName("album")
    @Expose
    private List<Album> artist = null;

    public List<Album> getAlbum() {
        return artist;
    }

    public void setAlbum(List<Album> artist) {
        this.artist = artist;
    }

}