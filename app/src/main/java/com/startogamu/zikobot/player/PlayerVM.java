package com.startogamu.zikobot.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.joxad.easydatabinding.base.IVM;
import com.startogamu.zikobot.core.event.player.TrackChangeEvent;
import com.startogamu.zikobot.databinding.ViewPlayerSimpleBinding;
import com.startogamu.zikobot.localtracks.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Jocelyn on 12/12/2016.
 */

public class PlayerVM extends BaseObservable implements IVM {

    private final ViewPlayerSimpleBinding binding;
    private ServiceConnection musicConnection;
    private AppCompatActivity activity;
    private PlayerService playerService;
    public final ObservableBoolean isBound = new ObservableBoolean(false);
    public final ObservableBoolean isExpanded = new ObservableBoolean(false);
    private Intent intent;
    private BottomSheetBehavior<View> behavior;

    public PlayerVM(AppCompatActivity activity, ViewPlayerSimpleBinding binding) {
        this.activity = activity;
        this.binding = binding;
        onCreate();
    }

    @Override
    public void onCreate() {
        behavior = BottomSheetBehavior.from(binding.getRoot());
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                isExpanded.set(newState == BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        intent = new Intent(activity, PlayerService.class);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

    }

    @Override
    public void onResume() {
        if (isBound.get()) return;
        musicConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) iBinder;
                playerService = binder.getService();
                isBound.set(true);
                refresh();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isBound.set(false);
            }
        };
        boolean b = activity.bindService(intent, musicConnection, Context.BIND_AUTO_CREATE);
    }

    private void refresh() {
        notifyChange();
    }


    public void playPause(View view) {
        playerService.pause();
        notifyChange();
    }

    @Subscribe
    public void onReceive(TrackChangeEvent trackChangeEvent) {
        refresh();
    }

    @Override
    public void onPause() {
        isBound.set(false);

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Bindable
    public TrackVM getCurrentTrackVM() {
        if (isBound.get())
            return playerService.currentTrackVM;
        return null;
    }

    @Bindable
    public boolean isPlaying() {
        if (!isBound.get()) return false;
        return playerService.playing.get();
    }

    @Bindable
    public boolean isEmpty() {
        if (!isBound.get()) return true;
        return playerService.isEmpty();
    }

}
