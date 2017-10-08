package com.startogamu.zikobot.player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.joxad.androidtemplate.core.log.AppLog;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.db.CurrentPlaylistManager;
import com.joxad.zikobot.data.db.model.TYPE;
import com.joxad.zikobot.data.db.model.ZikoTrack;
import com.joxad.zikobot.data.player.AndroidPlayer;
import com.joxad.zikobot.data.player.IMusicPlayer;
import com.joxad.zikobot.data.player.SpotifyPlayer;
import com.joxad.zikobot.data.player.VLCPlayer;
import com.startogamu.zikobot.core.ZikoNotification;

import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Jocelyn on 12/12/2016.
 */

public class PlayerService extends MediaBrowserServiceCompat implements IMusicPlayer {

    public static final int DELAY = 200;
    private final IBinder musicBind = new PlayerService.PlayerBinder();
    public boolean playing;

    VLCPlayer vlcPlayer;
    AndroidPlayer androidPlayer;
    SpotifyPlayer spotifyPlayer;
    private int currentProgress = 0;
    private MediaSessionCompat mediaSession;
    private Handler timeHandler;
    private IMusicPlayer currentPlayer;
    private ZikoNotification zikoNotification;
    private LastSession lastSession = null;


    @Override
    public void onCreate() {
        super.onCreate();
        vlcPlayer = new VLCPlayer(this);
        androidPlayer = new AndroidPlayer(this);
        spotifyPlayer = new SpotifyPlayer(this);
        mediaSession = new MediaSessionCompat(this, "PlayerService");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PAUSED, 0, 0)
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .build());

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(focusChange -> {
            // Ignore
        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        mediaSession.setActive(true);
        zikoNotification = new ZikoNotification(this);
        init();
        runHandler();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }

    @Override
    public void init() {
        timeHandler = new Handler();
        vlcPlayer.init();
        if (AppPrefs.spotifyUser() != null) {
            spotifyPlayer.init();
        }
        androidPlayer.init();
        currentPlayer = vlcPlayer;

        CurrentPlaylistManager.INSTANCE.subjectObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::play, throwable -> AppLog.INSTANCE.e(PlayerService.class.getSimpleName(), throwable.getMessage()));

    }


    private void play(ZikoTrack zikoTrack) {
        boolean canPlay = false;
        long ts = new Date().getTime();

        if (lastSession != null && lastSession.getRef() != null) {
            // si c'est la meme chanson, on ne refresh pas deux fois
            if (zikoTrack.getRef().contains(lastSession.getRef()) && ts < lastSession.getTs() + 500) {
                canPlay = false;
            } else {
                canPlay = true;
            }
        } else {
            lastSession = new LastSession(ts, zikoTrack.getRef());
            canPlay = true;
        }
       /* mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentTrackVM.getArtistName())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, currentTrackVM.getName())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, currentTrackVM.getDuration())
                .build());*/
        if (canPlay) {
            zikoNotification.prepareNotification(zikoTrack);
            updatePlayer(zikoTrack);
            play(zikoTrack.getRef());
        }
    }

    private void updatePlayer(ZikoTrack track) {
        currentPlayer.stop();
        switch (track.getType()) {
            case TYPE.LOCAL:
                if (track.getRef().contains(".mp3"))
                    currentPlayer = androidPlayer;
                else
                    currentPlayer = vlcPlayer;
                break;
            case TYPE.SOUNDCLOUD:
                currentPlayer = androidPlayer;
                break;
            case TYPE.SPOTIFY:
                currentPlayer = spotifyPlayer;
                break;
            case TYPE.YOUTUBE:
                currentPlayer = androidPlayer;
                break;
        }
    }

    @Override
    public void play(String ref) {
        currentPlayer.play(ref);
        currentProgress = 0;
        playing = true;
    }


    private void runHandler() {
        timeHandler.postDelayed(() -> {
            if (playing)
                currentProgress += DELAY;
            runHandler();

        }, DELAY);
    }

    @Override
    public void pause() {
        currentPlayer.pause();
        playing = false;

    }

    @Override
    public void resume() {
        currentPlayer.resume();
        playing = true;
    }

    @Override
    public void stop() {
        currentPlayer.stop();
        playing = false;
        currentProgress = 0;
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
        ZikoTrack zikoTrack = CurrentPlaylistManager.INSTANCE.getCurrentTrack();
        if (zikoTrack != null)
            return (int) zikoTrack.getDuration();
        else return 0;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        stopForeground(true);
        return false;
    }

    public class PlayerBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

}