package com.startogamu.zikobot.module.localmusic.model;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by josh on 06/06/16.
 */
@Parcel
public class LocalArtist {
    @Getter
    private long id;
    @Getter
    private String name;
    @Getter
    private int nbAlbums;
    @Getter
    @Setter
    private String image;

    public LocalArtist(){}

    public LocalArtist(long id, String name, int nbAlbums) {
        this.id = id;
        this.name = name;
        this.nbAlbums = nbAlbums;
    }


}
