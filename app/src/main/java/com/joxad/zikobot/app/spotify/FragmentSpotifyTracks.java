package com.joxad.zikobot.app.spotify;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.Menu;
import android.view.MenuInflater;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.v4.FragmentBase;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.databinding.FragmentSpotifyTracksBinding;
import com.joxad.zikobot.data.module.spotify_api.model.Item;

import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 31/03/16.
 */
public class FragmentSpotifyTracks extends FragmentBase<FragmentSpotifyTracksBinding, FragmentSpotifyTracksVM> {

    public final static String TAG = FragmentSpotifyTracks.class.getSimpleName();

    public static FragmentSpotifyTracks newInstance(Item item, int dataVm, @LayoutRes int layout) {
        FragmentSpotifyTracks fragment = new FragmentSpotifyTracks();

        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA.DATA_VM, dataVm);
        bundle.putInt(EXTRA.LAYOUT, layout);
        if (item != null) {
            bundle.putParcelable(EXTRA.PLAYLIST, Parcels.wrap(item));
        }
        fragment.setArguments(bundle);
        return fragment;
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
