package com.startogamu.musicalarm.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentSpotifyMusicBinding;
import com.startogamu.musicalarm.viewmodel.fragment.SpotifyMusicViewModel;

/**
 * Created by josh on 26/03/16.
 */
public class SpotifyMusicFragment extends Fragment {

    public static final java.lang.String TAG = SpotifyMusicFragment.class.getSimpleName();
    SpotifyMusicViewModel spotifyMusicViewModel;

    FragmentSpotifyMusicBinding binding;

    public static SpotifyMusicFragment newInstance() {
        SpotifyMusicFragment fragment = new SpotifyMusicFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_music, container, false);
        spotifyMusicViewModel = new SpotifyMusicViewModel(this, binding);
        return binding.getRoot();
    }
}
