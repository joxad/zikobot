package com.startogamu.musicalarm.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.startogamu.musicalarm.di.db.MusicAlarmDatabase;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by josh on 08/03/16.
 */
@ModelContainer
@Table(database = MusicAlarmDatabase.class)
public class Alarm extends BaseModel {

    @Getter
    @Setter
    @PrimaryKey(autoincrement = true)
    public long id;
    @Getter
    @Setter
    @Column
    public String name;
    @Getter
    @Setter
    @Column
    public int hour;
    @Getter
    @Setter
    @Column
    public int minute;
    @Getter
    @Setter
    @Column
    public String playlistId;

    List<AlarmTrack> tracks;

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
