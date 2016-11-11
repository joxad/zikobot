package com.startogamu.zikobot.album;

import android.os.Bundle;
import android.support.annotation.Nullable;


import com.joxad.easydatabinding.fragment.v4.FragmentBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentLocalAlbumsBinding;
import com.startogamu.zikobot.core.module.localmusic.model.LocalArtist;

import org.parceler.Parcels;

/**
 * Created by josh on 06/06/16.
 */
public class FragmentLocalAlbums extends FragmentBase<FragmentLocalAlbumsBinding, FragmentLocalAlbumsVM> {


    public static FragmentLocalAlbums newInstance(@Nullable LocalArtist artist) {
        FragmentLocalAlbums fragment = new FragmentLocalAlbums();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA.LOCAL_ARTIST, Parcels.wrap(artist));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int data() {
        return BR.fragmentLocalAlbumsVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_local_albums;
    }

    @Override
    public FragmentLocalAlbumsVM baseFragmentVM(FragmentLocalAlbumsBinding binding) {
        return new FragmentLocalAlbumsVM(this, binding);
    }

}
