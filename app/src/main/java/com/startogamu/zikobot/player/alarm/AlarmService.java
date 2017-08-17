package com.startogamu.zikobot.player.alarm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.joxad.androidtemplate.core.service.NonStopIntentService;
import com.joxad.zikobot.data.db.AlarmManager;
import com.joxad.zikobot.data.db.CurrentPlaylistManager;
import com.joxad.zikobot.data.db.model.ZikoAlarm;
import com.joxad.zikobot.data.db.model.ZikoPlaylist;
import com.joxad.zikobot.data.db.model.ZikoTrack;
import com.joxad.zikobot.data.player.PlayerService;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jocelyn on 28/04/2017.
 */

public class AlarmService extends NonStopIntentService {

    private PlayerService playerService;
    private ServiceConnection musicConnection;
    private AudioManager am;
    private Ringtone ringtone;
    boolean safeAlarmOn;

    public AlarmService() {
        super(AlarmService.class.getName());
    }

    public static Intent newInstance(Context context, long alarmId) {
        return new Intent(context, AlarmService.class).putExtra("id", alarmId);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        safeAlarmOn = false;
        long alarmId = 0;
        if (intent != null) {
            alarmId = intent.getLongExtra("id", 0);
            ZikoAlarm alarm = AlarmManager.INSTANCE.getAlarmById(alarmId);
            if (alarm != null) {
                AlarmManager.INSTANCE.prepareAlarm(this, alarm);
                if (AlarmManager.INSTANCE.canStart(alarm)) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, alarm.getVolume(), alarm.getVolume());
                    am.setStreamVolume(AudioManager.STREAM_ALARM, alarm.getVolume(), alarm.getVolume());

                    if (alarm.getRepeated() == 0) {
                        alarm.setActive(true);
                        alarm.save();
                    }
                    initService(alarm);
                    new Handler().postDelayed(() -> initService(alarm), 1000);
                }
            }

        }
    }


    private void startSafeAlarm() {
        if (!safeAlarmOn) {
            Uri safeAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (safeAlarm == null)
                safeAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), safeAlarm);
            ringtone.play();
            safeAlarmOn = true;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initService(ZikoAlarm alarm) {
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

    private void start(ZikoAlarm alarm) {
        ZikoPlaylist zikoPlaylist = alarm.getZikoPlaylist();
        if (zikoPlaylist != null) {
            zikoPlaylist.load();
            List<ZikoTrack> models = zikoPlaylist.getTracks();
            if (alarm.isRandom()) {
                Collections.shuffle(models);
            }
            CurrentPlaylistManager.INSTANCE.play(zikoPlaylist.getForeignTracks());
        }
    }

}