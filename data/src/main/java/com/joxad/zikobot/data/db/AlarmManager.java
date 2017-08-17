package com.joxad.zikobot.data.db;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.joxad.zikobot.data.db.model.ZikoAlarm;
import com.joxad.zikobot.data.db.model.ZikoAlarm_Table;
import com.joxad.zikobot.data.db.model.ZikoAlbum_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.reactivex.Observable;

/**
 * Created by josh on 28/03/16.
 */
public enum AlarmManager {
    INSTANCE;
    private static android.app.AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;
    private Context context;

    public void init(Context context) {
        this.context = context;
        if (alarmMgr == null)
            alarmMgr = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }


    /***
     * Create an observable for alarm
     *
     * @param id playlistId
     * @return
     */
    public ZikoAlarm getAlarmById(final long id) {

        ZikoAlarm alarm = new Select().from(ZikoAlarm.class).where(ZikoAlarm_Table.zikoPlaylist_id.eq(id)).querySingle();
        return alarm;
    }


    /***
     * @param alarm
     */
    public Observable<ZikoAlarm> saveAlarm(ZikoAlarm alarm) {
        return Observable.create(subscriber -> {
            alarm.save();
            subscriber.onNext(alarm);
        });

    }


    public void deleteAlarm(ZikoAlarm alarm) {
        SQLite.delete(ZikoAlarm.class).where(ZikoAlarm_Table.id.is(alarm.getId())).query();
        cancel(context, alarm);
    }


    /***
     * @param alarm
     * @return
     */
    public boolean canStart(ZikoAlarm alarm) {

        boolean alarmOk, dayOk = false;
        Calendar calendar = Calendar.getInstance();

        alarmOk = alarm.getActive();
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
    public void prepareAlarm(Context context, ZikoAlarm model) {
        init(context);

        if (model.getActive()) {
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
        } else {

            cancel(context, model);
        }
    }

    /**
     * Cancel the alarm
     *
     * @param context
     * @param alarm
     */
    public void cancel(Context context, ZikoAlarm alarm) {
      //  Intent intent = new Intent(context, AlarmReceiver.class);
       // intent.putExtra(EXTRA.ALARM_ID, alarm.getId());
      //  alarmIntent = PendingIntent.getBroadcast(context, (int) alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
       // alarmMgr.cancel(alarmIntent);
    }

    /***
     * Find the next alarm to trigger according to the days in the DB
     *
     * @param alarm
     * @return
     */
    public long nextTrigger(ZikoAlarm alarm) {
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
    public int findInterval(ZikoAlarm alarm, long calendarAlarmMs, Calendar calendarToday) {
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

    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    public String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}