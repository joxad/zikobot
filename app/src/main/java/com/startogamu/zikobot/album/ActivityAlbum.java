package com.startogamu.zikobot.album;

import android.os.Bundle;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.ActivityAlbumBinding;

/**
 * Created by josh on 09/08/16.
 */
public class ActivityAlbum extends ActivityBase<ActivityAlbumBinding, ActivityAlbumVM> {
    @Override
    public int data() {
        return BR.activityAlbumVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_album;
    }

    @Override
    public ActivityAlbumVM baseActivityVM(ActivityAlbumBinding binding, Bundle savedInstanceState) {
        return new ActivityAlbumVM(this, binding);
    }

}
