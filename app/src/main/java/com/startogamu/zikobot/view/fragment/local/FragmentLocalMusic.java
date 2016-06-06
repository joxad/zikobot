package com.startogamu.zikobot.view.fragment.local;

import com.android.databinding.library.baseAdapters.BR;
import com.f2prateek.dart.henson.Bundler;
import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentLocalMusicBinding;
import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.viewmodel.fragment.local.FragmentLocalVM;

import org.parceler.Parcels;

/**
 * Created by josh on 26/03/16.
 */
public class FragmentLocalMusic extends FragmentBase<FragmentLocalMusicBinding, FragmentLocalVM> {

    public static final String TAG = FragmentLocalMusic.class.getSimpleName();

    public static FragmentLocalMusic newInstance(LocalAlbum album) {
        FragmentLocalMusic fragment = new FragmentLocalMusic();
        fragment.setArguments(Bundler.create().put(EXTRA.LOCAL_ALBUM, Parcels.wrap(album)).get());
        return fragment;
    }


    @Override
    public int data() {
        return BR.fragmentLocalVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_local_music;
    }

    @Override
    public FragmentLocalVM baseFragmentVM(FragmentLocalMusicBinding binding) {
        return new FragmentLocalVM(this, binding);
    }

    public void loadMusic() {
        vm.loadLocalMusic();
    }

    public void permissionDenied() {
        vm.permissionDenied();
    }
}
