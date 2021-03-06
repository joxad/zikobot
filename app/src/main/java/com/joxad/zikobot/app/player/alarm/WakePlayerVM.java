package com.joxad.zikobot.app.player.alarm;

import android.animation.Animator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.joxad.easydatabinding.base.IVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.utils.AnimationEndListener;
import com.joxad.zikobot.app.core.utils.Constants;
import com.joxad.zikobot.app.databinding.ViewPlayerSimpleBinding;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.player.event.EventPosition;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.model.Alarm;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.util.Collections;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Jocelyn on 12/12/2016.
 */

public class WakePlayerVM extends BaseObservable implements IVM {

    public final ObservableBoolean isBound = new ObservableBoolean(false);
    private final ViewPlayerSimpleBinding binding;
    public ObservableField<Integer> seekBarValue = new ObservableField<>(0);
    public ItemBinding itemView = ItemBinding.of(BR.trackVM, R.layout.item_track_player);
    private ServiceConnection musicConnection;
    private AppCompatActivity activity;
    private WakePlayerService playerService;
    private Alarm alarm;

    public WakePlayerVM(AppCompatActivity activity, ViewPlayerSimpleBinding binding, Alarm alarm) {
        this.activity = activity;
        this.binding = binding;
        this.alarm = alarm;
        onCreate(null);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstance) {

    }

    @Override
    public void onResume() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        if (isBound.get())
            return;
//        if (AppPrefs.spotifyUser() != null) {
//            try {
//                SpotifyAuthManager.getInstance().refreshToken(activity, (newToken, tokenIdentical) -> {
//                });
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }

        new Handler().postDelayed(this::initService, 1000);

        if (binding != null)
            rotateCD();
    }

    private void initService() {
        musicConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                WakePlayerService.WakePlayerBinder binder = (WakePlayerService.WakePlayerBinder) iBinder;
                playerService = binder.getService();
                isBound.set(true);
                startAlarm(alarm);
                refresh();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isBound.set(false);
            }
        };
        activity.bindService(new Intent(activity, WakePlayerService.class), musicConnection, Context.BIND_AUTO_CREATE);
    }

    private void startAlarm(Alarm alarm) {
        ObservableArrayList<TrackVM> trackVMs = new ObservableArrayList<>();
        for (Track track : alarm.getTracks()) {
            trackVMs.add(new TrackVM(activity, track));
        }
        if (alarm.isRandom()) {
            Collections.shuffle(trackVMs);
        }
        playerService.startAlarm(alarm.getTracks());
    }

    private void refresh() {
        notifyChange();
    }

    @Subscribe
    public void onReceive(WakeTrackChangeEvent trackChangeEvent) {
        refresh();
    }

    @Subscribe
    public void onReceive(EventPosition eventPosition) {
        seekBarValue.set(eventPosition.getValue());
        notifyPropertyChanged(BR.position);
    }

    @Override
    public void onPause() {
        isBound.set(false);
        EventBus.getDefault().unregister(this);
        activity.unbindService(musicConnection);
    }

    @Override
    public void onDestroy() {
    }

    @Bindable
    public TrackVM getCurrentTrackVM() {
        if (isBound.get())
            return playerService.currentTrackVM;
        return null;
    }

    @Bindable
    public boolean isPlaying() {
        if (!isBound.get())
            return false;
        return playerService.playing.get();
    }

    @Bindable
    public boolean isEmpty() {
        if (!isBound.get())
            return true;
        return playerService.isEmpty();
    }

    @Bindable
    public int getPosition() {
        if (!isBound.get())
            return 0;
        return seekBarValue.get();
    }

    @Bindable
    public int getPositionMax() {
        if (!isBound.get())
            return 0;
        return playerService.positionMax();
    }

    @Bindable
    public ObservableArrayList<TrackVM> getTrackVMs() {
        if (!isBound.get())
            return new ObservableArrayList<>();
        return playerService.trackVMs;
    }

    /**
     * Handle the back press
     *
     * @return
     */
    public boolean onBackPressed() {
        return true;
    }


    /**
     * TODO call this somewhere
     */
    private void rotateCD() {

        binding.layoutVinyl.rlPlayer.animate().rotationBy(isPlaying() ? Constants.ROTATION : 0).setDuration(50).setListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rotateCD();
            }
        });
    }


}
