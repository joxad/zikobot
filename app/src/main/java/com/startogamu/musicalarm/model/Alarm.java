package com.startogamu.musicalarm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.startogamu.musicalarm.di.db.MusicAlarmDatabase;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;

/**
 * Created by josh on 08/03/16.
 */
@ModelContainer
@Table(database = MusicAlarmDatabase.class)
public class Alarm extends BaseModel implements Parcelable {

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
    protected List<AlarmTrack> tracks = new ArrayList<>();

    public Alarm() {

    }

    protected Alarm(Parcel in) {
        id = in.readLong();
        name = in.readString();
        hour = in.readInt();
        minute = in.readInt();
        // in.readTypedList(tracks, AlarmTrack.CREATOR);
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(hour);
        dest.writeInt(minute);
        //dest.writeTypedList(tracks);
    }
}
