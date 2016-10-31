package com.startogamu.zikobot.view.fragment.local;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.android.databinding.library.baseAdapters.BR;

import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentLocalTracksBinding;
import com.startogamu.zikobot.module.localmusic.model.LocalAlbum;
import com.startogamu.zikobot.viewmodel.fragment.local.FragmentLocalTracksVM;

import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 26/03/16.
 */
public class FragmentLocalTracks extends FragmentBase<FragmentLocalTracksBinding, FragmentLocalTracksVM> {

    public static final String TAG = FragmentLocalTracks.class.getSimpleName();

    public static FragmentLocalTracks newInstance(@Nullable LocalAlbum album, int dataVm, @LayoutRes int layout) {
        FragmentLocalTracks fragment = new FragmentLocalTracks();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA.LOCAL_ALBUM, Parcels.wrap(album));
        bundle.putInt(EXTRA.DATA_VM, dataVm);
        bundle.putInt(EXTRA.LAYOUT, layout);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int data() {
        return BR.fragmentLocalTracksVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_local_tracks;
    }

    @Override
    public FragmentLocalTracksVM baseFragmentVM(FragmentLocalTracksBinding binding) {
        return new FragmentLocalTracksVM(this, binding) {
            @Override
            public ItemView getItemView() {
                return ItemView.of(getArguments().getInt(EXTRA.DATA_VM), getArguments().getInt(EXTRA.LAYOUT));
            }
        };
    }
}
