package com.startogamu.musicalarm.di.manager;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.model.AlarmTrack;
import com.startogamu.musicalarm.model.Alarm_Table;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by josh on 28/03/16.
 */
public class AlarmManager {


    /***
     * Create an observable for alarm
     *
     * @param id
     * @return
     */
    public static Observable<Alarm> getAlarmById(final long id) {
        return Observable.create(new Observable.OnSubscribe<Alarm>() {
            @Override
            public void call(Subscriber<? super Alarm> subscriber) {
                Alarm alarm = new Select().from(Alarm.class).where(Alarm_Table.id.eq(id)).querySingle();
                alarm.getTracks();
                if (subscriber.isUnsubscribed()) return;
                subscriber.onNext(alarm);
            }
        });
    }

    /**
     * Create an observable on dbflow
     *
     * @return
     */
    public static Observable<List<Alarm>> loadAlarms() {
        return Observable.create(new Observable.OnSubscribe<List<Alarm>>() {
            @Override
            public void call(Subscriber<? super List<Alarm>> subscriber) {
                List<Alarm> organizationList = new Select().from(Alarm.class).queryList();
                if (subscriber.isUnsubscribed()) return;
                subscriber.onNext(organizationList);
            }
        });
    }


    /***
     * @param alarm
     * @param alarmTrackList
     */
    public static Observable<Alarm> saveAlarm(Alarm alarm, List<AlarmTrack> alarmTrackList) {
        return Observable.create(new Observable.OnSubscribe<Alarm>() {
            @Override
            public void call(Subscriber<? super Alarm> subscriber) {
                alarm.save();
                for (AlarmTrack alarmTrack : alarmTrackList) {
                    alarmTrack.associateAlarm(alarm);
                    alarmTrack.save();
                }
                subscriber.onNext(alarm);
            }
        });

    }


}
