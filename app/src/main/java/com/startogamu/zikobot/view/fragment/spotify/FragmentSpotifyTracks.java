package com.startogamu.zikobot.view.fragment.spotify;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.FragmentSpotifyTracksBinding;
import com.startogamu.zikobot.viewmodel.fragment.spotify.FragmentTracksVM;

/**
 * Created by josh on 26/03/16.
 */
public class FragmentSpotifyTracks extends FragmentBase<FragmentSpotifyTracksBinding, FragmentTracksVM> {

    public static final java.lang.String TAG = FragmentSpotifyTracks.class.getSimpleName();

    public static FragmentSpotifyTracks newInstance() {
        FragmentSpotifyTracks fragment = new FragmentSpotifyTracks();
        return fragment;
    }

    @Override
    public int data() {
        return BR.fragmentTracksVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_spotify_tracks;
    }

    @Override
    public FragmentTracksVM baseFragmentVM(FragmentSpotifyTracksBinding binding) {
        return new FragmentTracksVM(this, binding);
    }
}
