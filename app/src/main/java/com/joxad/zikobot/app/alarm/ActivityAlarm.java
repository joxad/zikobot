package com.joxad.zikobot.app.alarm;

import android.os.Bundle;
import android.view.Menu;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.ActivityAlarmBinding;

/**
 * Created by josh on 29/05/16.
 */
public class ActivityAlarm extends ActivityBase<ActivityAlarmBinding, ActivityAlarmVM> {


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
        return new ActivityAlarmVM(this, binding,savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

}
