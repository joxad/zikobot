package com.startogamu.zikobot.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.joxad.easydatabinding.base.IVM;
import com.startogamu.zikobot.core.event.player.TrackChangeEvent;
import com.startogamu.zikobot.localtracks.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Jocelyn on 12/12/2016.
 */

public class PlayerVM extends BaseObservable implements IVM {

    private ServiceConnection musicConnection;
    private AppCompatActivity activity;
    private PlayerService playerService;
    private boolean isBound = false;
    private Intent intent;

    public PlayerVM(AppCompatActivity activity) {
        this.activity = activity;
        onCreate();
    }

    @Override
    public void onCreate() {
        intent = new Intent(activity, PlayerService.class);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onResume() {
        if (isBound) return;

        musicConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) iBinder;
                playerService = binder.getService();
                isBound = true;
                refresh();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isBound = false;
            }
        };
        boolean b = activity.bindService(intent, musicConnection, Context.BIND_AUTO_CREATE);
    }

    private void refresh() {
        notifyChange();
    }


    @Subscribe
    public void onReceive(TrackChangeEvent trackChangeEvent) {
        refresh();
    }

    @Override
    public void onPause() {
        if (isBound) isBound = false;

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Bindable
    public TrackVM getCurrentTrackVM() {
        if (isBound)
            return playerService.currentTrackVM;
        return null;
    }
}
