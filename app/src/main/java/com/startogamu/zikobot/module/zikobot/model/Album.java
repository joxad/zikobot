package com.startogamu.zikobot.module.zikobot.model;

import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.module.content_resolver.model.LocalArtist;
import com.startogamu.zikobot.module.soundcloud.model.User;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyArtist;

import org.parceler.Parcel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by josh on 08/08/16.
 */
@Data
@Parcel
public class Album {
    private long id;
    private String name;
    private String image;
    protected int type;
    protected int nbTracks;

    public Album() {
    }

    public static Album from(LocalAlbum localAlbum) {
        Album album = new Album();
        album.id = localAlbum.getId();
        album.type = TYPE.LOCAL;
        album.name = localAlbum.getName();
        album.image = localAlbum.getImage();
        album.nbTracks = localAlbum.getNbTracks();
        return album;
    }
}
