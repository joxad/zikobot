package com.joxad.zikobot.app.deezer;

import com.joxad.easydatabinding.fragment.v4.FragmentBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.FragmentDeezerPlaylistsBinding;

public class FragmentDeezerPlaylists extends FragmentBase<FragmentDeezerPlaylistsBinding, FragmentDeezerPlaylistsVM> {

    public static final String TAG = FragmentDeezerPlaylists.class.getSimpleName();

    public static FragmentDeezerPlaylists newInstance() {
        FragmentDeezerPlaylists fragment = new FragmentDeezerPlaylists();

        return fragment;
    }

    @Override
    public int data() {
        return BR.fragmentDeezerPlaylistsVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_deezer_playlists;
    }

    @Override
    public FragmentDeezerPlaylistsVM baseFragmentVM(FragmentDeezerPlaylistsBinding binding) {
        return new FragmentDeezerPlaylistsVM(this, binding);
    }
}
