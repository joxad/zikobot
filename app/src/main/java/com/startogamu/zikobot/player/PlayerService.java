package com.startogamu.zikobot.player;

import android.app.Service;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.event.player.EventPlayTrack;
import com.startogamu.zikobot.core.event.player.TrackChangeEvent;
import com.startogamu.zikobot.core.module.music.player.IMusicPlayer;
import com.startogamu.zikobot.core.notification.PlayerNotification;
import com.startogamu.zikobot.localtracks.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

/**
 * Created by Jocelyn on 12/12/2016.
 */

public class PlayerService extends Service implements IMusicPlayer {

    public ObservableArrayList<TrackVM> trackVMs = new ObservableArrayList<>();
    public TrackVM currentTrackVM;
    private MediaPlayer vlcPlayer;
    PlayerNotification playerNotification;
    private int currentIndex = 0;
    LibVLC libVLC;
    public ObservableBoolean playing = new ObservableBoolean(false);
    private final IBinder musicBind = new PlayerService.PlayerBinder();

    public class PlayerBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }


    @Override
    public void init() {
        libVLC = new LibVLC();
        vlcPlayer = new MediaPlayer(libVLC);
        vlcPlayer.setEventListener(event -> {
            switch (event.type) {
                case MediaPlayer.Event.EncounteredError:
                    Logger.d(event.toString());
                    break;
                case MediaPlayer.Event.MediaChanged:
                    Logger.d("VLC Media changed");
                    break;
                case MediaPlayer.Event.EndReached:
                    Logger.d("VLOC Media ended");
                    next();
                    break;
            }
        });
        playerNotification = new PlayerNotification(this);
    }

    @Subscribe
    public void onEvent(EventPlayTrack eventPlayTrack) {
        currentTrackVM = eventPlayTrack.getTrack();
        play(currentTrackVM.getRef());
        EventBus.getDefault().post(new TrackChangeEvent());
    }

    @Subscribe
    public void onEvent(EventAddTrackToPlayer eventPlayTrack) {
        trackVMs = eventPlayTrack.getItems();
        currentIndex = 0;
        currentTrackVM = trackVMs.get(currentIndex);
        play(currentTrackVM.getRef());
        EventBus.getDefault().post(new TrackChangeEvent());
    }


    @Override
    public void play(String ref) {
        vlcPlayer.setMedia(new Media(libVLC, ref));
        vlcPlayer.play();
        playing.set(true);
    }

    @Override
    public void pause() {
        vlcPlayer.pause();
        playing.set(false);

    }

    @Override
    public void resume() {
        vlcPlayer.play();
        playing.set(true);

    }

    @Override
    public void stop() {
        vlcPlayer.stop();
        playing.set(false);

    }

    @Override
    public void seekTo(int position) {
        vlcPlayer.setPosition(position);
    }

    @Override
    public float position() {
        return vlcPlayer.getPosition();
    }

    @Override
    public void next() {
        currentIndex++;
        if (currentIndex < trackVMs.size()) {
            currentTrackVM = trackVMs.get(currentIndex);
            play(currentTrackVM.getRef());
            EventBus.getDefault().post(new TrackChangeEvent());
        }
    }


    @Override
    public boolean onUnbind(Intent intent) {
        if (currentTrackVM != null) {
            startForeground(playerNotification.getNotificationId(), playerNotification.show(currentTrackVM.getModel()));
        }
        return false;
    }

    @Override
    public void onDestroy() {
        //TODO start foregroundservice if current trackvm & tracks vms to handle
        EventBus.getDefault().unregister(this);
        vlcPlayer.release();
        super.onDestroy();
    }

    public boolean isEmpty() {
        return trackVMs.isEmpty();
    }
}
