package com.startogamu.zikobot.core.fragmentmanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.startogamu.zikobot.R;

/**
 * Created by josh on 08/06/16.
 */
public class FragmentManager {

    public static void addFragment(FragmentActivity activity, Fragment fragment, boolean withBackstack) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, fragment);
        if (withBackstack) transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public static void replaceFragment(FragmentActivity activity, Fragment fragment, boolean withBackstack) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (withBackstack) transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }
}
