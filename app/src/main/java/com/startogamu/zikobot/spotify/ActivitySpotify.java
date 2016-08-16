package com.startogamu.zikobot.spotify;

import android.os.Bundle;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ActivityAlbumBinding;
import com.startogamu.zikobot.databinding.ActivitySpotifyBinding;

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
        return new ActivitySpotifyVM(this, binding);
    }

}
