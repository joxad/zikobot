package com.startogamu.zikobot.view.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ActivityMainBinding;
import com.startogamu.zikobot.viewmodel.activity.ActivityMainVM;

/**
 * Created by josh on 08/06/16.
 */
public class ActivityMain  extends ActivityBase<ActivityMainBinding, ActivityMainVM> {
    @Override
    public int data() {
        return BR.activityMainVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_main;
    }

    @Override
    public ActivityMainVM baseActivityVM(ActivityMainBinding binding, Bundle savedInstanceState) {
        return new ActivityMainVM(this, binding);
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        vm.onPostCreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( vm.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
