package com.startogamu.zikobot.view.fragment;

import android.support.v4.app.Fragment;

import com.startogamu.zikobot.view.activity.BaseActivity;

/**
 * Created by josh on 01/04/16.
 */
public class BaseFragment extends Fragment {

    public void addFragment(Fragment fragment, boolean withBackstack) {
        ((BaseActivity) getActivity()).addFragment(fragment, withBackstack);
    }


    public void replaceFragment(Fragment fragment, boolean withBackstack) {
        ((BaseActivity) getActivity()).replaceFragment(fragment, withBackstack);
    }
}
