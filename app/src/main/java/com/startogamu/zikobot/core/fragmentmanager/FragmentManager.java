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

    /***
     * @param activity
     * @param fragment
     * @param clearBackStack
     * @param withBackstack
     */
    public static void replaceFragment(FragmentActivity activity, Fragment fragment, boolean clearBackStack, boolean withBackstack) {
        android.support.v4.app.FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        if (clearBackStack) {
            while (supportFragmentManager.getBackStackEntryCount() > 0) {
                supportFragmentManager.popBackStackImmediate();
            }
        }
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (withBackstack) transaction.addToBackStack(fragment.getClass().getSimpleName());
        try {
            transaction.commit();

        } catch (Exception e) {
            transaction = supportFragmentManager.beginTransaction();
            transaction.commitAllowingStateLoss();
        }
    }
}
