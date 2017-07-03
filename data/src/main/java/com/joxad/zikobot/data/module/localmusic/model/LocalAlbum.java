package com.joxad.zikobot.data.module.localmusic.model;

import org.parceler.Parcel;

import lombok.Getter;

/**
 * Created by josh on 06/06/16.
 */
@Parcel
public class LocalAlbum {

    @Getter
    protected long id;
    @Getter
    protected String name;
    @Getter
    protected String artist;
    @Getter
    protected String image;
    @Getter
    protected int nbTracks;

    public LocalAlbum() {
    }

    /***
     *
     * @param id
     * @param name
     * @param artist
     * @param image
     * @param nbTracks
     */
    public LocalAlbum(long id, String name, String artist, String image, int nbTracks) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.image = image;
        this.nbTracks = nbTracks;
    }
}
