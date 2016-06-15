package com.startogamu.zikobot.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.fragmentmanager.FragmentManager;
import com.startogamu.zikobot.core.fragmentmanager.IFragmentManager;
import com.startogamu.zikobot.databinding.ActivityMusicBinding;
import com.startogamu.zikobot.module.alarm.model.Alarm;
import com.startogamu.zikobot.viewmodel.activity.ActivityMusicVM;

/**
 * Created by josh on 26/03/16.
 */
public class ActivityMusic extends ActivityBase<ActivityMusicBinding, ActivityMusicVM> implements IFragmentManager {

    @InjectExtra
    Alarm alarm;

    @Override
    public int data() {
        return BR.activityMusicVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_music;
    }

    @Override
    public ActivityMusicVM baseActivityVM(ActivityMusicBinding binding, Bundle savedInstanceState) {
        return new ActivityMusicVM(this, binding);
    }


    public void loadSpotifyMusicFragment() {
        vm.loadSpotifyMusicFragment();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    public void addFragment(Fragment fragment, boolean withBackstack) {
        FragmentManager.addFragment(this, fragment, withBackstack);
    }

    @Override
    public void replaceFragment(Fragment fragment, boolean clearBackStack,boolean withBackstack) {
        FragmentManager.replaceFragment(this, fragment, clearBackStack, withBackstack);
    }
}
