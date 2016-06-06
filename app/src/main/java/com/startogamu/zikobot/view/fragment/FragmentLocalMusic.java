package com.startogamu.zikobot.view.fragment;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.FragmentLocalMusicBinding;
import com.startogamu.zikobot.viewmodel.fragment.FragmentLocalVM;

/**
 * Created by josh on 26/03/16.
 */
public class FragmentLocalMusic extends FragmentBase<FragmentLocalMusicBinding, FragmentLocalVM> {

    public static final String TAG = FragmentLocalMusic.class.getSimpleName();

    public static FragmentLocalMusic newInstance() {
        FragmentLocalMusic fragment = new FragmentLocalMusic();
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
