package com.joxad.zikobot.app.soundcloud;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.v4.FragmentBase;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.FragmentSoundCloudPlaylistsBinding;

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
    public FragmentSoundCloudPlaylistsVM baseFragmentVM(FragmentSoundCloudPlaylistsBinding binding,@Nullable Bundle saved) {
        return new FragmentSoundCloudPlaylistsVM(this,binding,saved);
    }
}
