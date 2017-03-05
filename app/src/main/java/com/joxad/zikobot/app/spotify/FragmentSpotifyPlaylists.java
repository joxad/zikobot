package com.joxad.zikobot.app.spotify;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.v4.FragmentBase;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.FragmentSpotifyPlaylistsBinding;

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
