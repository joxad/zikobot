package com.startogamu.zikobot.core.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.module.alarm.manager.AlarmManager;
import com.startogamu.zikobot.module.alarm.model.Alarm;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.view.Henson;

/**
 * Created by josh on 28/03/16.
 */
public class AlarmService extends Service {

    Alarm alarm;

    private static final String TAG = AlarmService.class.getSimpleName();

    /***
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        long alarmId = intent.getLongExtra(EXTRA.ALARM_ID, -1);
        AlarmManager.getAlarmById(alarmId).subscribe((alarm) -> {
            if (AlarmManager.canStart(alarm)) {
                AlarmService.this.alarm = alarm;
                if (alarm.getRepeated()==0) {
                    alarm.setActive(0);
                    alarm.save();
                }
                startActivity(Henson.with(getBaseContext()).gotoActivityWakeUp().alarm(alarm).build().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        return Service.START_NOT_STICKY;
    }




    /***
     *
     */
    private void stop() {
        Injector.INSTANCE.playerComponent().manager().stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
