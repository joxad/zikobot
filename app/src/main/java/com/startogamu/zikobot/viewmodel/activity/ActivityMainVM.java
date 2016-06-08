package com.startogamu.zikobot.viewmodel.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.f2prateek.dart.Dart;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.easydatabinding.activity.INewIntent;
import com.joxad.easydatabinding.activity.IPermission;
import com.joxad.easydatabinding.activity.IResult;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.pixplicity.easyprefs.library.Prefs;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.LocalAlbumSelectEvent;
import com.startogamu.zikobot.core.event.LocalArtistSelectEvent;
import com.startogamu.zikobot.core.event.SelectItemPlaylistEvent;
import com.startogamu.zikobot.core.fragmentmanager.FragmentManager;
import com.startogamu.zikobot.core.fragmentmanager.IFragmentManager;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.core.utils.REQUEST;
import com.startogamu.zikobot.databinding.ActivityMainBinding;
import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.module.content_resolver.model.LocalArtist;
import com.startogamu.zikobot.module.spotify_api.model.Item;
import com.startogamu.zikobot.view.activity.ActivityMain;
import com.startogamu.zikobot.view.fragment.DeezerFragment;
import com.startogamu.zikobot.view.fragment.FragmentSpotifyPlaylistTracks;
import com.startogamu.zikobot.view.fragment.SpotifyConnectFragment;
import com.startogamu.zikobot.view.fragment.SpotifyMusicFragment;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalAlbums;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalArtists;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalMusic;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by josh on 08/06/16.
 */
public class ActivityMainVM extends ActivityBaseVM<ActivityMain, ActivityMainBinding> implements IResult, IPermission, INewIntent, IFragmentManager {

    private SpotifyMusicFragment spotifyMusicFragment;
    private SpotifyConnectFragment spotifyConnectFragment;
    private FragmentLocalArtists fragmentLocalArtists;

    /***
     * @param activity
     * @param binding
     */
    public ActivityMainVM(ActivityMain activity, ActivityMainBinding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {

        createBottomNavigation(binding.bottomNavigation);
        EventBus.getDefault().register(this);
        Dart.inject(this, activity);
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(listener -> activity.onBackPressed());
        fragmentLocalArtists = FragmentLocalArtists.newInstance();
        replaceFragment(fragmentLocalArtists, false);
    }


    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    /***
     * @param bottomNavigationView
     */
    public void createBottomNavigation(BottomNavigationView bottomNavigationView) {

        BottomNavigationItem local = new BottomNavigationItem
                (activity.getString(R.string.activity_music_local), ContextCompat.getColor(activity, R.color.colorPrimary), R.drawable.ic_folder);
        BottomNavigationItem spotify = new BottomNavigationItem
                (activity.getString(R.string.activity_music_spotify), ContextCompat.getColor(activity, android.R.color.holo_green_dark), R.drawable.logo_spotify);
        BottomNavigationItem deezer = new BottomNavigationItem
                (activity.getString(R.string.activity_music_deezer), ContextCompat.getColor(activity, android.R.color.holo_orange_dark), R.drawable.logo_deezer);

        bottomNavigationView.addTab(local);
        bottomNavigationView.addTab(spotify);
        bottomNavigationView.addTab(deezer);

        bottomNavigationView.setOnBottomNavigationItemClickListener(index -> {
            switch (index) {
                case 0:
                    replaceFragment(fragmentLocalArtists, false);
                    break;
                case 1:
                    // if (spotifyManager.hasAccessToken()) {
                    if (!Prefs.contains(AppPrefs.SPOTIFY_ACCESS_CODE)) {
                        spotifyConnectFragment = SpotifyConnectFragment.newInstance();
                        replaceFragment(spotifyConnectFragment, false);
                    } else {
                        loadSpotifyMusicFragment();
                    }
                    break;
                case 2:
                    replaceFragment(DeezerFragment.newInstance(), false);
                    break;
            }
        });

    }

    /***
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (spotifyConnectFragment != null)
            spotifyConnectFragment.onActivityResult(requestCode, resultCode, data);
    }

    /***
     * @param intent
     */
    @Override
    public void onNewIntent(Intent intent) {
        if (spotifyConnectFragment != null && !spotifyConnectFragment.isDetached())
            spotifyConnectFragment.onNewIntent(intent);
    }

    /***
     *
     */
    public void loadSpotifyMusicFragment() {
        spotifyMusicFragment = SpotifyMusicFragment.newInstance();
        replaceFragment(spotifyMusicFragment, false);
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
        replaceFragment(FragmentLocalMusic.newInstance(item), true);
    }

}
