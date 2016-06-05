package com.startogamu.zikobot.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.startogamu.zikobot.R;

/**
 * Created by josh on 01/04/16.
 */
public class BaseActivity extends AppCompatActivity {

    public void addFragment(Fragment fragment, boolean withBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, fragment);
        if (withBackstack) transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, boolean withBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (withBackstack) transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

}
