package com.startogamu.zikobot.core.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import lombok.Setter;

/**
 * Created by josh on 10/04/16.
 */
public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private static final String TAG = MediaPlayerService.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private final IBinder musicBind = new MediaPlayerServiceBinder();
    @Setter
    private OnDisconnectListener onDisconnectListener;
    private MediaPlayer.OnCompletionListener onCompletionListener;


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void playUrlSong(String url) {
        //play
        mediaPlayer.reset();
        //set the data source
        try {
            mediaPlayer.setDataSource(url);
        } catch (Exception e) {
            Log.e(MediaPlayerService.class.getSimpleName(), "Error setting data source", e);
        }
        mediaPlayer.prepareAsync();
    }



    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer = mp;
        mediaPlayer.start();
        if (onCompletionListener != null)
            mediaPlayer.setOnCompletionListener(onCompletionListener);
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }


    /***
     *
     */
    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void resume() {
        mediaPlayer.start();
    }


    public class MediaPlayerServiceBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    //activity will bind to service
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }


    //release resources when unbind
    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        if (onDisconnectListener != null)
            onDisconnectListener.onDisconnect();
        Logger.d("INTEND UNBIND MEDIA PLAYER");
        return false;
    }

    public void seek(int position) {
        mediaPlayer.seekTo(position);
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public interface OnDisconnectListener {
        void onDisconnect();
    }
}
