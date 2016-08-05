package com.startogamu.zikobot.view.fragment.search;

import android.os.Bundle;

import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.ISearch;
import com.startogamu.zikobot.databinding.FragmentSearchBinding;
import com.startogamu.zikobot.viewmodel.fragment.search.FragmentSearchVM;

/**
 * Created by josh on 01/08/16.
 */
public class FragmentSearch extends FragmentBase<FragmentSearchBinding, FragmentSearchVM> implements ISearch {

    public static FragmentSearch newInstance() {
        Bundle args = new Bundle();
        FragmentSearch fragment = new FragmentSearch();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int data() {
        return BR.fragmentSearchVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_search;
    }

    @Override
    public FragmentSearchVM baseFragmentVM(FragmentSearchBinding binding) {
        return new FragmentSearchVM(this, binding);
    }

    @Override
    public void query(String string) {
        if (vm != null)
            vm.query(string);
    }
}
