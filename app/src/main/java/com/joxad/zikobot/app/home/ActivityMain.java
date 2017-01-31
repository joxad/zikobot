package com.joxad.zikobot.app.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.ActivityMainBinding;

/**
 * Created by josh on 08/06/16.
 */

public class ActivityMain extends ActivityBase<ActivityMainBinding, ActivityMainVM> {
    @Nullable
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
