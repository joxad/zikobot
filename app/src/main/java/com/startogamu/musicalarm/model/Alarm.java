package com.startogamu.musicalarm.model;


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
public class Alarm extends BaseModel  {

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

    @ParcelPropertyConverter(ItemListTrackConverter.class)
    protected List<AlarmTrack> tracks = new ArrayList<>();


    public Alarm() {
        hour = 8;
        minute = 0;
    }

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


}
