package com.joxad.zikobot.app.alarm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.joxad.zikobot.app.core.receiver.AlarmReceiver;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.core.utils.ZikoUtils;
import com.joxad.zikobot.app.widget.AppWidgetHelper;
import com.joxad.zikobot.data.db.model.ZikoAlarm;
import com.joxad.zikobot.data.db.model.Track;
import com.joxad.zikobot.data.db.model.Track_Table;
import com.joxad.zikobot.data.db.model.ZikoAlarm_Table;
import com.joxad.zikobot.data.db.model.ZikoPlaylist;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.Calendar;
import java.util.List;

import rx.Observable;

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
    public static Observable<ZikoAlarm> getAlarmById(final long id) {

        return Observable.create(subscriber -> {
            ZikoAlarm alarm = new Select().from(ZikoAlarm.class).where(ZikoAlarm_Table.id.eq(id)).querySingle();
            if (alarm == null) {
                subscriber.onError(new Throwable("No alarm"));
                return;
            }
            if (subscriber.isUnsubscribed()) return;
            subscriber.onNext(alarm);
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
    public static Observable<List<ZikoAlarm>> loadAlarms() {
        return Observable.create(subscriber -> {
            List<ZikoAlarm> organizationList = new Select().from(ZikoAlarm.class).queryList();
            if (subscriber.isUnsubscribed()) return;
            subscriber.onNext(organizationList);
        });
    }

    /**
     * Create an observable on dbflow
     *
     * @return
     */
    public static Observable<ZikoAlarm> refreshAlarm(final long id) {
        return Observable.create(subscriber -> {
            ZikoAlarm alarm = new Select().from(ZikoAlarm.class).where(ZikoAlarm_Table.id.eq(id)).querySingle();
            if (subscriber.isUnsubscribed()) return;
            subscriber.onNext(alarm);
        });
    }

    /***
     * @param alarm
     */
    public static Observable<ZikoAlarm> saveAlarm(ZikoAlarm alarm) {

        return Observable.create(subscriber -> {
            alarm.save();
            AppWidgetHelper.update(context);
            subscriber.onNext(alarm);
        });

    }


    /***
     * @param playlist
     * @param trackList
     */
    public static Observable<ZikoPlaylist> savePlaylist(ZikoPlaylist playlist, List<Track> trackList) {

        return Observable.create(subscriber -> {
            playlist.save();
            SQLite.delete().from(Track.class).where(Track_Table.zikoPlaylistForeignKey_id.eq(playlist.getId())).query();
            for (Track track : trackList) {
                track.associatePlaylist(playlist);
                track.save();
            }
            AppWidgetHelper.update(context);
            subscriber.onNext(playlist);
        });

    }

    /***
     * @param text
     * @param alarm
     * @return
     */
    public static Observable<ZikoAlarm> editPlaylist(String text, ZikoAlarm alarm) {
        return Observable.create(subscriber -> {
            alarm.save();
            AppWidgetHelper.update(context);
            subscriber.onNext(alarm);
        });
    }

    public static void deleteAlarm(ZikoAlarm alarm) {
        SQLite.delete(ZikoAlarm.class).where(ZikoAlarm_Table.id.is(alarm.getId())).query();
        AppWidgetHelper.update(context);
        cancel(context, alarm);
    }


    /***
     * @param alarm
     * @return
     */
    public static boolean canStart(ZikoAlarm alarm) {

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
    public static void prepareAlarm(Context context, ZikoAlarm model) {
        init(context);

        if (model.getActive() == 1) {
            cancel(context, model);
            long triggerMillis = nextTrigger(model);
            // Wakes up the device in Doze Mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, triggerMillis, alarmIntent);
            } else {
                alarmMgr.setExact(android.app.AlarmManager.RTC_WAKEUP, triggerMillis, alarmIntent);

            }

            model.setTimeInMillis(triggerMillis);
            model.save();
        //    Logger.d(model.getName() + model.getHour() + "h " + model.getMinute() + "m" + "prepared");
            AppWidgetHelper.update(context);
        } else {
       //     Logger.d(model.getName() + model.getHour() + "h " + model.getMinute() + "m" + "cancelled");
            cancel(context, model);
        }
    }

    /**
     * Cancel the alarm
     *
     * @param context
     * @param alarm
     */
    public static void cancel(Context context, ZikoAlarm alarm) {
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
    public static long nextTrigger(ZikoAlarm alarm) {
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
    public static int findInterval(ZikoAlarm alarm, long calendarAlarmMs, Calendar calendarToday) {
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
