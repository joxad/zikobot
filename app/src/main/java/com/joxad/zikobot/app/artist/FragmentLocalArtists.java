package com.joxad.zikobot.app.artist;

import android.os.Bundle;

import com.joxad.easydatabinding.fragment.v4.FragmentBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.FragmentLocalArtistsBinding;


/**
 * Created by josh on 06/06/16.
 */
public class FragmentLocalArtists extends FragmentBase<FragmentLocalArtistsBinding, FragmentLocalArtistsVM> {


    public static final String TAG = "FragmentLocalArtists";

    public static FragmentLocalArtists newInstance() {
        Bundle args = new Bundle();
        FragmentLocalArtists fragment = new FragmentLocalArtists();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int data() {
        return BR.fragmentLocalArtistsVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_local_artists;
    }

    @Override
    public FragmentLocalArtistsVM baseFragmentVM(FragmentLocalArtistsBinding binding) {
        return new FragmentLocalArtistsVM(this, binding);
    }

}
