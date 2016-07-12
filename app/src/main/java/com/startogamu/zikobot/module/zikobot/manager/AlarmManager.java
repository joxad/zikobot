package com.startogamu.zikobot.module.zikobot.manager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.startogamu.zikobot.core.receiver.AlarmReceiver;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.module.zikobot.model.Alarm;
import com.startogamu.zikobot.module.zikobot.model.Alarm_Table;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.module.zikobot.model.Track_Table;

import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by josh on 28/03/16.
 */
public class AlarmManager {
    private static android.app.AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;

    public static void init(Context context) {
        if (alarmMgr == null)
            alarmMgr = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }


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
     * @param alarm
     * @return
     */
    public static boolean canStart(Alarm alarm) {

        boolean alarmOk, dayOk = false;
        Calendar calendar = Calendar.getInstance();

        alarmOk = alarm.getActive() == 1;
        dayOk = alarm.isDayActive(calendar.get(Calendar.DAY_OF_WEEK));
        if (alarm.getRepeated() == 0)
            dayOk = true;
        return alarmOk && dayOk;
    }

    public static void prepareAlarm(Context context, Alarm model) {
        init(context);
        if (model.getActive() == 1) {
            cancel(context, model);
            Calendar calendar = Calendar.getInstance();
            long triggerMillis = System.currentTimeMillis();
            calendar.setTimeInMillis(triggerMillis);
            calendar.set(Calendar.HOUR_OF_DAY, model.getHour());
            calendar.set(Calendar.MINUTE, model.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if (calendar.getTimeInMillis() < Calendar.getInstance()
                    .getTimeInMillis()) {
                triggerMillis = calendar.getTimeInMillis() + android.app.AlarmManager.INTERVAL_DAY;
                System.out.println("Alarm will go off next day");
            } else {
                triggerMillis = calendar.getTimeInMillis();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                android.app.AlarmManager.AlarmClockInfo alarmClockInfo = new android.app.AlarmManager.AlarmClockInfo(triggerMillis, alarmIntent);
                alarmMgr.setAlarmClock(alarmClockInfo, alarmIntent);
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmMgr.setExact(android.app.AlarmManager.RTC_WAKEUP, triggerMillis, alarmIntent);
            }else {
                alarmMgr.set(android.app.AlarmManager.RTC_WAKEUP, triggerMillis, alarmIntent);
            }
            model.setTimeInMillis(triggerMillis);
            model.save();
            Logger.d(model.getName() + model.getHour() + "h " + model.getMinute() + "m" + "prepared");

        } else {
            Logger.d(model.getName() + model.getHour() + "h " + model.getMinute() + "m" + "cancelled");
            cancel(context, model);
        }
    }


    public static void cancel(Context context, Alarm alarm) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA.ALARM_ID, alarm.getId());
        alarmIntent = PendingIntent.getBroadcast(context, (int)alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(alarmIntent);
    }
}
