package com.startogamu.zikobot.module.music.manager;

import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.core.event.player.EventNextTrack;

import org.greenrobot.eventbus.EventBus;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

/**
 * Created by josh on 28/08/16.
 */
public class VLCPlayer implements IMusicPlayer {

    private MediaPlayer mediaPlayer;

    private LibVLC libVLC;


    public VLCPlayer() {
        init();

    }

    @Override
    public void init() {
        ArrayList<String> options = new ArrayList<>();
        options.add("--http-reconnect");
        options.add("--network-caching=2000");
        libVLC = new LibVLC(options);
        mediaPlayer = new MediaPlayer(libVLC);
        mediaPlayer.setEventListener(new MediaPlayerListener());
    }


    @Override
    public void play(String ref) {
        Media m = new Media(libVLC, ref);
        mediaPlayer.setMedia(m);
        mediaPlayer.play();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void resume() {
        mediaPlayer.play();
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
    }

    @Override
    public void seekTo(int position) {
        mediaPlayer.setTime(position);
    }

    @Override
    public float position() {
        return mediaPlayer.getPosition() * mediaPlayer.getLength();
    }


    @Override
    public void next() {
        EventBus.getDefault().post(new EventNextTrack());
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
