package com.startogamu.musicalarm.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.startogamu.musicalarm.di.db.MusicAlarmDatabase;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by josh on 29/03/16.
 */
@Table(database = MusicAlarmDatabase.class)
public class AlarmTrack extends BaseModel {

    @Getter
    @Setter
    @PrimaryKey(autoincrement = true)
    public long id;
    @Getter
    @Setter
    @Column
    public String ref;
    @Getter
    @Setter
    @Column
    public int type;

    @ForeignKey(saveForeignKeyModel = false)
    public ForeignKeyContainer<Alarm> alarmForeignKeyContainer;

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
