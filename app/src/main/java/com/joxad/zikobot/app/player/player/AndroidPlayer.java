package com.joxad.zikobot.app.player.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;

import com.joxad.zikobot.app.player.event.EventNextTrack;
import com.joxad.zikobot.data.service.MediaPlayerService;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by josh on 28/08/16.
 */
public class AndroidPlayer implements IMusicPlayer {

    private MediaPlayerService mediaPlayerService;
    private boolean mediaPlayerServiceBound = false;

    /***
     * service connexion that will handle the binding with the {@link MediaPlayerService} service
     */
    private ServiceConnection musicConnection;
    private Context context;

    public AndroidPlayer(Context context) {
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
                MediaPlayerService.MediaPlayerServiceBinder binder = (MediaPlayerService.MediaPlayerServiceBinder) service;
                //get service
                mediaPlayerService = binder.getService();
                mediaPlayerService.setOnCompletionListener(mp -> next());
                mediaPlayerService.setOnDisconnectListener(() -> {
                    mediaPlayerService = null;
                    mediaPlayerServiceBound = false;
                });
                //pass list
                mediaPlayerServiceBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mediaPlayerService = null;
                mediaPlayerServiceBound = false;
            }
        };

        Intent playIntent = new Intent(context, MediaPlayerService.class);
        context.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        context.startService(playIntent);

    }


    public void play(Uri ref) {
        if (mediaPlayerService == null) {
            initMediaPlayer(context);
        } else {
            mediaPlayerService.playSong(ref);
        }
    }

    @Override
    public void play(String ref) {
        if (mediaPlayerService == null) {
            initMediaPlayer(context);
        } else {
            mediaPlayerService.playUrlSong(ref);
        }
    }

    @Override
    public void pause() {
        if (mediaPlayerService != null) {
            mediaPlayerService.pause();
        }
    }

    @Override
    public void resume() {
        if (mediaPlayerService != null) {
            mediaPlayerService.resume();
        }
    }

    @Override
    public void stop() {
        mediaPlayerService.stop();

    }

    @Override
    public void seekTo(int position) {
        mediaPlayerService.seek(position);
    }

    @Override
    public int position() {
        return mediaPlayerService.getCurrentPosition();
    }

    @Override
    public int positionMax() {
        return 0;
    }

    @Override
    public void next() {
        EventBus.getDefault().post(new EventNextTrack());
    }

    @Override
    public void previous() {

    }


}
