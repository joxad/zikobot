package com.startogamu.musicalarm.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.databinding.FragmentLocalMusicBinding;
import com.startogamu.musicalarm.module.alarm.object.AlarmTrack;
import com.startogamu.musicalarm.module.alarm.object.LocalTrack;
import com.startogamu.musicalarm.viewmodel.fragment.LocalMusicViewModel;

import org.parceler.Parcels;

/**
 * Created by josh on 26/03/16.
 */
public class LocalMusicFragment extends Fragment {

    public static final String TAG = LocalMusicFragment.class.getSimpleName();
    LocalMusicViewModel localMusicViewModel;

    FragmentLocalMusicBinding binding;


    public static LocalMusicFragment newInstance() {
        LocalMusicFragment fragment = new LocalMusicFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_local_music, container, false);
        localMusicViewModel = new LocalMusicViewModel(this, binding);
        binding.setLocalMusicViewModel(localMusicViewModel);
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();
    }



    public void loadMusic() {
        localMusicViewModel.loadLocalMusic();
    }


}
