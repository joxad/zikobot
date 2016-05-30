package com.startogamu.musicalarm.view.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentSpotifyMusicBinding;
import com.startogamu.musicalarm.view.activity.BaseActivity;
import com.startogamu.musicalarm.viewmodel.fragment.SpotifyMusicViewModel;

/**
 * Created by josh on 26/03/16.
 */
public class SpotifyMusicFragment extends BaseFragment {

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
        binding.rvItems.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        spotifyMusicViewModel = new SpotifyMusicViewModel(this, binding);
        binding.setSpotifyMusicViewModel(spotifyMusicViewModel);
        return binding.getRoot();
    }


    @Override
    public void onPause() {
        super.onPause();
        spotifyMusicViewModel.destroy();
    }
}
