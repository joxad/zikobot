package com.startogamu.zikobot.view.fragment.deezer;

import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.FragmentDeezerPlaylistsBinding;
import com.startogamu.zikobot.viewmodel.fragment.deezer.FragmentDeezerPlaylistsVM;

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
