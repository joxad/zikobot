package com.startogamu.zikobot.view.activity;

import android.os.Bundle;
import android.view.Menu;

import com.f2prateek.dart.HensonNavigable;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ActivityAlarmsBinding;
import com.startogamu.zikobot.view.Henson;
import com.startogamu.zikobot.viewmodel.activity.ActivityAlarmsVM;

/***
 * List the alarms
 */
@HensonNavigable
public class ActivityAlarms extends ActivityBase<ActivityAlarmsBinding, ActivityAlarmsVM> {

    private String TAG = ActivityAlarms.class.getSimpleName();

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarms, menu);
        return true;
    }

    public void goToSettingsActivity() {
        startActivity(Henson.with(this).gotoActivitySettings().build());
    }

    @Override
    public int data() {
        return BR.activityAlarmsVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_alarms;
    }

    @Override
    public ActivityAlarmsVM baseActivityVM(ActivityAlarmsBinding binding, Bundle savedInstanceState) {
        return  new ActivityAlarmsVM(this, binding);
    }
}
