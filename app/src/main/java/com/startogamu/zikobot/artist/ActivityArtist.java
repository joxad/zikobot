package com.startogamu.zikobot.artist;

import android.os.Bundle;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ActivityArtistBinding;

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
