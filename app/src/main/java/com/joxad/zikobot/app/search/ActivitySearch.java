package com.joxad.zikobot.app.search;

import android.os.Bundle;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.ActivitySearchBinding;

/**
 * Created by josh on 12/08/16.
 */
public class ActivitySearch extends ActivityBase<ActivitySearchBinding, ActivitySearchVM> {
    @Override
    public int data() {
        return BR.activitySearchVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_search;
    }

    @Override
    public ActivitySearchVM baseActivityVM(ActivitySearchBinding binding, Bundle savedInstanceState) {
        return new ActivitySearchVM(this,binding,savedInstanceState);
    }
}
