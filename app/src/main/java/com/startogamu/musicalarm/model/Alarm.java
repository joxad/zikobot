package com.startogamu.musicalarm.model;

import org.parceler.Parcel;

import io.realm.AlarmRealmProxy;
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
    @Setter
    private int hour;
    @Getter
    @Setter
    private int minute;

    /*
RealmConfiguration config = new RealmConfiguration.Builder(context).build();
Realm realm = Realm.getInstance(config);
AtomicLong primaryKeyValue = realm.where(Foo.class).max("id").longValue();
realm.close();

// ....

// Create new object
        new Foo(primaryKeyValue.incrementAndGet());*/

}
