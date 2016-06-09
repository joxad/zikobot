package com.startogamu.zikobot.core.fragmentmanager;

import android.content.Context;
import android.support.design.widget.TabLayout;

/**
 * Created by josh on 09/06/16.
 */
public class TabLayoutManager {

    /***
     * Will contains the filters (artist/album/tracks)
     */
    public static void initTabLayoutAlarms(Context context, TabLayout tabLayout, SimpleTabSelectedListener simpleTabSelectedListener) {
        tabLayout.removeAllTabs();

        tabLayout.addTab(tabLayout.newTab().setText("Réveil"));
        tabLayout.addTab(tabLayout.newTab().setText("Veilleuse"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                simpleTabSelectedListener.onSelected(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public static void initTabLayoutLocalTracks(Context context, TabLayout tabLayout, SimpleTabSelectedListener simpleTabSelectedListener) {
        tabLayout.removeAllTabs();

        tabLayout.addTab(tabLayout.newTab().setText("Par Artiste"));
        tabLayout.addTab(tabLayout.newTab().setText("Par Album"));
        tabLayout.addTab(tabLayout.newTab().setText("Par Track"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                simpleTabSelectedListener.onSelected(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /***
     * Will contains the filters (artist/album/tracks)
     */
    public static void initTabLayoutSpotifyTracks(Context context, TabLayout tabLayout, SimpleTabSelectedListener simpleTabSelectedListener) {
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("Par playlist"));
        tabLayout.addTab(tabLayout.newTab().setText("Par Album"));
        tabLayout.addTab(tabLayout.newTab().setText("Par Track"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                simpleTabSelectedListener.onSelected(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public interface SimpleTabSelectedListener {
        void onSelected(TabLayout.Tab tab);
    }
}
