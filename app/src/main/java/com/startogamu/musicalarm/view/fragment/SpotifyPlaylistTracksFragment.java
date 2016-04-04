package com.startogamu.musicalarm.view.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.f2prateek.dart.henson.Bundler;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentSpotifyPlaylistTracksBinding;
import com.startogamu.musicalarm.model.spotify.SpotifyTrack;
import com.startogamu.musicalarm.viewmodel.fragment.SpotifyPlaylistTracksViewModel;

/**
 * Created by josh on 31/03/16.
 */
public class SpotifyPlaylistTracksFragment extends BaseFragment {

    public final static String TAG = SpotifyPlaylistTracksFragment.class.getSimpleName();

    FragmentSpotifyPlaylistTracksBinding binding;
    private SpotifyPlaylistTracksViewModel spotifyPlaylistTracksViewModel;

    public static SpotifyPlaylistTracksFragment newInstance(Bundle bundle) {
        SpotifyPlaylistTracksFragment fragment = new SpotifyPlaylistTracksFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_playlist_tracks, container, false);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(getActivity()));

        spotifyPlaylistTracksViewModel = new SpotifyPlaylistTracksViewModel(this, binding);
        binding.setSpotifyPlaylistTracksViewModel(spotifyPlaylistTracksViewModel);

        return binding.getRoot();
    }


    public void selectTrack(SpotifyTrack track) {
        spotifyPlaylistTracksViewModel.selectTrack(track);
    }
}
