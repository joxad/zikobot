package com.joxad.zikobot.app.album;

import android.os.Bundle;
import android.view.Menu;

import com.joxad.easydatabinding.activity.ActivityBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.ActivityAlbumBinding;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return true;
    }


}
