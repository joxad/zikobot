package com.joxad.zikobot.data.model;

import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;
import com.joxad.zikobot.data.module.spotify_api.model.Item;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;

/**
 * Created by josh on 19/06/16.
 */
@Data
@Builder
@AllArgsConstructor(suppressConstructorProperties = true)
public class Playlist {

    private String name;
    private String href;
    private int type;
    private int nbTracks;
    private String imageUrl;

    private ArrayList<Track> tracks;


    public static Playlist fromSpotifyPlaylist(Item item) {
        return Playlist.builder()
                .name(item.getName())
                .href(item.getHref())
                .type(TYPE.SPOTIFY)
                .nbTracks(item.tracks.getTotal())
                .imageUrl(item.getImages().get(0).getUrl())
                .build();
    }

    public static Playlist fromSoundCloudPlaylist(SoundCloudPlaylist soundCloudPlaylist) {
        return Playlist.builder()
                .name(soundCloudPlaylist.getTitle())
                .nbTracks(soundCloudPlaylist.getSoundCloudTracks().size())
                .type(TYPE.SOUNDCLOUD)
            //    .href(soundCloudPlaylist.getId())
                .imageUrl(soundCloudPlaylist.getArtworkUrl()).build();
    }


}
