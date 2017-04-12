package com.joxad.zikobot.data.model;


import com.joxad.zikobot.data.db.MusicAlarmDatabase;
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudTrack;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyTrack;
import com.joxad.zikobot.data.module.youtube.VideoItem;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

import org.parceler.Parcel;
import org.parceler.Transient;
import org.videolan.libvlc.Media;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by josh on 29/03/16.
 */
@Table(database = MusicAlarmDatabase.class, name = "AlarmTrack")
@Parcel
public class Track extends BaseModel {

    @Transient
    @ForeignKey(saveForeignKeyModel = false)
    public ForeignKeyContainer<Alarm> alarmForeignKeyContainer;
    @PrimaryKey(autoincrement = true)
    @Getter
    @Setter
    protected long id;
    @Column
    @Getter
    @Setter
    protected String name;
    @Column
    @Getter
    @Setter
    protected int type;
    @Column
    @Getter
    @Setter
    protected String ref;
    @Column
    @Getter
    @Setter
    protected String imageUrl;
    @Column
    @Getter
    @Setter
    protected String artistName;
    @Column
    @Getter
    @Setter
    protected String artistId;
    @Column
    @Getter
    @Setter
    protected String activated;
    @Column
    @Getter
    @Setter
    protected long duration;

    public Track() {
        setType(TYPE.LOCAL);
        setArtistName("Loading");
        setName("Loading");
    }


    public static Track from(LocalTrack localTrack) {
        Track track = new Track();
        track.setType(TYPE.LOCAL);
        track.setId(localTrack.getId());
        track.setRef(localTrack.getData());
        track.setImageUrl(localTrack.getArtPath());
        track.setArtistName(localTrack.getArtist());
        track.setName(localTrack.getTitle());
        track.setDuration((int) localTrack.getDuration());
        return track;
    }

    public static Track from(SpotifyTrack spotifyTrack) {
        Track track = new Track();
        track.setType(TYPE.SPOTIFY);
        track.setRef("spotify:track:" + spotifyTrack.getId());
        if (spotifyTrack.getAlbum() != null)
            track.setImageUrl(spotifyTrack.getAlbum().getImages().get(0).url);
        if (spotifyTrack.getArtists() != null) {
            track.setArtistName(spotifyTrack.getArtists().get(0).getName());
            track.setArtistId(spotifyTrack.getArtists().get(0).getId());
        }
        track.setName(spotifyTrack.getName());
        track.setDuration(spotifyTrack.getDuration());
        return track;
    }

    /***
     * @param deezerTrack
     * @return
     */
    public static Track from(com.deezer.sdk.model.Track deezerTrack) {
        Track track = new Track();
        track.setType(TYPE.DEEZER);
        track.setRef("" + deezerTrack.getId());
        track.setImageUrl(deezerTrack.getAlbum().getMediumImageUrl());
        track.setArtistName(deezerTrack.getArtist().getName());
        track.setName(deezerTrack.getTitle());
        track.setDuration(deezerTrack.getDuration() * 1000);
        return track;
    }

    /***
     * @param videoItem
     * @return
     */
    public static Track from(VideoItem videoItem) {
        Track track = new Track();
        track.setType(TYPE.YOUTUBE);
        track.setRef(videoItem.getRef());
        track.setImageUrl(videoItem.getThumbnailURL());
        track.setArtistName(videoItem.getDescription());
        track.setName(videoItem.getTitle());
        track.setDuration(videoItem.getDuration());
        return track;
    }


    /***
     * @param soundCloudTrack
     * @param clientId
     * @return
     */
    public static Track from(SoundCloudTrack soundCloudTrack, String clientId) {
        Track track = new Track();
        track.setType(TYPE.SOUNDCLOUD);
        track.setRef(soundCloudTrack.getStreamUrl() + "?client_id=" + clientId);
        track.setImageUrl(soundCloudTrack.getArtworkUrl());
        track.setArtistName(soundCloudTrack.getUser().getUsername());
        track.setArtistId("" + soundCloudTrack.getUser().getId());
        track.setName(soundCloudTrack.getTitle());
        track.setDuration(soundCloudTrack.getDuration());
        return track;
    }

    public static Track from(Media media, String title) {
        Track track = new Track();
        track.setType(TYPE.LOCAL);
        track.setRef(media.getUri().toString());
        track.setName(title);
        return track;
    }

    public static Track from(SpotifyTrack spotifyTrack, String image) {
        Track track = new Track();
        track.setType(TYPE.SPOTIFY);
        track.setRef("spotify:track:" + spotifyTrack.getId());
        track.setImageUrl(image);
        if (spotifyTrack.getArtists() != null) {
            track.setArtistName(spotifyTrack.getArtists().get(0).getName());
            track.setArtistId(spotifyTrack.getArtists().get(0).getId());
        }
        track.setName(spotifyTrack.getName());
        track.setDuration(spotifyTrack.getDuration());
        return track;
    }

    public void associateAlarm(Alarm alarm) {
        alarmForeignKeyContainer = FlowManager.getContainerAdapter(Alarm.class).toForeignKeyContainer(alarm);
    }

    public ForeignKeyContainer<Alarm> getAlarmForeignKeyContainer() {
        return alarmForeignKeyContainer;
    }
}
