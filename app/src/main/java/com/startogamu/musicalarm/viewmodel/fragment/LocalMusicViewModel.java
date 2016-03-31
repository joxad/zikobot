package com.startogamu.musicalarm.viewmodel.fragment;

import android.app.Fragment;
import android.databinding.BaseObservable;

import com.startogamu.musicalarm.databinding.FragmentLocalMusicBinding;
import com.startogamu.musicalarm.viewmodel.ViewModel;

/**
 * Created by josh on 26/03/16.
 */
public class LocalMusicViewModel extends BaseObservable implements ViewModel {

    private Fragment fragment;
    private FragmentLocalMusicBinding binding;

    public LocalMusicViewModel(Fragment fragment, FragmentLocalMusicBinding binding) {

        this.fragment = fragment;
        this.binding = binding;

    }


    @Override
    public void onDestroy() {

    }
}
