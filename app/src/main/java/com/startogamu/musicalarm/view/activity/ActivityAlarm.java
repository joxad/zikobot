package com.startogamu.musicalarm.view.activity;

import android.os.Bundle;
import android.view.Menu;

import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.musicalarm.BR;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivityAlarmBinding;
import com.startogamu.musicalarm.module.alarm.model.Alarm;
import com.startogamu.musicalarm.viewmodel.activity.ActivityAlarmVM;

/**
 * Created by josh on 29/05/16.
 */
public class ActivityAlarm extends ActivityBase<ActivityAlarmBinding, ActivityAlarmVM>{


    @InjectExtra
    Alarm alarm;

    @Override
    public int data() {
        return BR.activityAlarmVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_alarm;
    }

    @Override
    public ActivityAlarmVM baseActivityVM(ActivityAlarmBinding binding, Bundle savedInstanceState) {
        return new ActivityAlarmVM(this, binding);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

}