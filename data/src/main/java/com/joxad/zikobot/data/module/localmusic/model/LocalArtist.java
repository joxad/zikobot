package com.joxad.zikobot.data.module.localmusic.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by josh on 06/06/16.
 */
public class LocalArtist {
    @Getter
    protected long id;
    @Getter
    protected String name;
    @Getter
    protected int nbAlbums;
    @Getter
    @Setter
    protected String image;

    public LocalArtist() {
    }

    public LocalArtist(long id, String name, int nbAlbums) {
        this.id = id;
        this.name = name;
        this.nbAlbums = nbAlbums;
    }


}
