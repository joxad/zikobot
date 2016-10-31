package com.startogamu.zikobot.view.fragment.soundcloud;

import com.android.databinding.library.baseAdapters.BR;

import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentSoundCloudPlaylistsBinding;
import com.startogamu.zikobot.viewmodel.fragment.soundcloud.FragmentSoundCloudPlaylistsVM;

/**
 * Created by josh on 26/03/16.
 */
public class FragmentSoundCloudPlaylists extends FragmentBase<FragmentSoundCloudPlaylistsBinding, FragmentSoundCloudPlaylistsVM> {

    public static final String TAG = FragmentSoundCloudPlaylists.class.getSimpleName();

    public static FragmentSoundCloudPlaylists newInstance() {
        FragmentSoundCloudPlaylists fragment = new FragmentSoundCloudPlaylists();

        return fragment;
    }

    @Override
    public int data() {
        return BR.fragmentSoundCloudPlaylistsVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_sound_cloud_playlists;
    }

    @Override
    public FragmentSoundCloudPlaylistsVM baseFragmentVM(FragmentSoundCloudPlaylistsBinding binding) {
        return new FragmentSoundCloudPlaylistsVM(this, binding);
    }
}
