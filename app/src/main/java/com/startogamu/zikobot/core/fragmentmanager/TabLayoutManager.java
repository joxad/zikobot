package com.startogamu.zikobot.core.fragmentmanager;

import android.content.Context;
import android.support.design.widget.TabLayout;

import com.startogamu.zikobot.R;

/**
 * Created by josh on 09/06/16.
 */
public class TabLayoutManager {


    public static final int TAB_PLAYLIST = 0;
    public static final int TAB_ALBUM= 1;
    public static final int TAB_ARTIST = 2;
    public static final int TAB_TRACK = 3;


    /***
     * Will contains the filters (artist/album/tracks)
     */
    public static void initTabLayoutAlarms(Context context, TabLayout tabLayout, SimpleTabSelectedListener simpleTabSelectedListener) {
        tabLayout.removeAllTabs();

        tabLayout.addTab(tabLayout.newTab().setText(context.getString(R.string.drawer_alarms)));


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

    public static void initTabLayoutTracks(Context context, NavigationManager.Account account, TabLayout tabLayout, SimpleTabSelectedListener simpleTabSelectedListener) {

        tabLayout.removeAllTabs();
        switch (account){
            case local:
                tabLayout.addTab(tabLayout.newTab().setText(context.getString(R.string.drawer_filter_album)));
                tabLayout.addTab(tabLayout.newTab().setText(context.getString(R.string.drawer_filter_artiste)));
                tabLayout.addTab(tabLayout.newTab().setText(context.getString(R.string.drawer_filter_tracks)));
                break;
            case spotify:
            case soundcloud:
            case deezer:
                tabLayout.addTab(tabLayout.newTab().setText(context.getString(R.string.drawer_filter_playlist)));
                break;
        }




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
