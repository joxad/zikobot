package com.joxad.zikobot.app.deezer;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.Menu;
import android.view.MenuInflater;

import com.android.databinding.library.baseAdapters.BR;
import com.deezer.sdk.model.Playlist;

import com.joxad.easydatabinding.fragment.v4.FragmentBase;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.databinding.FragmentDeezerTracksBinding;

import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 31/03/16.
 */
public class FragmentDeezerTracks extends FragmentBase<FragmentDeezerTracksBinding, FragmentDeezerTracksVM> {

    public final static String TAG = FragmentDeezerTracks.class.getSimpleName();

    public static FragmentDeezerTracks newInstance(Playlist item, int dataVm, @LayoutRes int layout) {
        FragmentDeezerTracks fragment = new FragmentDeezerTracks();
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
        return BR.fragmentDeezerTracksVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_deezer_tracks;
    }

    @Override
    public FragmentDeezerTracksVM baseFragmentVM(FragmentDeezerTracksBinding binding) {
        return new FragmentDeezerTracksVM(this, binding) {
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
