package com.startogamu.musicalarm.viewmodel.activity;

import android.view.View;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.VisualizerDbmHandler;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.musicalarm.databinding.ActivityWakeUpBinding;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.view.activity.ActivityWakeUp;

import java.io.UnsupportedEncodingException;

/**
 * Created by josh on 31/05/16.
 */
public class ActivityWakeUpVM extends ActivityBaseVM<ActivityWakeUp, ActivityWakeUpBinding> {

    @InjectExtra
    Alarm alarm;
    /***
     * @param activity
     * @param binding
     */
    public ActivityWakeUpVM(ActivityWakeUp activity, ActivityWakeUpBinding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {

        Injector.INSTANCE.spotifyAuth().inject(this);
        Injector.INSTANCE.playerComponent().inject(this);
        Dart.inject(this,activity);
        //TODO add information
        refreshToken();

    }
    @Override
    protected void onResume() {
        super.onResume();
        binding.visualizerView.onResume();
    }

    /***
     * We refresh the token of spotify to be sure
     */
    private void refreshToken() {
        try {
            Injector.INSTANCE.spotifyAuth().manager().refreshToken(activity, () -> {
                Injector.INSTANCE.playerComponent().manager().refreshAccessTokenPlayer();
                startAlarm(alarm);

                VisualizerDbmHandler vizualizerHandler = DbmHandler.Factory.newVisualizerHandler(activity, 0);
                binding.visualizerView.linkTo(vizualizerHandler);
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
    }

    public void stop(View view){
        Injector.INSTANCE.playerComponent().manager().stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.visualizerView.onPause();
    }

    @Override
    public void destroy() {

    }
}
