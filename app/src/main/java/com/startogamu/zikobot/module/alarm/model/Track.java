package com.startogamu.zikobot.module.alarm.model;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.startogamu.zikobot.core.db.MusicAlarmDatabase;
import com.startogamu.zikobot.module.content_resolver.model.LocalTrack;
import com.startogamu.zikobot.module.soundcloud.model.SoundCloudTrack;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyTrack;

import org.parceler.Parcel;
import org.parceler.Transient;

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

    @Transient
    @ForeignKey(saveForeignKeyModel = false)
    public ForeignKeyContainer<Alarm> alarmForeignKeyContainer;

    public Track() {
    }


    public static Track from(LocalTrack localTrack) {
        Track track = new Track();
        track.setType(Track.TYPE.LOCAL);
        track.setRef(localTrack.getData());
        track.setImageUrl(localTrack.getArtPath());
        track.setArtistName(localTrack.getArtist());
        track.setName(localTrack.getTitle());
        return track;
    }

    public static Track from(SpotifyTrack spotifyTrack) {
        Track track = new Track();
        track.setType(Track.TYPE.SPOTIFY);
        track.setRef("spotify:track:" + spotifyTrack.getId());
        track.setImageUrl(spotifyTrack.getAlbum().getImages().get(0).url);
        track.setArtistName(spotifyTrack.getArtists().get(0).getName());
        track.setName(spotifyTrack.getName());
        return track;
    }


    public static Track from(SoundCloudTrack soundCloudTrack) {
        Track track = new Track();
        track.setType(Track.TYPE.SOUNDCLOUD);
        track.setRef(soundCloudTrack.getStreamURL() + "?client_id=c6cbaa6a6e431c11c4b0e6e0cffb4ff5");
        track.setImageUrl(soundCloudTrack.getArtworkURL());
        // track.setArtistName(soundCloudTrack.get);
        track.setName(soundCloudTrack.getTitle());
        return track;
    }

    public void associateAlarm(Alarm alarm) {
        alarmForeignKeyContainer = FlowManager.getContainerAdapter(Alarm.class).toForeignKeyContainer(alarm);
    }

    public ForeignKeyContainer<Alarm> getAlarmForeignKeyContainer() {
        return alarmForeignKeyContainer;
    }


    public static class TYPE {
        public static final int SPOTIFY = 0;
        public static final int DEEZER = 1;
        public static final int LOCAL = 2;
        public static final int SOUNDCLOUD = 3;
    }

}
