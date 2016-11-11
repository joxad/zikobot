package com.startogamu.zikobot.spotify;

import android.os.Bundle;

import com.joxad.easydatabinding.fragment.v4.FragmentBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.FragmentSpotifySearchBinding;

/**
 * Created by josh on 25/08/16.
 */
public class FragmentSpotifySearch extends FragmentBase<FragmentSpotifySearchBinding, FragmentSpotifySearchVM> {


    public static FragmentSpotifySearch newInstance() {
        Bundle args = new Bundle();
        FragmentSpotifySearch fragment = new FragmentSpotifySearch();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int data() {
        return BR.fragmentSearchVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_spotify_search;
    }

    @Override
    public FragmentSpotifySearchVM baseFragmentVM(FragmentSpotifySearchBinding binding) {
        return new FragmentSpotifySearchVM(this, binding);
    }
}