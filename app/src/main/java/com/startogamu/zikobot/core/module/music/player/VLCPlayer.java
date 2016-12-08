package com.startogamu.zikobot.core.module.music.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.core.event.player.EventNextTrack;
import com.startogamu.zikobot.core.service.MediaPlayerService;
import com.startogamu.zikobot.core.service.VLCMediaPlayerService;

import org.greenrobot.eventbus.EventBus;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

/**
 * Created by josh on 28/08/16.
 */
public class VLCPlayer implements IMusicPlayer {

    private VLCMediaPlayerService mediaPlayerService;

    private ServiceConnection musicConnection;
    private boolean mediaPlayerServiceBound;
    private Context context;


    public VLCPlayer(Context context) {
        this.context = context;
        init();

    }

    @Override
    public void init() {
        initMediaPlayer(context);

    }

    /***
     * @param context
     */
    private void initMediaPlayer(Context context) {
        musicConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                VLCMediaPlayerService.VLCMediaPlayerServiceBinder binder = (VLCMediaPlayerService.VLCMediaPlayerServiceBinder) service;
                //get service
                mediaPlayerService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mediaPlayerService = null;
                mediaPlayerServiceBound = false;
            }
        };

        Intent playIntent = new Intent(context, VLCMediaPlayerService.class);
        context.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        context.startService(playIntent);

    }



    @Override
    public void play(String ref) {
        mediaPlayerService.play(ref);
    }

    @Override
    public void pause() {
        mediaPlayerService.pause();
    }

    @Override
    public void resume() {
        mediaPlayerService.resume();
    }

    @Override
    public void stop() {
        mediaPlayerService.stop();
    }

    @Override
    public void seekTo(int position) {
        mediaPlayerService.seekTo(position);
    }

    @Override
    public float position() {
        return mediaPlayerService.position();
    }


    @Override
    public void next() {
        mediaPlayerService.next();
    }



}
