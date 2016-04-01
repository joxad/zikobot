package com.startogamu.musicalarm.view.fragment;

import android.app.Fragment;

import com.startogamu.musicalarm.view.activity.BaseActivity;

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
