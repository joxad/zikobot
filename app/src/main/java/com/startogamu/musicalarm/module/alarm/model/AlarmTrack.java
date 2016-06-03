package com.startogamu.musicalarm.module.alarm.model;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.startogamu.musicalarm.core.db.MusicAlarmDatabase;
import com.startogamu.musicalarm.module.spotify_api.model.SpotifyTrack;

import org.parceler.Parcel;
import org.parceler.Transient;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by josh on 29/03/16.
 */
@Table(database = MusicAlarmDatabase.class)
@Parcel
public class AlarmTrack extends BaseModel {

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

    public AlarmTrack() {
    }


    public static AlarmTrack from(LocalTrack localTrack) {
        AlarmTrack alarmTrack = new AlarmTrack();
        alarmTrack.setType(AlarmTrack.TYPE.LOCAL);
        alarmTrack.setRef(localTrack.getData());
        alarmTrack.setImageUrl(localTrack.getArtPath());
        alarmTrack.setArtistName(localTrack.getArtist());
        alarmTrack.setName(localTrack.getTitle());
        return alarmTrack;
    }

    public static AlarmTrack from(SpotifyTrack spotifyTrack) {
        AlarmTrack alarmTrack = new AlarmTrack();
        alarmTrack.setType(AlarmTrack.TYPE.SPOTIFY);
        alarmTrack.setRef("spotify:track:" + spotifyTrack.getId());
        alarmTrack.setImageUrl(spotifyTrack.getAlbum().getImages().get(0).url);
        alarmTrack.setArtistName(spotifyTrack.getArtists().get(0).getName());
        alarmTrack.setName(spotifyTrack.getName());
        return alarmTrack;
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

    }

}
