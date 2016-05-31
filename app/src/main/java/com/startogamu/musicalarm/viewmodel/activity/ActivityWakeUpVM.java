package com.startogamu.musicalarm.viewmodel.activity;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.musicalarm.databinding.ActivityWakeUpBinding;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.view.activity.ActivityWakeUp;

/**
 * Created by josh on 31/05/16.
 */
public class ActivityWakeUpVM extends ActivityBaseVM<ActivityWakeUp, ActivityWakeUpBinding> {
    private AudioVisualization audioVisualization;

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
        Dart.inject(activity);
        //TODO add information

    }
    @Override
    protected void onResume() {
        super.onResume();
        binding.visualizerView.onResume();
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
