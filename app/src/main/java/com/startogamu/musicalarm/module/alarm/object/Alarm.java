package com.startogamu.musicalarm.module.alarm.object;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.startogamu.musicalarm.core.db.MusicAlarmDatabase;
import com.startogamu.musicalarm.core.utils.ItemListTrackConverter;

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

    //   @Column
    protected byte[] days = new byte[8];
    @ParcelPropertyConverter(ItemListTrackConverter.class)
    protected List<AlarmTrack> tracks = new ArrayList<>();

    /***
     * Default constructor =>  8 00 am
     */
    public Alarm() {
        name = "";
        hour = 8;
        minute = 0;
    }

    /****
     * @return
     */
    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE}, variableName = "tracks")
    public List<AlarmTrack> getTracks() {
        if (tracks == null || tracks.isEmpty()) {
            tracks = SQLite.select()
                    .from(AlarmTrack.class)
                    .where(AlarmTrack_Table.alarmForeignKeyContainer_id.eq(id))
                    .queryList();
        }
        return tracks;
    }



    public boolean isDayActive(int day) {
        return true;
    }

    public void activeDay(int day) {
        days[day] = 1;
    }


    public void unactiveDay(int day) {
        days[day] = 0;
    }
}
