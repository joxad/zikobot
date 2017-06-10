package com.joxad.zikobot.app.player.alarm;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.joxad.zikobot.app.alarm.AlarmManager;
import com.joxad.zikobot.app.player.PlayerService;
import com.joxad.zikobot.data.model.Alarm;
import com.joxad.zikobot.data.model.Track;
import com.orhanobut.logger.Logger;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jocelyn on 28/04/2017.
 */

public class AlarmService extends IntentService {

    private PlayerService playerService;
    private ServiceConnection musicConnection;
    private AudioManager am;

    public AlarmService() {
        super(AlarmService.class.getName());
    }

    public static Intent newInstance(Context context, long alarmId) {
        return new Intent(context, AlarmService.class).putExtra("id", alarmId);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        long alarmId = intent.getLongExtra("id", 0);
        AlarmManager.getAlarmById(alarmId).subscribe((alarm) -> {
            AlarmManager.prepareAlarm(this, alarm);
            if (AlarmManager.canStart(alarm)) {
                Logger.d("AlarmReceiver" + alarm.getName());
                am.setStreamVolume(AudioManager.STREAM_MUSIC, alarm.getVolume(), alarm.getVolume());
                if (alarm.getRepeated() == 0) {
                    alarm.setActive(0);
                    alarm.save();
                }
                initService(alarm);
                new Handler().postDelayed(() -> initService(alarm), 1000);
            }
        }, throwable -> {
            Logger.d(throwable.getMessage());
        });
    }

    private void initService(Alarm alarm) {
        musicConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) iBinder;
                playerService = binder.getService();
                start(alarm);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };
        bindService(new Intent(this, PlayerService.class), musicConnection, Context.BIND_AUTO_CREATE);
    }

    private void start(Alarm alarm) {
        List<Track> models = alarm.getTracks();
        if (alarm.isRandom()) {
            Collections.shuffle(models);
        }
        playerService.playTracks(models);
    }

}
