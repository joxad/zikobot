package com.startogamu.zikobot.module.alarm.manager;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.startogamu.zikobot.module.alarm.model.Alarm;
import com.startogamu.zikobot.module.alarm.model.Track;
import com.startogamu.zikobot.module.alarm.model.Track_Table;
import com.startogamu.zikobot.module.alarm.model.Alarm_Table;

import java.util.Calendar;
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

                if (alarm == null) {
                    subscriber.onError(new Throwable("No alarm"));
                    return;
                }
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
     * @param trackList
     */
    public static Observable<Alarm> saveAlarm(Alarm alarm, List<Track> trackList) {
        return Observable.create(new Observable.OnSubscribe<Alarm>() {
            @Override
            public void call(Subscriber<? super Alarm> subscriber) {
                alarm.save();
                SQLite.delete().from(Track.class).where(Track_Table.alarmForeignKeyContainer_id.eq(alarm.getId())).query();
                for (Track track : trackList) {
                    track.associateAlarm(alarm);
                    track.save();
                }
                subscriber.onNext(alarm);
            }
        });

    }

    public static void deleteAlarm(Alarm alarm) {
        SQLite.delete(Alarm.class).where(Alarm_Table.id.is(alarm.getId())).query();
    }


    /***
     *
     * @param alarm
     * @return
     */
    public static boolean canStart(Alarm alarm) {

        boolean alarmOk, dayOk = false;
        Calendar calendar = Calendar.getInstance();

        alarmOk = alarm.getActive() == 1;
        dayOk = alarm.isDayActive(calendar.get(Calendar.DAY_OF_WEEK));
        if (alarm.getRepeated() == 0)
            dayOk=true;
        return alarmOk && dayOk;
    }
}
