package com.startogamu.zikobot.view.fragment.spotify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.f2prateek.dart.henson.Bundler;
import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentSpotifyPlaylistTracksBinding;
import com.startogamu.zikobot.module.spotify_api.model.Item;
import com.startogamu.zikobot.viewmodel.fragment.spotify.FragmentSpotifyPlaylistVM;

/**
 * Created by josh on 31/03/16.
 */
public class FragmentSpotifyPlaylistTracks extends FragmentBase<FragmentSpotifyPlaylistTracksBinding, FragmentSpotifyPlaylistVM> {

    public final static String TAG = FragmentSpotifyPlaylistTracks.class.getSimpleName();

    public static FragmentSpotifyPlaylistTracks newInstance(Item item) {
        FragmentSpotifyPlaylistTracks fragment = new FragmentSpotifyPlaylistTracks();
        fragment.setArguments( Bundler.create().put(EXTRA.PLAYLIST_ID, item.getId()).put(EXTRA.PLAYLIST_TRACKS_TOTAL, item.getTracks().total).get());
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int data() {
        return BR.fragmentSpotifyPlaylistVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_spotify_playlist_tracks;
    }

    @Override
    public FragmentSpotifyPlaylistVM baseFragmentVM(FragmentSpotifyPlaylistTracksBinding binding) {
        return new FragmentSpotifyPlaylistVM(this, binding);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_music, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



}
