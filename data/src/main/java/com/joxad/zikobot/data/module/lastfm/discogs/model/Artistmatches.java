package com.joxad.zikobot.data.module.lastfm.discogs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Artistmatches {

        @SerializedName("artist")
        @Expose
        private List<Artist> artist = null;

        public List<Artist> getArtist() {
            return artist;
        }

        public void setArtist(List<Artist> artist) {
            this.artist = artist;
        }

    }