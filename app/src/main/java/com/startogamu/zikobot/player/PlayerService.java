package com.startogamu.zikobot.player;

import android.app.Service;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.core.event.player.EventAddTrackToCurrent;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.event.player.EventNextTrack;
import com.startogamu.zikobot.core.event.player.EventPlayTrack;
import com.startogamu.zikobot.core.event.player.EventPosition;
import com.startogamu.zikobot.core.event.player.EventPreviousTrack;
import com.startogamu.zikobot.core.event.player.EventStopPlayer;
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
    private int currentProgress = 0;
    public static final int DELAY = 200;

    private Handler timeHandler;

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
        runHandler();
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
        timeHandler = new Handler();
    }

    @Subscribe
    public void onEvent(EventPlayTrack eventPlayTrack) {
        stopPreviousTrack();

        boolean foundInCurrentList = false;
        for (int i = 0; i < trackVMs.size(); i++) {
            TrackVM trackVM = trackVMs.get(i);
            if (trackVM.getModel().getId() == eventPlayTrack.getTrack().getModel().getId()) {
                currentTrackVM = trackVM;
                foundInCurrentList = true;
            }
        }
        if (!foundInCurrentList) {
            currentTrackVM = eventPlayTrack.getTrack();
            trackVMs.add(currentTrackVM);
        }
        play(currentTrackVM);
        EventBus.getDefault().post(new TrackChangeEvent());
    }


    @Subscribe
    public void onEvent(EventAddTrackToPlayer eventPlayTrack) {
        stopPreviousTrack();
        trackVMs = eventPlayTrack.getItems();
        currentIndex = 0;
        currentTrackVM = trackVMs.get(currentIndex);
        play(currentTrackVM);
        EventBus.getDefault().post(new TrackChangeEvent());
    }

    @Subscribe
    public void onEvent(EventAddTrackToCurrent eventPlayTrack) {
        stopPreviousTrack();
        trackVMs.add(eventPlayTrack.getTrackVM());
        currentIndex = 0;
        currentTrackVM = trackVMs.get(currentIndex);
        play(currentTrackVM);
        EventBus.getDefault().post(new TrackChangeEvent());
    }

    @Subscribe
    public void onEvent(EventStopPlayer eventStopPlayer) {
        stopForeground(true);
        stop();
    }

    @Subscribe
    public void onEvent(EventNextTrack eventNextTrack) {
        next();
    }

    @Subscribe
    public void onEvent(EventPreviousTrack eventPreviousTrack) {
        previous();
    }

    private void stopPreviousTrack() {
        if (currentTrackVM != null)
            currentTrackVM.isPlaying.set(false);
    }

    private void play(TrackVM currentTrackVM) {
        currentTrackVM.isPlaying.set(true);
        play(currentTrackVM.getRef());
    }

    @Override
    public void play(String ref) {
        vlcPlayer.setMedia(new Media(libVLC, ref));
        vlcPlayer.play();
        playing.set(true);
        if (currentTrackVM != null) {
            startForeground(playerNotification.getNotificationId(), playerNotification.show(currentTrackVM.getModel()));
        }
        currentProgress = 0;

    }

    private void runHandler() {
        timeHandler.postDelayed(() -> {
            if (playing.get())
                currentProgress += DELAY;
            EventBus.getDefault().post(new EventPosition(currentProgress));
            runHandler();
        }, DELAY);
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
        stopPreviousTrack();
        currentTrackVM = null;
        vlcPlayer.stop();
        playing.set(false);
        currentProgress = 0;
    }

    @Override
    public void seekTo(int position) {
        currentProgress = position;
        float p = (float) position / (float) positionMax();
        vlcPlayer.setPosition(p);
    }

    @Override
    public int position() {
        return currentProgress;
    }

    @Override
    public int positionMax() {
        if (currentTrackVM == null) return 0;
        return (int) currentTrackVM.getModel().getDuration();
    }

    @Override
    public void next() {
        if (currentIndex < trackVMs.size() - 1) {
            stopPreviousTrack();
            currentIndex++;
            currentTrackVM = trackVMs.get(currentIndex);
            play(currentTrackVM);
            EventBus.getDefault().post(new TrackChangeEvent());
        }
    }

    @Override
    public void previous() {
        if (currentIndex > 0) {
            stopPreviousTrack();
            currentIndex--;
            currentTrackVM = trackVMs.get(currentIndex);
            play(currentTrackVM);
            EventBus.getDefault().post(new TrackChangeEvent());
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopForeground(true);
        return false;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        vlcPlayer.release();
        super.onDestroy();
    }


    public boolean isEmpty() {
        return trackVMs.isEmpty();
    }


}
