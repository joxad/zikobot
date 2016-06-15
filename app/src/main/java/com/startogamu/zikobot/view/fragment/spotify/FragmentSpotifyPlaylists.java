package com.startogamu.zikobot.view.fragment.spotify;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.FragmentSpotifyPlaylistsBinding;
import com.startogamu.zikobot.viewmodel.fragment.spotify.FragmentSpotifyPlaylistsVM;

/**
 * Created by josh on 26/03/16.
 */
public class FragmentSpotifyPlaylists extends FragmentBase<FragmentSpotifyPlaylistsBinding, FragmentSpotifyPlaylistsVM> {

    public static final java.lang.String TAG = FragmentSpotifyPlaylists.class.getSimpleName();

    public static FragmentSpotifyPlaylists newInstance() {
        FragmentSpotifyPlaylists fragment = new FragmentSpotifyPlaylists();
        return fragment;
    }

    @Override
    public int data() {
        return BR.fragmentSpotifyPlaylistsVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_spotify_playlists;
    }

    @Override
    public FragmentSpotifyPlaylistsVM baseFragmentVM(FragmentSpotifyPlaylistsBinding binding) {
        return new FragmentSpotifyPlaylistsVM(this, binding);
    }
}
