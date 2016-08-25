package com.startogamu.zikobot.soundcloud;

import android.os.Bundle;

import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.FragmentSoundCloudSearchBinding;
import com.startogamu.zikobot.databinding.FragmentSpotifySearchBinding;
import com.startogamu.zikobot.spotify.FragmentSpotifySearchVM;

/**
 * Created by josh on 25/08/16.
 */
public class FragmentSoundCloudSearch extends FragmentBase<FragmentSoundCloudSearchBinding, FragmentSoundCloudSearchVM> {


    public static FragmentSoundCloudSearch newInstance() {
        Bundle args = new Bundle();
        FragmentSoundCloudSearch fragment = new FragmentSoundCloudSearch();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int data() {
        return BR.fragmentSearchVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_sound_cloud_search;
    }

    @Override
    public FragmentSoundCloudSearchVM baseFragmentVM(FragmentSoundCloudSearchBinding binding) {
        return new FragmentSoundCloudSearchVM(this, binding);
    }
}