package com.startogamu.zikobot.core.fragmentmanager;

import android.support.v4.app.Fragment;

/**
 * Created by josh on 08/06/16.
 */
public interface IFragmentManager {

    public void addFragment(Fragment fragment, boolean withBackstack);

    public void replaceFragment(Fragment fragment, boolean withBackstack);
}
