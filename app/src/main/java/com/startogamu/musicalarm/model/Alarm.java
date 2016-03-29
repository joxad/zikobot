package com.startogamu.musicalarm.model;

import com.startogamu.musicalarm.utils.AlarmListParcelConverter;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import java.util.List;

import io.realm.AlarmRealmProxy;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by josh on 08/03/16.
 */


@Parcel(implementations = {AlarmRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Alarm.class})
public class Alarm extends RealmObject {

    public Alarm() {
    }

    @PrimaryKey
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String playlistId;
    @Getter
    private RealmList<AlarmTrack> alarmTracks;
    @Getter
    @Setter
    private int hour;
    @Getter
    @Setter
    private int minute;


    @ParcelPropertyConverter(AlarmListParcelConverter.class)
    public void setTracks(List<AlarmTrack> alarmTracks) {
        this.alarmTracks.clear();
        this.alarmTracks.addAll(alarmTracks);
    }

}
