package com.joxad.zikobot.data.player;

import android.content.Context;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

/**
 * Created by Jocelyn on 06/02/2017.
 */

public class VLCPlayer implements IMusicPlayer {
    LibVLC libVLC;
    private MediaPlayer vlcPlayer;
    private Context context;

    public VLCPlayer(Context context) {
        this.context = context;
    }

    @Override
    public void init() {
        libVLC = new LibVLC();

        vlcPlayer = new MediaPlayer(libVLC);
        vlcPlayer.setEventListener(event -> {
            switch (event.type) {
                case MediaPlayer.Event.EncounteredError:
                    break;
                case MediaPlayer.Event.MediaChanged:
                    break;
                case MediaPlayer.Event.EndReached:

                    break;
            }
        });
    }

    @Override
    public void play(String ref) {
        vlcPlayer.setMedia(new Media(libVLC, ref));
        vlcPlayer.play();
    }

    @Override
    public void pause() {
        vlcPlayer.pause();

    }

    @Override
    public void resume() {
        vlcPlayer.play();
    }

    @Override
    public void stop() {
        vlcPlayer.stop();
    }

    @Override
    public void seekTo(float percent) {
        vlcPlayer.setPosition(percent);

    }

    @Override
    public void seekTo(int position) {
        //not used
    }


}