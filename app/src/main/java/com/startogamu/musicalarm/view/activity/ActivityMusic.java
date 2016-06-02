package com.startogamu.musicalarm.view.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.f2prateek.dart.HensonNavigable;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBase;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivityMusicBinding;
import com.startogamu.musicalarm.BR;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.viewmodel.ActivityMusicViewModel;

/**
 * Created by josh on 26/03/16.
 */
public class ActivityMusic extends ActivityBase<ActivityMusicBinding, ActivityMusicViewModel> {

    @InjectExtra
    Alarm alarm;

    @Override
    public int data() {
        return BR.activityMusicViewModel;
    }

    @Override
    public int layoutResources() {
        return R.layout.activity_music;
    }

    @Override
    public ActivityMusicViewModel baseActivityVM(ActivityMusicBinding binding, Bundle savedInstanceState) {
        return new ActivityMusicViewModel(this,binding);
    }



    public void loadSpotifyMusicFragment() {
        vm.loadSpotifyMusicFragment();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }


    public void addFragment(Fragment fragment, boolean withBackstack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.container, fragment);
        if (withBackstack) transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, boolean withBackstack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (withBackstack) transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

}
