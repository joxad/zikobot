package com.startogamu.musicalarm.view.activity;

import android.os.Bundle;
import android.view.Menu;

import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.musicalarm.BR;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivityAlarm2Binding;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.viewmodel.activity.ActivityAlarm2VM;

/**
 * Created by josh on 29/05/16.
 */
public class ActivityAlarm2 extends ActivityBase<ActivityAlarm2Binding, ActivityAlarm2VM>{


    @InjectExtra
    Alarm alarm;

    @Override
    public int data() {
        return BR.activityAlarmVM2;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_alarm_2;
    }

    @Override
    public ActivityAlarm2VM baseActivityVM(ActivityAlarm2Binding binding, Bundle savedInstanceState) {
        return new ActivityAlarm2VM(this, binding);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

}
