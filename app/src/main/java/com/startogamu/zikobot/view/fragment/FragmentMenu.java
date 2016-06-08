package com.startogamu.zikobot.view.fragment;

import android.os.Bundle;

import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.FragmentMenuBinding;
import com.startogamu.zikobot.viewmodel.fragment.FragmentMenuVM;

/**
 * Created by josh on 08/06/16.
 */
public class FragmentMenu extends FragmentBase<FragmentMenuBinding, FragmentMenuVM> {
    public static FragmentMenu newInstance() {

        Bundle args = new Bundle();

        FragmentMenu fragment = new FragmentMenu();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int data() {
        return BR.fragmentMenuVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_menu;
    }

    @Override
    public FragmentMenuVM baseFragmentVM(FragmentMenuBinding binding) {
        return new FragmentMenuVM(this, binding);
    }
}
