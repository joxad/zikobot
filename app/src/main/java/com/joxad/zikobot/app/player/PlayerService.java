package com.joxad.zikobot.app.player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.joxad.zikobot.app.core.notification.PlayerNotification;
import com.joxad.zikobot.app.core.receiver.ZikoMediaCallback;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.player.event.EventAddList;
import com.joxad.zikobot.app.player.event.EventAddTrackToCurrent;
import com.joxad.zikobot.app.player.event.EventAddTrackToEndOfCurrent;
import com.joxad.zikobot.app.player.event.EventNextTrack;
import com.joxad.zikobot.app.player.event.EventPauseMediaButton;
import com.joxad.zikobot.app.player.event.EventPlayMediaButton;
import com.joxad.zikobot.app.player.event.EventPlayTrack;
import com.joxad.zikobot.app.player.event.EventPosition;
import com.joxad.zikobot.app.player.event.EventPreviousTrack;
import com.joxad.zikobot.app.player.event.EventRefreshPlayer;
import com.joxad.zikobot.app.player.event.EventStopPlayer;
import com.joxad.zikobot.app.player.event.TrackChangeEvent;
import com.joxad.zikobot.app.player.player.AndroidPlayer;
import com.joxad.zikobot.app.player.player.IMusicPlayer;
import com.joxad.zikobot.app.player.player.VLCPlayer;
import com.joxad.zikobot.data.model.TYPE;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Jocelyn on 12/12/2016.
 */

public class PlayerService extends Service implements IMusicPlayer {

    public ObservableArrayList<TrackVM> trackVMs = new ObservableArrayList<>();
    public TrackVM currentTrackVM;
    PlayerNotification playerNotification;
    private int currentIndex = 0;
    public ObservableBoolean playing = new ObservableBoolean(false);
    private final IBinder musicBind = new PlayerService.PlayerBinder();
    private int currentProgress = 0;
    public static final int DELAY = 200;
    private MediaSessionCompat mediaSession;
    private Handler timeHandler;
    VLCPlayer vlcPlayer;
    AndroidPlayer androidPlayer;
    private IMusicPlayer currentPlayer;

    public class PlayerBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        vlcPlayer = new VLCPlayer();
        androidPlayer = new AndroidPlayer(this);

        mediaSession = new MediaSessionCompat(this, "PlayerService");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setCallback(new ZikoMediaCallback());

        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PAUSED, 0, 0)
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .build());

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(focusChange -> {
            // Ignore
        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        mediaSession.setActive(true);
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

        playerNotification = new PlayerNotification(this);
        timeHandler = new Handler();
        vlcPlayer.init();
        androidPlayer.init();
        currentPlayer = vlcPlayer;
        //TODO test if other player needed


    }

    @Subscribe
    public void onEvent(EventPlayTrack eventPlayTrack) {
        stopPreviousTrack();

        boolean foundInCurrentList = false;
        if (!trackVMs.isEmpty()) {
            for (int i = 0; i < trackVMs.size(); i++) {
                TrackVM trackVM = trackVMs.get(i);
                if (trackVM.getModel().getId() == eventPlayTrack.getTrack().getModel().getId()) {
                    currentTrackVM = trackVM;
                    foundInCurrentList = true;
                }
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
    public void onEvent(EventAddList eventAddList) {
        stopPreviousTrack();
        trackVMs = eventAddList.getItems();
        currentIndex = 0;
        currentTrackVM = trackVMs.get(currentIndex);
        play(currentTrackVM);
        EventBus.getDefault().post(new TrackChangeEvent());
    }

    @Subscribe
    public void onEvent(EventAddTrackToCurrent eventPlayTrack) {
        if (trackVMs.isEmpty()) {
            trackVMs.add(eventPlayTrack.getTrackVM());
            currentIndex = 0;
            currentTrackVM = trackVMs.get(currentIndex);
            play(currentTrackVM);
            EventBus.getDefault().post(new TrackChangeEvent());
        } else {
            trackVMs.add(currentIndex + 1, eventPlayTrack.getTrackVM());
        }

    }

    @Subscribe
    public void onEvent(EventAddTrackToEndOfCurrent eventPlayTrack) {
        if (trackVMs.isEmpty()) {
            trackVMs.add(eventPlayTrack.getTrackVM());
            currentIndex = 0;
            currentTrackVM = trackVMs.get(currentIndex);
            play(currentTrackVM);
            EventBus.getDefault().post(new TrackChangeEvent());
        } else {
            trackVMs.add(eventPlayTrack.getTrackVM());
        }

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
    public void onEvent(EventPauseMediaButton eventPauseMediaButton) {
        pause();
    }

    @Subscribe
    public void onEvent(EventPlayMediaButton eventPlayMediaButton) {
        if (currentTrackVM != null)
            resume();
        else {

        }
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
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentTrackVM.getArtistName())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, currentTrackVM.getName())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, currentTrackVM.getDuration())
                .build());
        updatePlayer(currentTrackVM);
        play(currentTrackVM.getRef());
    }

    private void updatePlayer(TrackVM currentTrackVM) {
        switch (currentTrackVM.getType()) {
            case TYPE.LOCAL:
                currentPlayer = vlcPlayer;
                break;
            case TYPE.SOUNDCLOUD:
                currentPlayer = androidPlayer;
                break;
        }
    }

    @Override
    public void play(String ref) {
        currentPlayer.play(ref);
        playing.set(true);
        if (currentTrackVM != null) {
            playerNotification.prepareNotification(currentTrackVM.getModel());
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
        currentPlayer.pause();
        playing.set(false);
        EventBus.getDefault().post(new EventRefreshPlayer());

    }

    @Override
    public void resume() {
        currentPlayer.resume();
        playing.set(true);
        EventBus.getDefault().post(new EventRefreshPlayer());
    }

    @Override
    public void stop() {
        stopPreviousTrack();
        currentPlayer.stop();
        playing.set(false);
        currentProgress = 0;
        playerNotification.cancel();
        EventBus.getDefault().post(new EventRefreshPlayer());

    }

    @Override
    public void seekTo(float position) {
        vlcPlayer.seekTo(position);
    }

    @Override
    public void seekTo(int position) {
        currentProgress = position;
        if (currentPlayer instanceof VLCPlayer) seekTo((float) position / (float) positionMax());
        else currentPlayer.seekTo(position);

    }

    public int position() {
        return currentProgress;
    }

    public int positionMax() {
        if (currentTrackVM == null) return 0;
        return (int) currentTrackVM.getModel().getDuration();
    }

    public void next() {
        if (currentIndex < trackVMs.size() - 1) {
            stopPreviousTrack();
            currentIndex++;
            currentTrackVM = trackVMs.get(currentIndex);
            play(currentTrackVM);
            EventBus.getDefault().post(new TrackChangeEvent());
        }
    }

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

        super.onDestroy();
    }


    public boolean isEmpty() {
        return trackVMs.isEmpty();
    }


}
