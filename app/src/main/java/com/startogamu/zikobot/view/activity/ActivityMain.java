package com.startogamu.zikobot.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ActivityMainBinding;
import com.startogamu.zikobot.viewmodel.activity.ActivityMainVM;

/**
 * Created by josh on 08/06/16.
 */

public class ActivityMain extends ActivityBase<ActivityMainBinding, ActivityMainVM> {
    @Nullable
    @InjectExtra
    String fromWidget;


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
