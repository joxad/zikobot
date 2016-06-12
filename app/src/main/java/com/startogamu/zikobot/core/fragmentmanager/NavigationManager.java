package com.startogamu.zikobot.core.fragmentmanager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.bumptech.glide.Glide;
import com.joxad.easydatabinding.activity.INewIntent;
import com.joxad.easydatabinding.activity.IPermission;
import com.joxad.easydatabinding.activity.IResult;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.LocalAlbumSelectEvent;
import com.startogamu.zikobot.core.event.LocalArtistSelectEvent;
import com.startogamu.zikobot.core.event.SelectItemPlaylistEvent;
import com.startogamu.zikobot.core.event.drawer.EventMenuDrawerAlarms;
import com.startogamu.zikobot.core.event.drawer.EventMenuDrawerLocal;
import com.startogamu.zikobot.core.event.navigation_manager.EventCollapseToolbar;
import com.startogamu.zikobot.core.event.navigation_manager.EventTabBars;
import com.startogamu.zikobot.core.event.permission.EventPermission;
import com.startogamu.zikobot.core.utils.REQUEST;
import com.startogamu.zikobot.databinding.ActivityMainBinding;
import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.module.content_resolver.model.LocalArtist;
import com.startogamu.zikobot.module.spotify_api.model.Item;
import com.startogamu.zikobot.view.activity.ActivityMain;
import com.startogamu.zikobot.view.fragment.alarm.FragmentAlarms;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalAlbums;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalArtists;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalPlaylists;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalTracks;
import com.startogamu.zikobot.view.fragment.permission.FragmentPermission;
import com.startogamu.zikobot.view.fragment.spotify.FragmentSpotifyAlbums;
import com.startogamu.zikobot.view.fragment.spotify.FragmentSpotifyArtists;
import com.startogamu.zikobot.view.fragment.spotify.FragmentSpotifyPlaylists;
import com.startogamu.zikobot.view.fragment.spotify.FragmentSpotifyTracks;
import com.startogamu.zikobot.viewmodel.activity.ActivityMainVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import lombok.Data;

/**
 * Created by josh on 10/06/16.
 */
@Data
public class NavigationManager implements IFragmentManager, IResult, INewIntent, IPermission {


    public enum Account {local, spotify, deezer}

    private Account current = Account.local;

    private final ActivityMainVM activityMainVM;
    private final ActivityMain activity;
    private final ActivityMainBinding binding;
    private final android.support.v4.app.FragmentManager supportFragmentManager;

    public void init() {
        initTabLayoutTracks();
        replaceFragment(FragmentLocalPlaylists.newInstance(), true, false);
    }

    public void showLocals() {
        current = Account.local;
        initTabLayoutTracks();
        replaceFragment(FragmentLocalPlaylists.newInstance(), true, false);
    }


    public void showSpotifys() {
        current = Account.spotify;
        initTabLayoutTracks();
        replaceFragment(FragmentSpotifyPlaylists.newInstance(), true, false);
    }

    public void showAlarms() {
        initTabLayoutAlarms();
        replaceFragment(FragmentAlarms.newInstance(), true, false);
    }


    public void showAbout() {
        new LibsBuilder()
                .withAboutAppName(activity.getString(R.string.about))
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .start(activity);
    }

    /***
     * EVENTS
     */


    @Subscribe
    public void onEvent(EventMenuDrawerLocal eventMenuDrawerLocal) {
        initTabLayoutTracks();
        replaceFragment(FragmentLocalArtists.newInstance(), true, false);
    }

    @Subscribe
    public void onEvent(EventMenuDrawerAlarms eventMenuDrawerAlarms) {
        initTabLayoutAlarms();
        replaceFragment(FragmentAlarms.newInstance(), true, false);
    }


    @Subscribe
    public void onEvent(SelectItemPlaylistEvent selectItemPlaylistEvent) {
        Item item = selectItemPlaylistEvent.getItem();
        replaceFragment(FragmentSpotifyTracks.newInstance(item, BR.trackVM, R.layout.item_track), false, true);
    }

    @Subscribe
    public void onEvent(LocalArtistSelectEvent localArtistSelectEvent) {
        LocalArtist item = localArtistSelectEvent.getLocalArtist();
        replaceFragment(FragmentLocalAlbums.newInstance(item), false, true);
    }


