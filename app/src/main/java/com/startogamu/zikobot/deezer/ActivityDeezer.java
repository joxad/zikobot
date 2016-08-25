package com.startogamu.zikobot.deezer;

import android.app.Activity;
import android.os.Bundle;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ActivityDeezerBinding;

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
        return new ActivityDeezerVM(this,binding);
    }
}
