package com.startogamu.musicalarm.model;

import org.parceler.Parcel;

import io.realm.AlarmRealmProxy;
import io.realm.RealmObject;

/**
 * Created by josh on 08/03/16.
 */

@Parcel(implementations = {AlarmRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {Alarm.class})
public class Alarm extends RealmObject {


    public Alarm() {

    }
    public Alarm(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /*
RealmConfiguration config = new RealmConfiguration.Builder(context).build();
Realm realm = Realm.getInstance(config);
AtomicLong primaryKeyValue = realm.where(Foo.class).max("id").longValue();
realm.close();

// ....

// Create new object
        new Foo(primaryKeyValue.incrementAndGet());*/

}
