package com.startogamu.musicalarm.di.manager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.startogamu.musicalarm.model.Alarm;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by josh on 28/03/16.
 */
@Singleton
public class AlarmManager {

    private Realm realm;

    @Inject
    public AlarmManager(Realm realm) {
        this.realm = realm;
    }

    public void loadAlarms(Subscriber<List<Alarm>> subscriber) {
        realm.where(Alarm.class).findAllAsync().asObservable()
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }


    public int getNextKey() {


        try {
            return realm.where(Alarm.class).max("id").intValue() + 1;
        } catch (NullPointerException ne) {
            return 0;
        }
    }

    /***
     * @param alarm
     */
    public void saveAlarm(Alarm alarm) {
        realm.beginTransaction();
        if (alarm.getId() == -1)
            alarm.setId(getNextKey());
        realm.copyToRealmOrUpdate(alarm);
        realm.commitTransaction();

    }


}
