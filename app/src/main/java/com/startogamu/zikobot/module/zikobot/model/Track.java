package com.startogamu.zikobot.module.zikobot.model;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.startogamu.zikobot.core.db.MusicAlarmDatabase;
import com.startogamu.zikobot.module.localmusic.model.LocalTrack;
import com.startogamu.zikobot.module.soundcloud.model.SoundCloudTrack;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyTrack;

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
    protected String activated;

    @Getter
    @Setter
    protected long duration;

    @Transient
    @ForeignKey(saveForeignKeyModel = false)
    public ForeignKeyContainer<Alarm> alarmForeignKeyContainer;

    public Track() {
    }


    public static Track from(LocalTrack localTrack) {
        Track track = new Track();
        track.setType(TYPE.LOCAL);
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
        track.setImageUrl(spotifyTrack.getAlbum().getImages().get(0).url);
        track.setArtistName(spotifyTrack.getArtists().get(0).getName());
        track.setName(spotifyTrack.getName());
        track.setDuration(spotifyTrack.getDuration());
        return track;
    }

    /***
     * 
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
        track.setDuration(deezerTrack.getDuration());
        return track;
    }


    /***
     *
     * @param soundCloudTrack
     * @param clientId
     * @return
     */
    public static Track from(SoundCloudTrack soundCloudTrack, String clientId) {
        Track track = new Track();
        track.setType(TYPE.SOUNDCLOUD);
        track.setRef(soundCloudTrack.getStreamUrl() + "?client_id=" + clientId);
        track.setImageUrl(soundCloudTrack.getArtworkUrl());
        //track.setArtistName(soundCloudTrack.geta);
        track.setName(soundCloudTrack.getTitle());
        track.setDuration(soundCloudTrack.getDuration());
        return track;
    }

    public void associateAlarm(Alarm alarm) {
        alarmForeignKeyContainer = FlowManager.getContainerAdapter(Alarm.class).toForeignKeyContainer(alarm);
    }

    public ForeignKeyContainer<Alarm> getAlarmForeignKeyContainer() {
        return alarmForeignKeyContainer;
    }


    public static Track from(Media media, String title) {
        Track track = new Track();
        track.setType(TYPE.LOCAL);
        track.setRef(media.getUri().toString());
        track.setName(title);
        return track;
    }
}
