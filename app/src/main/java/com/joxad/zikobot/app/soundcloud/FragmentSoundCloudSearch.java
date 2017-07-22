package com.joxad.zikobot.app.soundcloud;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.joxad.easydatabinding.fragment.v4.FragmentBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.databinding.FragmentSoundCloudSearchBinding;

/**
 * Created by josh on 25/08/16.
 */
public class FragmentSoundCloudSearch extends FragmentBase<FragmentSoundCloudSearchBinding, FragmentSoundCloudSearchVM> {


    public static FragmentSoundCloudSearch newInstance() {
        Bundle args = new Bundle();
        FragmentSoundCloudSearch fragment = new FragmentSoundCloudSearch();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int data() {
        return BR.fragmentSearchVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_sound_cloud_search;
    }

    @Override
    public FragmentSoundCloudSearchVM baseFragmentVM(FragmentSoundCloudSearchBinding binding,@Nullable Bundle savedInstanceState) {
        return new FragmentSoundCloudSearchVM(this,binding,savedInstanceState);
    }
}