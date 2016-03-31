package com.startogamu.musicalarm.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentLocalMusicBinding;
import com.startogamu.musicalarm.databinding.FragmentSpotifyMusicBinding;
import com.startogamu.musicalarm.viewmodel.fragment.LocalMusicViewModel;
import com.startogamu.musicalarm.viewmodel.fragment.SpotifyMusicViewModel;

/**
 * Created by josh on 26/03/16.
 */
public class LocalMusicFragment extends Fragment {

    public static final String TAG = LocalMusicFragment.class.getSimpleName();
    LocalMusicViewModel spotifyMusicViewModel;

    FragmentLocalMusicBinding binding;

    public static LocalMusicFragment newInstance() {
        LocalMusicFragment fragment = new LocalMusicFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_local_music, container, false);
        spotifyMusicViewModel = new LocalMusicViewModel(this, binding);
        return binding.getRoot();
    }
}
