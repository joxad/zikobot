package com.startogamu.musicalarm.view.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joxad.android_easy_spotify.SpotifyManager;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentConnectSpotifyBinding;
import com.startogamu.musicalarm.viewmodel.fragment.SpotifyConnectViewModel;

/**
 * Created by josh on 26/03/16.
 */
public class SpotifyConnectFragment extends Fragment {

    FragmentConnectSpotifyBinding binding;
    SpotifyConnectViewModel spotifyConnectViewModel;
    public static SpotifyConnectFragment newInstance() {
        SpotifyConnectFragment fragment = new SpotifyConnectFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_connect_spotify,container,false);
        spotifyConnectViewModel = new SpotifyConnectViewModel(this,binding);
        binding.setSpotifyConnectViewModel(spotifyConnectViewModel);

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        spotifyConnectViewModel.onActivityResult(requestCode, resultCode, data);
    }

    public void onNewIntent(Intent intent) {
        spotifyConnectViewModel.onNewIntent(intent);
    }

    @Override
    public void onPause() {
        super.onDestroy();
        spotifyConnectViewModel.onDestroy();
    }
}
