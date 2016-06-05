package com.startogamu.musicalarm.viewmodel.activity;

import android.animation.Animator;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.VisualizerDbmHandler;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.musicalarm.core.event.TrackChangeEvent;
import com.startogamu.musicalarm.core.utils.AnimationEndListener;
import com.startogamu.musicalarm.databinding.ActivityWakeUpBinding;
import com.startogamu.musicalarm.module.alarm.model.Alarm;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.module.mock.Mock;
import com.startogamu.musicalarm.view.activity.ActivityWakeUp;
import com.startogamu.musicalarm.viewmodel.base.AlarmVM;
import com.startogamu.musicalarm.viewmodel.base.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;

/**
 * Created by josh on 31/05/16.
 */
public class ActivityWakeUpVM extends ActivityBaseVM<ActivityWakeUp, ActivityWakeUpBinding> {


    private static final int ROTATION = 2;
    private static final int DELAY = 10;//MS
    @InjectExtra
    Alarm alarm;


    int rotation = ROTATION;
    public AlarmVM alarmVM;
    public TrackVM trackVM;

    /***
     * @param activity
     * @param binding
     */
    public ActivityWakeUpVM(ActivityWakeUp activity, ActivityWakeUpBinding binding) {
        super(activity, binding);
        Injector.INSTANCE.spotifyAuth().inject(this);
        Injector.INSTANCE.playerComponent().inject(this);

    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        Dart.inject(this, activity);
       activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(listener -> onBackPressed());
        activity.setTitle(alarm.getName());
        alarmVM = new AlarmVM(activity, alarm);
        binding.setAlarmVM(alarmVM);
        if (!alarm.getTracks().isEmpty()) {
            trackVM = new TrackVM(activity, Mock.track(activity));
            binding.setAlarmTrackVM(trackVM);
        }
        refreshToken();

    }

    @Subscribe
    public void onReceived(TrackChangeEvent trackChangeEvent) {
        trackVM.updateTrack(trackChangeEvent.getTrack());
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /***
     * We refresh the token of spotify to be sure
     */
    private void refreshToken() {
        try {
            Injector.INSTANCE.spotifyAuth().manager().refreshToken(activity, () -> {
                Injector.INSTANCE.playerComponent().manager().refreshAccessTokenPlayer();
                startAlarm(alarm);

            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param alarm
     */
    private void startAlarm(Alarm alarm) {
        Injector.INSTANCE.playerComponent().manager().startAlarm(alarm);
        rotateCD();
    }

    /**
     *
     */
    private void rotateCD() {
        binding.rlPlayer.animate().rotationBy(rotation).setDuration(50).setListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rotateCD();
            }
        });
    }

    /***
     *
     * @param view
     */
    public void stop(View view) {
        Injector.INSTANCE.playerComponent().manager().stop();
        activity.finish();
    }


    @Override
    protected boolean onBackPressed() {
        stop(null);
        return super.onBackPressed();

    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void destroy() {
    }
}
