package com.startogamu.zikobot.view.fragment.spotify;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
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
import com.startogamu.zikobot.databinding.FragmentSpotifyTracksBinding;
import com.startogamu.zikobot.module.spotify_api.model.Item;
import com.startogamu.zikobot.viewmodel.fragment.spotify.FragmentSpotifyTracksVM;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 31/03/16.
 */
public class FragmentSpotifyTracks extends FragmentBase<FragmentSpotifyTracksBinding, FragmentSpotifyTracksVM> {

    public final static String TAG = FragmentSpotifyTracks.class.getSimpleName();

    public static FragmentSpotifyTracks newInstance(Item item, int dataVm, @LayoutRes int layout) {
        FragmentSpotifyTracks fragment = new FragmentSpotifyTracks();

        Bundler bundler = Bundler.create()
                .put(EXTRA.DATA_VM, dataVm)
                .put(EXTRA.LAYOUT, layout);
        if (item != null) {
            bundler.put(EXTRA.PLAYLIST_ID, item.getId())
                    .put(EXTRA.PLAYLIST_TRACKS_TOTAL, item.getTracks().total);

        }
        fragment.setArguments(bundler.get());
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
        return BR.fragmentSpotifyTracksVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_spotify_tracks;
    }

    @Override
    public FragmentSpotifyTracksVM baseFragmentVM(FragmentSpotifyTracksBinding binding) {
        return new FragmentSpotifyTracksVM(this, binding) {
            @Override
            public ItemView getItemView() {
                return ItemView.of(fragment.getArguments().getInt(EXTRA.DATA_VM), getArguments().getInt(EXTRA.LAYOUT));
            }
        };
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_music, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


}
