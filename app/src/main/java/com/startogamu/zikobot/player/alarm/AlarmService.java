package com.startogamu.zikobot.player.alarm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.db.CurrentPlaylistManager;
import com.joxad.zikobot.data.db.PlaylistManager;
import com.joxad.zikobot.data.db.model.ZikoAlarm;
import com.joxad.zikobot.data.db.model.ZikoPlaylist;
import com.joxad.zikobot.data.db.model.ZikoTrack;
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager;
import com.startogamu.zikobot.Constants;
import com.startogamu.zikobot.core.notification.ZikoNotification;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jocelyn on 28/04/2017.
 */

public class AlarmService extends IntentService {

    private AudioManager am;
    private Ringtone ringtone;
    boolean safeAlarmOn;
    int alarmId = 0;

    public AlarmService() {
        super(AlarmService.class.getName());
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (intent==null)
            return START_NOT_STICKY;
        alarmId = intent.getIntExtra(Constants.Extra.INSTANCE.getALARM_ID(), 0);

        ZikoAlarm alarm = AlarmManager.INSTANCE.queryAlarmById(alarmId);

        new ZikoNotification(this).showNotificationAlarm(alarm, "Wake up");
        safeAlarmOn = false;
        if (AppPrefs.spotifyUser() != null) {
            SpotifyAuthManager.INSTANCE.refreshToken().subscribe(spotifyToken -> {
                new ZikoNotification(this).showNotificationAlarm(alarm, "Refresh token");
                startAlarm(alarm, intent);
            }, throwable -> {
                new ZikoNotification(this).showNotificationAlarm(alarm, "Refresh token error");
                startSafeAlarm();
            });
        } else {
            startAlarm(alarm, intent);
        }
        return START_STICKY;
    }

    public static Intent newInstance(Context context, int alarmId) {
        return new Intent(context, AlarmService.class).putExtra(Constants.Extra.INSTANCE.getALARM_ID(), alarmId);
    }

    private void startAlarm(ZikoAlarm alarm, Intent intent) {
        if (intent != null) {
            if (alarm != null) {
                AlarmManager.INSTANCE.prepareAlarm(this, alarm);
                if (AlarmManager.INSTANCE.canStart(alarm)) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, alarm.getVolume(), alarm.getVolume());
                    am.setStreamVolume(AudioManager.STREAM_ALARM, alarm.getVolume(), alarm.getVolume());

                    if (alarm.getRepeated() == 0) {
                        alarm.setActive(true);
                        alarm.save();
                    }
                    start(alarm);
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

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    private void start(ZikoAlarm alarm) {
        ZikoPlaylist zikoPlaylist = PlaylistManager.INSTANCE.findOne(alarm.getZikoPlaylist().
                getId()).querySingle().blockingGet();
        if (zikoPlaylist != null) {
            List<ZikoTrack> models = PlaylistManager.INSTANCE.findTracks(zikoPlaylist.getId(), -1)
                    .queryList().blockingGet();
            if (alarm.isRandom()) {
                Collections.shuffle(models);
            }
            CurrentPlaylistManager.INSTANCE.play(models);
        }
    }

}