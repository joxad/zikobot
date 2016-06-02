package com.startogamu.musicalarm.viewmodel.activity;

import android.animation.Animator;
import android.view.View;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.musicalarm.core.utils.AnimationEndListener;
import com.startogamu.musicalarm.databinding.ActivityWakeUpBinding;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.view.activity.ActivityWakeUp;
import com.startogamu.musicalarm.viewmodel.base.AlarmVM;
import com.startogamu.musicalarm.viewmodel.base.TrackVM;

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
    }

    @Override
    public void init() {
        Dart.inject(this, activity);
        Injector.INSTANCE.spotifyAuth().inject(this);
        Injector.INSTANCE.playerComponent().inject(this);
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(listener -> onBackPressed());
        activity.setTitle(alarm.getName());
        alarmVM = new AlarmVM(activity, alarm);
        binding.setAlarmVM(alarmVM);
        trackVM = new TrackVM(activity, alarm.getTracks().get(0));
        binding.setAlarmTrackVM(trackVM);
        refreshToken();

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
    }

    @Override
    public void destroy() {

    }
}
