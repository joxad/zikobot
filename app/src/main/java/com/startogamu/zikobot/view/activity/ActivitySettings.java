package com.startogamu.zikobot.view.activity;

import android.os.Bundle;

import com.f2prateek.dart.HensonNavigable;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ActivitySettingsBinding;
import com.startogamu.zikobot.viewmodel.activity.ActivitySettingsVM;

/**
 * Created by josh on 25/03/16.
 */
@HensonNavigable
public class ActivitySettings extends ActivityBase<ActivitySettingsBinding, ActivitySettingsVM> {


    @Override
    public int data() {
        return BR.activitySettingsVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_settings;
    }

    @Override
    public ActivitySettingsVM baseActivityVM(ActivitySettingsBinding binding, Bundle savedInstanceState) {
        return new ActivitySettingsVM(this,binding);
    }


}
