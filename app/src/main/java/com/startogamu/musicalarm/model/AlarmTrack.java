package com.startogamu.musicalarm.model;

import android.os.Parcel;
import android.os.Parcelable;

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
import lombok.experimental.Builder;

/**
 * Created by josh on 29/03/16.
 */
@Table(database = MusicAlarmDatabase.class)
public class AlarmTrack extends BaseModel implements Parcelable {

    @PrimaryKey(autoincrement = true)
    @Getter
    @Setter
    protected long id;

    @Column
    @Getter
    @Setter
    protected String ref;

    @Column
    @Getter
    @Setter
    protected int type;

    @ForeignKey(saveForeignKeyModel = false)
    public ForeignKeyContainer<Alarm> alarmForeignKeyContainer;

    public AlarmTrack() {
    }

    protected AlarmTrack(Parcel in) {
        id = in.readLong();
        ref = in.readString();
        type = in.readInt();
    }

    public static final Creator<AlarmTrack> CREATOR = new Creator<AlarmTrack>() {
        @Override
        public AlarmTrack createFromParcel(Parcel in) {
            return new AlarmTrack(in);
        }

        @Override
        public AlarmTrack[] newArray(int size) {
            return new AlarmTrack[size];
        }
    };

    public void associateAlarm(Alarm alarm) {
        alarmForeignKeyContainer = FlowManager.getContainerAdapter(Alarm.class).toForeignKeyContainer(alarm);
    }

    public ForeignKeyContainer<Alarm> getAlarmForeignKeyContainer() {
        return alarmForeignKeyContainer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(ref);
        dest.writeInt(type);
    }

    public static class TYPE {
        public static final int SPOTIFY = 0;
        public static final int DEEZER = 1;
        public static final int LOCAL = 2;

    }

}
