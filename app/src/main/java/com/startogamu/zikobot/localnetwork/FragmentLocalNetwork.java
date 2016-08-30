package com.startogamu.zikobot.localnetwork;

import android.os.Bundle;

import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.BR;

import com.startogamu.zikobot.databinding.FragmentLocalNetworkBinding;

/**
 * Created by josh on 19/08/16.
 */
public class FragmentLocalNetwork extends FragmentBase<FragmentLocalNetworkBinding, FragmentLocalNetworkVM>{


    public static FragmentLocalNetwork newInstance() {
        Bundle args = new Bundle();
        FragmentLocalNetwork fragment = new FragmentLocalNetwork();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int data() {
        return BR.fragmentLocalNetworkVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_local_network;
    }

    @Override
    public FragmentLocalNetworkVM baseFragmentVM(FragmentLocalNetworkBinding binding) {
        return new FragmentLocalNetworkVM(this,binding);
    }

}
