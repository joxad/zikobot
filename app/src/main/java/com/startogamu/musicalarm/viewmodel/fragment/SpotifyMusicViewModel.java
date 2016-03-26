package com.startogamu.musicalarm.viewmodel.fragment;

import android.databinding.BaseObservable;
import android.support.v4.app.Fragment;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.startogamu.musicalarm.databinding.FragmentSpotifyMusicBinding;
import com.startogamu.musicalarm.viewmodel.ViewModel;

/**
 * Created by josh on 26/03/16.
 */
public class SpotifyMusicViewModel extends BaseObservable implements ViewModel {

    private Fragment fragment;
    private FragmentSpotifyMusicBinding binding;

    public SpotifyMusicViewModel(Fragment fragment, FragmentSpotifyMusicBinding binding) {

        this.fragment = fragment;
        this.binding = binding;

    }


    @Override
    public void onDestroy() {

    }
}
