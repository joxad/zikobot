package com.joxad.zikobot.data.model;


import com.joxad.zikobot.data.db.ItemListTrackConverter;
import com.joxad.zikobot.data.db.MusicAlarmDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by josh on 08/03/16.
 */
@ModelContainer
@Table(database = MusicAlarmDatabase.class)
@Parcel
public class Alarm extends BaseModel {

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
    protected int hour;
    @Column
    @Getter
    @Setter
    protected int minute;
    @Column
    @Getter
    @Setter
    protected int active;

    @Column
    @Getter
    @Setter
    protected int repeated;

    @Column
    @Getter
    @Setter
    protected String days = "-0000000";

    @Column
    @Getter
    @Setter
    protected int randomTrack;

    @Column
    @Getter
    @Setter
    protected int volume;

    @Column
    @Getter
    @Setter
    protected long timeInMillis;

    @ParcelPropertyConverter(ItemListTrackConverter.class)
    protected List<Track> tracks = new ArrayList<>();

    /***
     * Default constructor =>  8 00 am
     */
    public Alarm() {
        name = "Playlist";
        hour = 8;
        minute = 0;
        active = 0;
        volume = -1;
    }

    /****
     * @return
     */
    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE}, variableName = "tracks")
    public List<Track> getTracks() {
        tracks = SQLite.select()
                .from(Track.class)
                .where(Track_Table.alarmForeignKeyContainer_id.eq(id))
                .queryList();

        return tracks;
    }


    public boolean isRandom() {
        return randomTrack == 1;
    }


    public boolean isDayActive(int day) {
        StringBuilder daysBuilder = new StringBuilder(days);
        char dayStatus = daysBuilder.charAt(day);
        return dayStatus == '1';
    }

    public void activeDay(int day, Boolean aBoolean) {
        replaceChar(aBoolean ? '1' : '0', day);
    }

    public void replaceChar(char c, int index) {
        StringBuilder daysBuilder = new StringBuilder(days);
        daysBuilder.setCharAt(index, c);
        days = daysBuilder.toString();
    }

}
