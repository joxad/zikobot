package com.joxad.zikobot.app.artist;

import android.os.Bundle;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.ActivityArtistBinding;

/**
 * Created by josh on 08/08/16.
 */
public class ActivityArtist extends ActivityBase<ActivityArtistBinding, ActivityArtistVM> {

    @Override
    public int data() {
        return BR.activityArtistVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_artist;
    }

    @Override
    public ActivityArtistVM baseActivityVM(ActivityArtistBinding binding, Bundle savedInstanceState) {
        return new ActivityArtistVM(this, binding);
    }

}
