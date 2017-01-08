package com.startogamu.zikobot.core.model;

import com.startogamu.zikobot.core.module.localmusic.model.LocalAlbum;

import org.parceler.Parcel;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by josh on 08/08/16.
 */
@Data
@Parcel
@AllArgsConstructor(suppressConstructorProperties = true)
public class Album {
    protected long id;
    protected String name;
    protected String image;
    protected int type;
    protected int nbTracks;
    protected String artist;
    public Album() {
    }

    public static Album from(LocalAlbum localAlbum) {
        Album album = new Album();
        album.id = localAlbum.getId();
        album.type = TYPE.LOCAL;
        album.name = localAlbum.getName();
        album.image = localAlbum.getImage();
        album.nbTracks = localAlbum.getNbTracks();
        album.artist=localAlbum.getArtist();
        return album;
    }
}
