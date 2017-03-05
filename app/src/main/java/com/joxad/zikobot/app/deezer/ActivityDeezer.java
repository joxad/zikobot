package com.joxad.zikobot.app.deezer;

import android.os.Bundle;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.ActivityDeezerBinding;

/**
 * Created by josh on 25/08/16.
 */
public class ActivityDeezer extends ActivityBase<ActivityDeezerBinding, ActivityDeezerVM> {
    @Override
    public int data() {
        return BR.activityDeezerVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_deezer;
    }

    @Override
    public ActivityDeezerVM baseActivityVM(ActivityDeezerBinding binding, Bundle savedInstanceState) {
        return new ActivityDeezerVM(this, binding);
    }
}
