package com.joxad.zikobot.app.player.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;

import com.joxad.zikobot.app.player.event.EventNextTrack;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by josh on 28/08/16.
 */
public class AndroidPlayer implements IMusicPlayer, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private Context context;
    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener onCompletionListener;

    public AndroidPlayer(Context context) {
        this.context = context;
    }


    @Override
    public void init() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(context,
                PowerManager.PARTIAL_WAKE_LOCK);
        onCompletionListener = mediaPlayer1 -> EventBus.getDefault().post(new EventNextTrack());
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void play(String ref) {
        //play
        mediaPlayer.reset();
        //set the data source
        try {
            mediaPlayer.setDataSource(ref);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.prepareAsync();
    }

    @Override
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void seekTo(float percent) {

    }

    @Override
    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }


    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        mediaPlayer.start();
        if (onCompletionListener != null)
            mediaPlayer.setOnCompletionListener(onCompletionListener);
    }
}
