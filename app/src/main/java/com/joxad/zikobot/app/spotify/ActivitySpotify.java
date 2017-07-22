package com.joxad.zikobot.app.spotify;

import android.os.Bundle;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.ActivitySpotifyBinding;

/**
 * Created by josh on 09/08/16.
 */
public class ActivitySpotify extends ActivityBase<ActivitySpotifyBinding, ActivitySpotifyVM> {
    @Override
    public int data() {
        return BR.activitySpotifyVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_spotify;
    }

    @Override
    public ActivitySpotifyVM baseActivityVM(ActivitySpotifyBinding binding, Bundle savedInstanceState) {
        return new ActivitySpotifyVM(this,binding,savedInstanceState);
    }

}
