package com.startogamu.musicalarm.view.fragment;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentSpotifyMusicBinding;
import com.startogamu.musicalarm.viewmodel.fragment.SpotifyMusicViewModel;

/**
 * Created by josh on 26/03/16.
 */
public class SpotifyMusicFragment extends FragmentBase<FragmentSpotifyMusicBinding, SpotifyMusicViewModel> {

    public static final java.lang.String TAG = SpotifyMusicFragment.class.getSimpleName();

    public static SpotifyMusicFragment newInstance() {
        SpotifyMusicFragment fragment = new SpotifyMusicFragment();
        return fragment;
    }

    @Override
    public int data() {
        return BR.spotifyMusicViewModel;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_spotify_music;
    }

    @Override
    public SpotifyMusicViewModel baseFragmentVM(FragmentSpotifyMusicBinding binding) {
        return new SpotifyMusicViewModel(this, binding);
    }
}
