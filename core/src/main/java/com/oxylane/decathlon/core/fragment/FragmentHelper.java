package com.oxylane.decathlon.core.fragment;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.joxad.androidtemplate.core.R;

/**
 * Created by josh on 24/08/16.
 */
public class FragmentHelper {


    /***
     * @param activity
     * @param fragment
     * @param withBackstack
     */
    public static void addFragment(FragmentActivity activity, Fragment fragment, @IdRes int id, boolean withBackstack) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(id, fragment);
        if (withBackstack) transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    /***
     * @param activity
     * @param fragment
     * @param clearBackStack
     * @param withBackstack
     */
    public static void replaceFragment(FragmentActivity activity, Fragment fragment, @IdRes int id,
                                       boolean clearBackStack, boolean withBackstack) {
        android.support.v4.app.FragmentManager supportFragmentManager = activity.getSupportFragmentManager();

        // Clear the back stack if needed
        if (clearBackStack) {
            while (supportFragmentManager.getBackStackEntryCount() > 0) {
                supportFragmentManager.popBackStackImmediate();
            }
        }

        // Set up the transaction
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
       /* transaction.setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left,
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right
        );*/
        transaction.replace(id, fragment);

        // Add the new fragment to back stack if needed
        if (withBackstack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }

        // Commit transaction
        try {
            transaction.commit();
        } catch (Exception e) {
            transaction = supportFragmentManager.beginTransaction();
            transaction.replace(id, fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     *
     * @param activity
     */
    public static void popBackStack(FragmentActivity activity) {
        android.support.v4.app.FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        supportFragmentManager.popBackStack();
    }

    /***
     *
     * @param fragmentName
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Fragment fragmentByName(String fragmentName) throws IllegalAccessException, InstantiationException {
        Class<?> className = null;
        try {
            className = Class.forName(fragmentName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (Fragment) className.newInstance();
    }
}
