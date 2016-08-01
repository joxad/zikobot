package com.startogamu.zikobot.viewmodel.fragment.search;

import android.app.Fragment;

import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.core.event.navigation_manager.EventTabBars;
import com.startogamu.zikobot.databinding.FragmentSearchBinding;
import com.startogamu.zikobot.view.fragment.search.FragmentSearch;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by josh on 01/08/16.
 */
public class FragmentSearchVM extends FragmentBaseVM<FragmentSearch,FragmentSearchBinding> {
    /***
     * @param fragment
     * @param binding
     */
    public FragmentSearchVM(FragmentSearch fragment, FragmentSearchBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void init() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post(new EventTabBars(false, FragmentSearch.class.getSimpleName()));
    }

    @Override
    public void destroy() {

    }
}
