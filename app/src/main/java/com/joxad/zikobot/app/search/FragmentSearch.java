package com.joxad.zikobot.app.search;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.joxad.easydatabinding.fragment.v4.FragmentBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.utils.ISearch;
import com.joxad.zikobot.app.databinding.FragmentSearchBinding;

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
    public FragmentSearchVM baseFragmentVM(FragmentSearchBinding binding, @Nullable Bundle savedInstance) {
        return new FragmentSearchVM(this,binding,savedInstance);
    }


    @Override
    public void query(String string) {
        if (vm != null)
            vm.query(string);
    }
}
