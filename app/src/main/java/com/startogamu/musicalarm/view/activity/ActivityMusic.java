package com.startogamu.musicalarm.view.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivityMusicBinding;
import com.startogamu.musicalarm.BR;
import com.startogamu.musicalarm.module.alarm.model.Alarm;
import com.startogamu.musicalarm.viewmodel.activity.ActivityMusicVM;

/**
 * Created by josh on 26/03/16.
 */
public class ActivityMusic extends ActivityBase<ActivityMusicBinding, ActivityMusicVM> {

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
        return new ActivityMusicVM(this,binding);
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


    public void addFragment(Fragment fragment, boolean withBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, fragment);
        if (withBackstack) transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, boolean withBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (withBackstack) transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

}