    @Subscribe
    public void onEvent(LocalAlbumSelectEvent localAlbumSelectEvent) {
        LocalAlbum item = localAlbumSelectEvent.getModel();
        replaceFragment(FragmentLocalTracks.newInstance(item, BR.trackVM, R.layout.item_track), false, true);
    }


    public void subscribe() {
        EventBus.getDefault().register(this);
    }

    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
    }


    /***
     * Will contains the filters (artist/album/tracks)
     */
    private void initTabLayoutAlarms() {
        TabLayoutManager.initTabLayoutAlarms(activity, binding.tabLayout, tab -> {
            switch (tab.getPosition()) {
                case 0:
                    replaceFragment(FragmentAlarms.newInstance(), true, false);
                    break;
            }
        });
        activityMainVM.fabVisible.set(true);
    }

    /***
     * Will contains the filters (artist/album/tracks)
     */
    private void initTabLayoutTracks() {
        TabLayoutManager.initTabLayoutLocalTracks(activity, binding.tabLayout, tab -> {
            Fragment fragment = null;
            switch (tab.getPosition()) {
                case TabLayoutManager.TAB_PLAYLIST:
                    switch (current) {
                        case local:
                            fragment = FragmentLocalPlaylists.newInstance();
                            break;
                        case spotify:
                            fragment = FragmentSpotifyPlaylists.newInstance();
                            break;
                    }
                    break;
                case TabLayoutManager.TAB_ARTIST:
                    switch (current) {
                        case local:
                            fragment = FragmentLocalArtists.newInstance();
                            break;
                        case spotify:
                            fragment = FragmentSpotifyArtists.newInstance();
                            break;
                    }
                    break;
                case TabLayoutManager.TAB_ALBUM:
                    switch (current) {
                        case local:
                            fragment = FragmentLocalAlbums.newInstance(null);
                            break;
                        case spotify:
                            fragment = FragmentSpotifyAlbums.newInstance();
                            break;
                    }
                    break;
                case TabLayoutManager.TAB_TRACK:
                    switch (current) {
                        case local:
                            fragment = FragmentLocalTracks.newInstance(null, BR.trackVM, R.layout.item_track);
                            break;
                        case spotify:
                            fragment = FragmentSpotifyTracks.newInstance(null, BR.trackVM, R.layout.item_track);
                            break;
                    }
                    break;
            }
            replaceFragment(fragment, false, false);

        });
        activityMainVM.fabVisible.set(false);
    }


    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST.PERMISSION_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    EventBus.getDefault().post(new EventPermission(REQUEST.PERMISSION_STORAGE, true));
                } else {
                    replaceFragment(FragmentPermission.newInstance(activity.getString(R.string.permission_local)), false, false);
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void addFragment(Fragment fragment, boolean withBackstack) {
        FragmentManager.addFragment(activity, fragment, withBackstack);
    }

    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void replaceFragment(Fragment fragment, boolean clearBackStack, boolean addToBackstack) {
        FragmentManager.replaceFragment(activity, fragment, clearBackStack, addToBackstack);
    }


    private void updateToolbar(String name, @Nullable String image) {
        binding.mainCollapsing.setTitle(name);
        if (image == null) {
            binding.ivToolbar.setVisibility(View.GONE);
            binding.title.setText(name);
        } else {
            binding.title.setText("");

            binding.ivToolbar.setVisibility(View.VISIBLE);
            Glide.with(activity).load(image).placeholder(R.drawable.ic_vinyl).into(binding.ivToolbar);
        }
    }


    public void showTabLayout() {
        activityMainVM.tabLayoutVisible.set(true);
    }

    public void hideTabLayout() {
        activityMainVM.tabLayoutVisible.set(false);
    }


    @Subscribe
    public void onEvent(EventCollapseToolbar eventCollapseToolbar) {
        String title = "";
        if (eventCollapseToolbar.getName() == null)
            title = activity.getString(R.string.drawer_filter_music);
        else {
            title = eventCollapseToolbar.getName();
        }

        updateToolbar(title, eventCollapseToolbar.getImageUrl());
    }

    @Subscribe
    public void onEvent(EventTabBars eventTabBars) {
        if (eventTabBars.isVisible()) {
            showTabLayout();
        } else {
            hideTabLayout();
        }
    }


}
