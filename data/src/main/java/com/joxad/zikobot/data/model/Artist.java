package com.joxad.zikobot.data.model;

import com.joxad.zikobot.data.module.localmusic.model.LocalArtist;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudUser;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyArtist;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by josh on 08/08/16.
 */
@Parcel
public class Artist {
    @Setter
    @Getter
    private long id;
    @Setter
    @Getter
    private String name;
    @Getter
    @Setter
    private String image;
    @Getter
    @Setter
    protected int type;

    public Artist() {
    }

    public static Artist from(LocalArtist localArtist) {
        Artist artist = new Artist();
        artist.id = localArtist.getId();
        artist.type = TYPE.LOCAL;
        artist.name = localArtist.getName();
        artist.image = localArtist.getImage();
        return artist;
    }

    public static  Artist from(SpotifyArtist spotifyArtist) {
        Artist artist = new Artist();

        artist.id = Long.parseLong(spotifyArtist.getHref());
        artist.type = TYPE.SPOTIFY;
        artist.name = spotifyArtist.getName();
        return artist;
    }

    public static Artist from(com.deezer.sdk.model.Artist deezerArtist) {
        Artist artist = new Artist();
        artist.id = deezerArtist.getId();
        artist.type = TYPE.DEEZER;
        artist.name = deezerArtist.getName();
        return artist;

    }

    public static Artist from(SoundCloudUser user) {
        Artist artist = new Artist();
        artist.id = user.getId();
        artist.type = TYPE.SOUNDCLOUD;
        artist.name = user.getUsername();
        artist.image = user.getAvatarUrl();
        return artist;
    }
}
