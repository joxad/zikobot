package com.startogamu.musicalarm.view.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.startogamu.musicalarm.R;

/**
 * Created by josh on 01/04/16.
 */
public class BaseActivity extends AppCompatActivity {

    public void addFragment(Fragment fragment, boolean withBackstack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.container, fragment);
        if (withBackstack) transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, boolean withBackstack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (withBackstack) transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

}