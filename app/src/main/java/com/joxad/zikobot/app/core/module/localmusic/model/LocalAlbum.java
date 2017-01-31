package com.joxad.zikobot.app.core.module.localmusic.model;

import org.parceler.Parcel;

import lombok.Getter;

/**
 * Created by josh on 06/06/16.
 */
@Parcel
public class LocalAlbum {

    @Getter
    private long id;
    @Getter
    private String name;
    @Getter
    private String artist;
    @Getter
    private String image;
    @Getter
    private int nbTracks;

    public LocalAlbum() {}

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
