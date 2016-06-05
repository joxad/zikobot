package com.startogamu.zikobot.view.activity;

import android.os.Bundle;
import android.view.Menu;

import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ActivityAlarmBinding;
import com.startogamu.zikobot.module.alarm.model.Alarm;
import com.startogamu.zikobot.viewmodel.activity.ActivityAlarmVM;

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
