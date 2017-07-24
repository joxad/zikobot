package com.joxad.zikobot.app.soundcloud;

import android.os.Bundle;
import android.view.Menu;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.ActivitySoundCloudBinding;

/**
 * Created by josh on 09/08/16.
 */
public class ActivitySoundCloud extends ActivityBase<ActivitySoundCloudBinding, ActivitySoundCloudVM> {
    @Override
    public int data() {
        return BR.activitySoundCloudVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_sound_cloud;
    }

    @Override
    public ActivitySoundCloudVM baseActivityVM(ActivitySoundCloudBinding binding, Bundle savedInstanceState) {
        return new ActivitySoundCloudVM(this,binding,savedInstanceState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }
}
