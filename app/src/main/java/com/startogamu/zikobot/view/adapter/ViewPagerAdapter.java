package com.startogamu.zikobot.view.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Activity activity;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();

    public void addFragment(String title, Fragment fragment) {
        if (titles.contains(title)) return;
        titles.add(title);
        fragments.add(fragment);
    }

    public ViewPagerAdapter(Activity activity, android.support.v4.app.FragmentManager fragmentManager) {
        super(fragmentManager);
        this.activity = activity;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return fragments.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}