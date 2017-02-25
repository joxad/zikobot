package com.joxad.zikobot.data.model;

import com.joxad.zikobot.data.module.localmusic.model.LocalAlbum;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyAlbum;

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
    protected String id;
    protected String name;
    protected String image;
    protected int type;
    protected int nbTracks;
    protected String artist;

    public Album() {
    }

    public static Album from(LocalAlbum localAlbum) {
        Album album = new Album();
        album.id = "" + localAlbum.getId();
        album.type = TYPE.LOCAL;
        album.name = localAlbum.getName();
        album.image = localAlbum.getImage();
        album.nbTracks = localAlbum.getNbTracks();
        album.artist = localAlbum.getArtist();
        return album;
    }


    public static Album from(SpotifyAlbum spotifyAlbum) {
        Album album = new Album();
        album.id = spotifyAlbum.getId();
        album.type = TYPE.SPOTIFY;
        album.name = spotifyAlbum.getName();
        album.image = spotifyAlbum.getImages().get(0).getUrl();
        album.nbTracks = spotifyAlbum.getTotal();
        album.artist = spotifyAlbum.getArtists().get(0).getName();
        return album;
    }
}
