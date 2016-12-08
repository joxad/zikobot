package com.startogamu.zikobot.core.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.core.event.player.EventNextTrack;
import com.startogamu.zikobot.core.module.music.player.VLCPlayer;

import org.greenrobot.eventbus.EventBus;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

/**
 * Created by Jocelyn on 08/12/2016.
 */

public class VLCMediaPlayerService extends Service {

    private final IBinder musicBind = new VLCMediaPlayerService.VLCMediaPlayerServiceBinder();
    private MediaPlayer mediaPlayer;
    private LibVLC libVLC;

    @Override
    public void onCreate() {
        super.onCreate();
        ArrayList<String> options = new ArrayList<>();
        options.add("--http-reconnect");
        options.add("--network-caching=2000");
        options.add("--aout=opensles");
        options.add("--audio-time-stretch"); // time stretching
        options.add("-vvv"); // verbosity
        libVLC = new LibVLC(options);
        mediaPlayer = new MediaPlayer(libVLC);
        mediaPlayer.setEventListener(new MediaPlayerListener());

    }

    //activity will bind to service
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }


    public void play(String ref) {
        Media m = new Media(libVLC, ref);
        mediaPlayer.setMedia(m);
        mediaPlayer.play();
    }
    public void pause() {
        mediaPlayer.pause();
    }

    public void resume() {
        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void seekTo(int position) {
        mediaPlayer.setTime(position);
    }

    public float position() {
        return mediaPlayer.getPosition() * mediaPlayer.getLength();
    }

    public void next() {
        EventBus.getDefault().post(new EventNextTrack());
    }

    //release resources when unbind
    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        Logger.d("INTEND UNBIND MEDIA PLAYER");
        return false;
    }

    public class VLCMediaPlayerServiceBinder extends Binder {
        public VLCMediaPlayerService getService() {
            return VLCMediaPlayerService.this;
        }
    }

    private class MediaPlayerListener implements MediaPlayer.EventListener {
        @Override
        public void onEvent(final MediaPlayer.Event event) {
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
        }
    }
}
