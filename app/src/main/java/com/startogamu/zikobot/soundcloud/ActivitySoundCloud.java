package com.startogamu.zikobot.soundcloud;

import android.os.Bundle;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ActivitySoundCloudBinding;
import com.startogamu.zikobot.databinding.ActivitySpotifyBinding;

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
        return new ActivitySoundCloudVM(this, binding);
    }

}
