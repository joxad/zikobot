package com.joxad.zikobot.data.db.model;


import com.joxad.zikobot.data.db.ZikoDB;
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudTrack;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyTrack;
import com.joxad.zikobot.data.module.youtube.VideoItem;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.videolan.libvlc.Media;

/**
 * Created by josh on 29/03/16.
 */
@Table(database = ZikoDB.class, insertConflict = ConflictAction.REPLACE)
public class ZikoTrack extends BaseModel {


    @PrimaryKey(autoincrement = true)
    @Column
    protected long id;

    @Column
    protected String name;

    @Column
    protected int localId;

    @Column
    protected int type;
    @Column
    protected String ref;

    @Column
    protected String activated;
    @Column
    protected long duration;

    @Column
    @ForeignKey(saveForeignKeyModel = false, stubbedRelationship = true)
    public ZikoPlaylist zikoPlaylist;
    @Column
    @ForeignKey(saveForeignKeyModel = false, stubbedRelationship = true)
    public ZikoArtist zikoArtist;
    @Column
    @ForeignKey(saveForeignKeyModel = false, stubbedRelationship = true)
    public ZikoAlbum zikoAlbum;


    public ZikoTrack() {
        this.type = TYPE.LOCAL;
        this.name = "Loading";
    }


    public static ZikoTrack local(LocalTrack localTrack, ZikoArtist zikoArtist, ZikoAlbum zikoAlbum) {
        ZikoTrack track = new ZikoTrack();
        track.setType(TYPE.LOCAL);
        track.setId(localTrack.getId());
        track.setRef(localTrack.getData());
        track.setLocalId(localTrack.getId());
        track.associateArtist(zikoArtist);
        track.associateAlbum(zikoAlbum);
        track.setName(localTrack.getTitle());
        track.setDuration((int) localTrack.getDuration());
        return track;
    }

    public static ZikoTrack spotify(SpotifyTrack spotifyTrack, ZikoArtist zikoArtist, ZikoAlbum zikoAlbum, ZikoPlaylist zikoPlaylist) {

        ZikoTrack track = new ZikoTrack();
        track.setType(TYPE.SPOTIFY);
        track.setRef("spotify:track:" + spotifyTrack.getId());
        track.associateArtist(zikoArtist);
        track.associateAlbum(zikoAlbum);
        track.associatePlaylist(zikoPlaylist);
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
        // track.setArtistName(videoItem.getDescription());
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
        //  track.associateArtist();
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
        zikoPlaylist = playlist;
    }

    public void associateAlbum(ZikoAlbum album) {
        zikoAlbum = album;
    }

    public void associateArtist(ZikoArtist artist) {
        zikoArtist = artist;
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

    public String getActivated() {
        return activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public long getDuration() {
        return duration;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
