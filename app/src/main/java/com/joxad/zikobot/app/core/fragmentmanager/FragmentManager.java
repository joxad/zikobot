package com.joxad.zikobot.app.core.fragmentmanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.joxad.zikobot.app.R;

/**
 * Created by josh on 08/06/16.
 */
public class FragmentManager {

    private static Fragment current;

    public static void addFragment(FragmentActivity activity, Fragment fragment, boolean withBackstack) {
        current = fragment;
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
        current = fragment;
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
            transaction.replace(R.id.container, fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    public static Fragment currentFragment() {
        return current;
    }

    public static void pop(AppCompatActivity activity) {
        android.support.v4.app.FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        supportFragmentManager.popBackStack();
    }

    public static Fragment fragmentByName(String fragmentName) throws IllegalAccessException, InstantiationException {
        Class<?> className = null;
        try {
            className  = Class.forName(fragmentName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (Fragment) className.newInstance();
    }

}
