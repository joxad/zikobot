package com.joxad.zikobot.app.settings;

import android.os.Bundle;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.ActivitySettingsBinding;

/**
 * Created by josh on 25/03/16.
 */
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
        return new ActivitySettingsVM(this, binding);
    }


}
