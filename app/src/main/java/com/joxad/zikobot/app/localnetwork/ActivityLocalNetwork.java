package com.joxad.zikobot.app.localnetwork;

import android.os.Bundle;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.ActivityLocalNetworkBinding;

/**
 * Created by josh on 10/10/16.
 */

public class ActivityLocalNetwork extends ActivityBase<ActivityLocalNetworkBinding, ActivityLocalNetworkVM> {
    @Override
    public int data() {
        return BR.activityLocalNetworkVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_local_network;
    }

    @Override
    public ActivityLocalNetworkVM baseActivityVM(ActivityLocalNetworkBinding binding, Bundle savedInstanceState) {
        return new ActivityLocalNetworkVM(this,binding);
    }
}
