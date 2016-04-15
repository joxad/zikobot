package com.startogamu.musicalarm.core.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.startogamu.musicalarm.core.notification.MusicNotification;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.module.alarm.manager.AlarmManager;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.component.Injector;

import java.io.UnsupportedEncodingException;

/**
 * Created by josh on 28/03/16.
 */
public class AlarmService extends Service {

    Alarm alarm;

    private static final String TAG = AlarmService.class.getSimpleName();

    /***
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Injector.INSTANCE.spotifyAuth().inject(this);
        Injector.INSTANCE.playerComponent().inject(this);
        long alarmId = intent.getLongExtra(EXTRA.ALARM_ID, -1);
        AlarmManager.getAlarmById(alarmId).subscribe((alarm) -> {
            AlarmService.this.alarm = alarm;
            MusicNotification.show(alarm.getName());
            refreshToken();
        });
        return Service.START_STICKY;
    }

    /***
     * We refresh the token of spotify to be sure
     */
    private void refreshToken() {
        try {
            Injector.INSTANCE.spotifyAuth().manager().refreshToken(getApplicationContext(), () -> {
                Injector.INSTANCE.playerComponent().manager().refreshAccessTokenPlayer();
                startAlarm(alarm);
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private void startAlarm(Alarm alarm) {
        Injector.INSTANCE.playerComponent().manager().startAlarm(alarm);
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
