package com.joxad.zikobot.data.db.model;


import com.joxad.zikobot.data.db.ZikoDB;
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudTrack;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyTrack;
import com.joxad.zikobot.data.module.youtube.VideoItem;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.videolan.libvlc.Media;

/**
 * Created by josh on 29/03/16.
 */
@Table(database = ZikoDB.class)
public class ZikoTrack extends BaseModel {

    @ForeignKey(saveForeignKeyModel = false, stubbedRelationship = true)
    public ZikoPlaylist zikoPlaylistForeignKey;

    @PrimaryKey(autoincrement = true)
    @Column
    protected long id;
    @Column


    protected String name;
    @Column


    protected int type;
    @Column


    protected String ref;
    @Column


    protected String artistName;
    @Column


    protected String artistId;
    @Column


    protected String activated;
    @Column


    protected long duration;

    public ZikoTrack() {
        this.type = TYPE.LOCAL;
        this.artistName = "Loading";
        this.name = "Loading";
    }


    public static ZikoTrack local(LocalTrack localTrack) {
        ZikoTrack track = new ZikoTrack();
        track.setType(TYPE.LOCAL);
        track.setId(localTrack.getId());
        track.setRef(localTrack.getData());
        track.setArtistName(localTrack.getArtistName());
        track.setName(localTrack.getTitle());
        track.setDuration((int) localTrack.getDuration());
        return track;
    }

    public static ZikoTrack from(SpotifyTrack spotifyTrack) {
        ZikoTrack track = new ZikoTrack();
        track.setType(TYPE.SPOTIFY);
        track.setRef("spotify:track:" + spotifyTrack.getId());
        if (spotifyTrack.getArtists() != null) {
            track.setArtistName(spotifyTrack.getArtists().get(0).getName());
            track.setArtistId(spotifyTrack.getArtists().get(0).getId());
        }
        track.setName(spotifyTrack.getName());
        track.setDuration(spotifyTrack.getDuration());
        return track;
    }

    /***
     * @param videoItem
     * @return
     */
    public static ZikoTrack from(VideoItem videoItem) {
        ZikoTrack track = new ZikoTrack();
        track.setType(TYPE.YOUTUBE);
        track.setRef(videoItem.getRef());
        track.setArtistName(videoItem.getDescription());
        track.setName(videoItem.getTitle());
        track.setDuration(videoItem.getDuration());
        return track;
    }

    /***
     * @param soundCloudTrack
     * @return
     */
    public static ZikoTrack from(SoundCloudTrack soundCloudTrack) {
        ZikoTrack track = new ZikoTrack();
        track.setType(TYPE.SOUNDCLOUD);
        track.setRef(soundCloudTrack.getStreamUrl());
        track.setArtistName(soundCloudTrack.getUser().getUsername());
        track.setArtistId("" + soundCloudTrack.getUser().getId());
        track.setName(soundCloudTrack.getTitle());
        track.setDuration(soundCloudTrack.getDuration());
        return track;
    }

    public static ZikoTrack from(Media media, String title) {
        ZikoTrack track = new ZikoTrack();
        track.setType(TYPE.LOCAL);
        track.setRef(media.getUri().toString());
        track.setName(title);
        return track;
    }

    public void associatePlaylist(ZikoPlaylist playlist) {
        zikoPlaylistForeignKey = playlist;
    }

    public void associateAlbum(ZikoPlaylist playlist) {
        zikoPlaylistForeignKey = playlist;
    }

    public void associateArtist(ZikoPlaylist playlist) {
        zikoPlaylistForeignKey = playlist;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getActivated() {
        return activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
