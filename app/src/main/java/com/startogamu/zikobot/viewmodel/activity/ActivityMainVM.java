package com.startogamu.zikobot.viewmodel.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.easydatabinding.activity.INewIntent;
import com.joxad.easydatabinding.activity.IPermission;
import com.joxad.easydatabinding.activity.IResult;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.LocalAlbumSelectEvent;
import com.startogamu.zikobot.core.event.LocalArtistSelectEvent;
import com.startogamu.zikobot.core.event.SelectItemPlaylistEvent;
import com.startogamu.zikobot.core.fragmentmanager.FragmentManager;
import com.startogamu.zikobot.core.fragmentmanager.IFragmentManager;
import com.startogamu.zikobot.core.utils.REQUEST;
import com.startogamu.zikobot.databinding.ActivityMainBinding;
import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.module.content_resolver.model.LocalArtist;
import com.startogamu.zikobot.module.spotify_api.model.Item;
import com.startogamu.zikobot.view.activity.ActivityMain;
import com.startogamu.zikobot.view.fragment.menu.FragmentMenu;
import com.startogamu.zikobot.view.fragment.spotify.FragmentSpotifyConnect;
import com.startogamu.zikobot.view.fragment.spotify.FragmentSpotifyPlaylistTracks;
import com.startogamu.zikobot.view.fragment.spotify.FragmentSpotifyTracks;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalAlbums;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalArtists;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalMusic;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by josh on 08/06/16.
 */
public class ActivityMainVM extends ActivityBaseVM<ActivityMain, ActivityMainBinding> implements IResult, IPermission, INewIntent, IFragmentManager {

    private FragmentSpotifyTracks fragmentSpotifyTracks;
    private FragmentSpotifyConnect fragmentSpotifyConnect;
    private FragmentLocalArtists fragmentLocalArtists;
    private FragmentLocalAlbums fragmentLocalAlbums;
    private FragmentLocalMusic fragmentLocalMusic;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    /***
     * @param activity
     * @param binding
     */
    public ActivityMainVM(ActivityMain activity, ActivityMainBinding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        initDrawer();
        initFragments();
        initTabLayout();

    }

    /***
     * Will contains the filters (artist/album/tracks)
     */
    private void initTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Par Artiste"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Par Album"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Par Track"));

        binding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        replaceFragment(fragmentLocalArtists, false);
                        break;
                    case 1:
                        /*if (!Prefs.contains(AppPrefs.SPOTIFY_ACCESS_CODE)) {
                            fragmentSpotifyConnect = FragmentSpotifyConnect.newInstance();
                            replaceFragment(fragmentSpotifyConnect, false);
                        } else {
                            loadSpotifyMusicFragment();
                        }*/
                        if (fragmentLocalAlbums == null){
                            fragmentLocalAlbums  = FragmentLocalAlbums.newInstance(null);
                        }
                        replaceFragment(fragmentLocalAlbums, false);
                        break;
                    case 2:
                        if (fragmentLocalMusic == null){
                            fragmentLocalMusic  = FragmentLocalMusic.newInstance(null, BR.trackVM, R.layout.item_track);
                        }
                        replaceFragment(fragmentLocalMusic, false);
                        break;
                }
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
     *
     */
    private void initFragments() {
        fragmentLocalArtists = FragmentLocalArtists.newInstance();
        replaceFragment(fragmentLocalArtists, false);
    }

    /***
     * Init the menu drawer layout that will contains the other way to play music
     */
    private void initDrawer() {
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_menu, FragmentMenu.newInstance()).commit();
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(activity, binding.drawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        binding.drawerLayout.setScrimColor(Color.TRANSPARENT);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.setToolbarNavigationClickListener(v -> {
            if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                binding.drawerLayout.openDrawer(Gravity.LEFT);
            } else {
                binding.drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);

    }


    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }


    /***
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fragmentSpotifyConnect != null)
            fragmentSpotifyConnect.onActivityResult(requestCode, resultCode, data);
    }

    /***
     * @param intent
     */
    @Override
    public void onNewIntent(Intent intent) {
        if (fragmentSpotifyConnect != null && !fragmentSpotifyConnect.isDetached())
            fragmentSpotifyConnect.onNewIntent(intent);
    }

    /***
     *
     */
    public void loadSpotifyMusicFragment() {
        fragmentSpotifyTracks = FragmentSpotifyTracks.newInstance();
        replaceFragment(fragmentSpotifyTracks, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (fragmentLocalArtists == null || fragmentLocalArtists.isDetached())
            return;
        switch (requestCode) {
            case REQUEST.PERMISSION_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    fragmentLocalArtists.loadMusic();
                else {
                    fragmentLocalArtists.permissionDenied();
                }
        }
    }


    @Override
    public void addFragment(Fragment fragment, boolean withBackstack) {
        FragmentManager.addFragment(activity, fragment, withBackstack);
    }

    @Override
    public void replaceFragment(Fragment fragment, boolean withBackstack) {
        FragmentManager.replaceFragment(activity, fragment, withBackstack);
    }


    /***
     * EVENTS
     */

    @Subscribe
    public void onEvent(SelectItemPlaylistEvent selectItemPlaylistEvent) {
        Item item = selectItemPlaylistEvent.getItem();
        replaceFragment(FragmentSpotifyPlaylistTracks.newInstance(item), true);
    }

    @Subscribe
    public void onEvent(LocalArtistSelectEvent localArtistSelectEvent) {
        LocalArtist item = localArtistSelectEvent.getLocalArtist();
        replaceFragment(FragmentLocalAlbums.newInstance(item), true);
    }

    @Subscribe
    public void onEvent(LocalAlbumSelectEvent localAlbumSelectEvent) {
        LocalAlbum item = localAlbumSelectEvent.getModel();
        replaceFragment(FragmentLocalMusic.newInstance(item,BR.trackVM, R.layout.item_track), true);
    }


    public void onPostCreate() {
        actionBarDrawerToggle.syncState();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }
}
