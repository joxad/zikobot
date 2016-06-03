package com.startogamu.musicalarm.view.activity;

import android.os.Bundle;

import com.f2prateek.dart.HensonNavigable;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.musicalarm.BR;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivitySettingsBinding;
import com.startogamu.musicalarm.viewmodel.activity.ActivitySettingsVM;

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
