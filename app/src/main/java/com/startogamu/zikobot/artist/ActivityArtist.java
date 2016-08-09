package com.startogamu.zikobot.artist;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ActivityArtistBinding;
import com.startogamu.zikobot.module.zikobot.model.Artist;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        return new ActivityArtistVM(this, binding);
    }


}
