package com.startogamu.musicalarm.view.activity;

import android.os.Bundle;
import android.view.Menu;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.musicalarm.BR;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivityAlarmsBinding;
import com.startogamu.musicalarm.view.Henson;
import com.startogamu.musicalarm.viewmodel.activity.ActivityAlarmsVM;

/***
 * List the alarms
 */
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
