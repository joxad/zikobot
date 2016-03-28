package com.startogamu.musicalarm.view.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentSpotifyMusicBinding;
import com.startogamu.musicalarm.di.manager.spotify_api.SpotifyAPIManager;
import com.startogamu.musicalarm.model.spotify.Item;
import com.startogamu.musicalarm.model.spotify.SpotifyPlaylist;
import com.startogamu.musicalarm.viewmodel.ItemAlarmViewModel;
import com.startogamu.musicalarm.viewmodel.fragment.SpotifyMusicViewModel;
import com.startogamu.musicalarm.viewmodel.items.ItemPlaylistViewModel;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import javax.inject.Inject;

import rx.Subscriber;

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
        binding.rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        spotifyMusicViewModel = new SpotifyMusicViewModel(this, binding);
        binding.setSpotifyMusicViewModel(spotifyMusicViewModel);
        return binding.getRoot();
    }


}
