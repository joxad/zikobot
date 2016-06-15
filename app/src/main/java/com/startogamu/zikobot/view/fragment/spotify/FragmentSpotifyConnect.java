package com.startogamu.zikobot.view.fragment.spotify;

import android.content.Intent;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.FragmentConnectSpotifyBinding;
import com.startogamu.zikobot.viewmodel.fragment.spotify.FragmentConnectVM;

/**
 * Created by josh on 26/03/16.
 */
public class FragmentSpotifyConnect extends FragmentBase<FragmentConnectSpotifyBinding, FragmentConnectVM> {

    public static FragmentSpotifyConnect newInstance() {
        FragmentSpotifyConnect fragment = new FragmentSpotifyConnect();
        return fragment;
    }

    @Override
    public int data() {
        return BR.fragmentConnectVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_connect_spotify;
    }

    @Override
    public FragmentConnectVM baseFragmentVM(FragmentConnectSpotifyBinding binding) {
        return new FragmentConnectVM(this,binding);
    }

    public void onNewIntent(Intent intent) {
        vm.onNewIntent(intent);
    }

}
