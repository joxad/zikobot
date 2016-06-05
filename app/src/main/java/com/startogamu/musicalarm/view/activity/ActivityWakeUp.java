package com.startogamu.musicalarm.view.activity;

import android.os.Bundle;

import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.musicalarm.BR;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivityWakeUpBinding;
import com.startogamu.musicalarm.module.alarm.model.Alarm;
import com.startogamu.musicalarm.viewmodel.activity.ActivityWakeUpVM;

/**
 * Created by josh on 31/05/16.
 */
public class ActivityWakeUp extends ActivityBase<ActivityWakeUpBinding, ActivityWakeUpVM> {

    @InjectExtra
    Alarm alarm;
    @Override
    public int data() {
        return BR.activityWakeUpVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_wake_up;
    }

    @Override
    public ActivityWakeUpVM baseActivityVM(ActivityWakeUpBinding binding, Bundle savedInstanceState) {
        return new ActivityWakeUpVM(this, binding);
    }

    @Override
    protected void onDestroy() {
        vm.destroy();
        super.onDestroy();
    }
}
