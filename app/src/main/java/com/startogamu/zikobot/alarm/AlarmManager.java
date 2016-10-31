package com.startogamu.zikobot.alarm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.startogamu.zikobot.core.receiver.AlarmReceiver;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.core.model.Alarm;
import com.startogamu.zikobot.core.model.Alarm_Table;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.core.model.Track_Table;
import com.startogamu.zikobot.widget.AppWidgetHelper;

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
    private static Context context;

    public static void init(Context context) {
        AlarmManager.context = context;
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

    public static String getNextAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ZikoUtils.getDate(alarmMgr.getNextAlarmClock().getTriggerTime(), "dd/MM/yyyy hh:mm:ss");
        } else {
            return Settings.System.getString(context.getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED);
        }
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

    /**
     * Create an observable on dbflow
     *
     * @return
     */
    public static Observable<Alarm> refreshAlarm(final long id) {
        return Observable.create(new Observable.OnSubscribe<Alarm>() {
            @Override
            public void call(Subscriber<? super Alarm> subscriber) {
                Alarm alarm = new Select().from(Alarm.class).where(Alarm_Table.id.eq(id)).querySingle();
                if (subscriber.isUnsubscribed()) return;
                subscriber.onNext(alarm);
            }
        });
    }

    /***
     * @param alarm
     */
    public static Observable<Alarm> saveAlarm(Alarm alarm) {

        return Observable.create(new Observable.OnSubscribe<Alarm>() {
            @Override
            public void call(Subscriber<? super Alarm> subscriber) {
                alarm.save();
                AppWidgetHelper.update(context);
                subscriber.onNext(alarm);
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
                AppWidgetHelper.update(context);
                subscriber.onNext(alarm);
            }
        });

    }

    /***
     * @param text
     * @param alarm
     * @return
     */
    public static Observable<Alarm> editAlarmName(String text, Alarm alarm) {
        return Observable.create(new Observable.OnSubscribe<Alarm>() {
            @Override
            public void call(Subscriber<? super Alarm> subscriber) {
                alarm.setName(text);
                alarm.save();
                AppWidgetHelper.update(context);
                subscriber.onNext(alarm);
            }
        });
    }

    public static void deleteAlarm(Alarm alarm) {
        SQLite.delete(Alarm.class).where(Alarm_Table.id.is(alarm.getId())).query();
        AppWidgetHelper.update(context);
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

    /***
     * Prepare the next alarm to fire
     *
     * @param context
     * @param model
     */
    public static void prepareAlarm(Context context, Alarm model) {
        init(context);
        if (model.getActive() == 1) {
            cancel(context, model);
            long triggerMillis = nextTrigger(model);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                android.app.AlarmManager.AlarmClockInfo alarmClockInfo = new android.app.AlarmManager.AlarmClockInfo(triggerMillis, alarmIntent);
                alarmMgr.setAlarmClock(alarmClockInfo, alarmIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmMgr.setExact(android.app.AlarmManager.RTC_WAKEUP, triggerMillis, alarmIntent);
            } else {
                alarmMgr.set(android.app.AlarmManager.RTC_WAKEUP, triggerMillis, alarmIntent);
            }
            model.setTimeInMillis(triggerMillis);
            model.save();
            Logger.d(model.getName() + model.getHour() + "h " + model.getMinute() + "m" + "prepared");
            AppWidgetHelper.update(context);
        } else {
            Logger.d(model.getName() + model.getHour() + "h " + model.getMinute() + "m" + "cancelled");
            cancel(context, model);
        }
    }

    /**
     * Cancel the alarm
     *
     * @param context
     * @param alarm
     */
    public static void cancel(Context context, Alarm alarm) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA.ALARM_ID, alarm.getId());
        alarmIntent = PendingIntent.getBroadcast(context, (int) alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(alarmIntent);
    }

    /***
     * Find the next alarm to trigger according to the days in the DB
     *
     * @param alarm
     * @return
     */
    public static long nextTrigger(Alarm alarm) {
        Calendar calendarAlarm = Calendar.getInstance();
        Calendar calendarToday = Calendar.getInstance();
        long triggerMillis = System.currentTimeMillis();
        calendarAlarm.setTimeInMillis(triggerMillis);
        calendarAlarm.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendarAlarm.set(Calendar.MINUTE, alarm.getMinute());
        calendarAlarm.set(Calendar.SECOND, 0);
        calendarAlarm.set(Calendar.MILLISECOND, 0);
        int interval = findInterval(alarm, calendarAlarm.getTime().getTime(), calendarToday);
        triggerMillis = calendarAlarm.getTimeInMillis() + interval * android.app.AlarmManager.INTERVAL_DAY;
        return triggerMillis;
    }

    /***
     * find the difference of days for the next alarm to trigger
     *
     * @param alarm
     * @param calendarAlarmMs
     * @param calendarToday @return
     */
    public static int findInterval(Alarm alarm, long calendarAlarmMs, Calendar calendarToday) {
        String alarms = alarm.getDays();//-1111111
        int today = calendarToday.get(Calendar.DAY_OF_WEEK);//2 lundi 15H, alarme a 18h
        // si l'alarme est prévu plus tard aujoudhui
        if (alarm.isDayActive(today) && calendarAlarmMs > calendarToday.getTimeInMillis()) {
            return 0;
        }
        //sinon on cherche le nmb de jours avant la prochaine
        int tomorrow = (today + 1) % 7; //3
        char nextDayAlarm;
        int nextTrigger = -1;
        int nbDayToNextAlarm = 0;
        for (int i = tomorrow; i < 8; i++) {// for 3 to 7
            nbDayToNextAlarm++; // 1 2 3 4 5
            nextDayAlarm = alarms.charAt(i); // days(3) => 0, days(4) => 0 , days(5) => 1, days(6), days(7)
            if (nextDayAlarm == '1') {
                nextTrigger = i;//3=> mardi
                break;
            }
        }
        if (nextTrigger != -1)
            return nbDayToNextAlarm;
        //on teste la semaine suivante
        for (int i = 1; i <= today; i++) { //for 1 to 3
            nbDayToNextAlarm++; // 6
            nextDayAlarm = alarms.charAt(i); // days(1) => 1
            if (nextDayAlarm == '1') {
                break;
            }
        }
        return nbDayToNextAlarm;
    }


    public static class Builder {
        private Context context;

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public void build() throws Exception {
            if (this.context == null)
                throw new Exception("Please set the Context");
            AlarmManager.init(this.context);
        }
    }
}
