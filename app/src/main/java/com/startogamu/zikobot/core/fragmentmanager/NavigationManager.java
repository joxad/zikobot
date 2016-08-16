package com.startogamu.zikobot.core.fragmentmanager;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.bumptech.glide.Glide;
import com.joxad.easydatabinding.activity.IPermission;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventShowArtistDetail;
import com.startogamu.zikobot.core.event.LocalAlbumSelectEvent;
import com.startogamu.zikobot.core.event.SelectItemPlaylistEvent;
import com.startogamu.zikobot.core.event.alarm.EventAlarmSelect;
import com.startogamu.zikobot.core.event.deezer.SelectDeezerItemPlaylistEvent;
import com.startogamu.zikobot.core.event.drawer.EventMenuDrawerAlarms;
import com.startogamu.zikobot.core.event.drawer.EventMenuDrawerLocal;
import com.startogamu.zikobot.core.event.navigation_manager.EventAccountSelect;
import com.startogamu.zikobot.core.event.navigation_manager.EventCollapseToolbar;
import com.startogamu.zikobot.core.event.navigation_manager.EventTabBars;
import com.startogamu.zikobot.core.event.soundcloud.SelectSCItemPlaylistEvent;
import com.startogamu.zikobot.core.utils.Constants;
import com.startogamu.zikobot.databinding.ActivityMainBinding;
import com.startogamu.zikobot.search.FragmentSearch;
import com.startogamu.zikobot.view.activity.ActivityMain;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalAlbums;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalArtists;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalPlaylists;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalTracks;
import com.startogamu.zikobot.view.fragment.permission.FragmentPermission;
import com.startogamu.zikobot.viewmodel.activity.ActivityMainVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import lombok.Data;

/**
 * Created by josh on 10/06/16.
 */
@Data
public class NavigationManager implements IPermission {


    Handler handler = new Handler(Looper.getMainLooper());

    public enum Account {local, spotify, deezer, soundcloud}

    private Account current = Account.local;

    private final ActivityMainVM activityMainVM;
    private final ActivityMain activity;
    private final ActivityMainBinding binding;
    private final android.support.v4.app.FragmentManager supportFragmentManager;

    public void showSearch() {
        activity.startActivity(IntentManager.goToSearch());
    }

    @Subscribe
    public void onEvent(EventAccountSelect eventAccountSelect) {
        current = eventAccountSelect.getAccount();
        switch (current) {
            case spotify:
                showSpotifys();
                break;
            case soundcloud:
                showSoundClouds();
                break;
            case local:
                //showLocals();
                break;
            case deezer:
                showDeezers();
                break;
        }
    }

    /****
     * filter on spotify playlists
     */
    public void showSpotifys() {
        //   initTabLayoutTracks();
        //   replaceFragment(FragmentSpotifyPlaylists.newInstance(), true, false);
    }

    /****
     * filter on soundcloud playlists
     */
    public void showSoundClouds() {
        //   initTabLayoutTracks();
        //  replaceFragment(FragmentSoundCloudPlaylists.newInstance(), true, false);
    }

    /****
     * filter on deezer playlists
     */
    public void showDeezers() {
        //   initTabLayoutTracks();
        //   replaceFragment(FragmentDeezerPlaylists.newInstance(), true, false);
    }


    /***
     * filter on the alarms
     */
    public void showAlarms() {
        //   initTabLayoutAlarms();
        //   replaceFragment(FragmentAlarms.newInstance(), true, false);
    }

    /***
     * Show the libs used in the projects
     */
    public void showAbout() {
        new LibsBuilder()
                .withActivityTheme(R.style.MaterialDrawerTheme)
                .withActivityStyle(Libs.ActivityStyle.LIGHT)
                .start(activity);
    }

    /***
     * EVENTS
     */
    @Subscribe
    public void onEvent(EventMenuDrawerLocal eventMenuDrawerLocal) {
        //  initTabLayoutTracks();
        //  replaceFragment(FragmentLocalArtists.newInstance(), true, false);
    }

    @Subscribe
    public void onEvent(EventMenuDrawerAlarms eventMenuDrawerAlarms) {
        //  initTabLayoutAlarms();
        //  replaceFragment(FragmentAlarms.newInstance(), true, false);
    }

    @Subscribe
    public void onEvent(SelectItemPlaylistEvent selectItemPlaylistEvent) {
        //  Item item = selectItemPlaylistEvent.getItem();
        //  replaceFragment(FragmentSpotifyTracks.newInstance(item, BR.trackVM, R.layout.item_track), false, true);
    }

    @Subscribe
    public void onEvent(SelectSCItemPlaylistEvent selectItemPlaylistEvent) {
        //SoundCloudPlaylist item = selectItemPlaylistEvent.getItem();
        //replaceFragment(FragmentSoundCloudTracks.newInstance(item, BR.trackVM, R.layout.item_track), false, true);
    }

    @Subscribe
    public void onEvent(SelectDeezerItemPlaylistEvent selectItemPlaylistEvent) {
        //Playlist item = selectItemPlaylistEvent.getItem();
        //replaceFragment(FragmentDeezerTracks.newInstance(item, BR.trackVM, R.layout.item_track), false, true);
    }

    @Subscribe
    public void onEvent(EventShowArtistDetail eventShowArtistDetail) {
        activity.startActivity(IntentManager.goToArtist(eventShowArtistDetail.getArtist()));
    }


    @Subscribe
    public void onEvent(EventAlarmSelect eventAlarmSelect) {
        activity.startActivity(IntentManager.goToAlarm(eventAlarmSelect.getModel()));
    }

    @Subscribe
    public void onEvent(LocalAlbumSelectEvent localAlbumSelectEvent) {
        activity.startActivity(IntentManager.goToAlbum(localAlbumSelectEvent.getModel()));
    }


    public void onResume() {
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }


    private void updateToolbar(String name, @Nullable String image) {

        if (name.equals(getActivity().getString(R.string.drawer_filter_search))) {
            //TODO handle search
            binding.title.setText("");
            binding.rlToolbarImage.setVisibility(View.GONE);

        } else {

            binding.mainCollapsing.setTitle(name);
            if (image == null) {
                binding.rlToolbarImage.setVisibility(View.GONE);
                binding.title.setText(name);
            } else {
                binding.title.setText("");
                binding.rlToolbarImage.setVisibility(View.VISIBLE);
                Glide.with(activity).load(image).placeholder(R.drawable.ic_vinyl).into(binding.ivToolbar);
            }
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
