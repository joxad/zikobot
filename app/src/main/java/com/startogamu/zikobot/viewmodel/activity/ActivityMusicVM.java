package com.startogamu.zikobot.viewmodel.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
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
import com.startogamu.zikobot.core.event.SelectAllTracks;
import com.startogamu.zikobot.core.event.SelectItemPlaylistEvent;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.core.utils.REQUEST;
import com.startogamu.zikobot.databinding.ActivityMusicBinding;
import com.startogamu.zikobot.module.alarm.manager.AlarmManager;
import com.startogamu.zikobot.module.alarm.manager.AlarmTrackManager;
import com.startogamu.zikobot.module.alarm.model.Alarm;
import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.module.content_resolver.model.LocalArtist;
import com.startogamu.zikobot.module.spotify_api.model.Item;
import com.startogamu.zikobot.view.activity.ActivityMusic;
import com.startogamu.zikobot.view.fragment.DeezerFragment;
import com.startogamu.zikobot.view.fragment.FragmentSpotifyPlaylistTracks;
import com.startogamu.zikobot.view.fragment.SpotifyConnectFragment;
import com.startogamu.zikobot.view.fragment.SpotifyMusicFragment;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalAlbums;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalArtists;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalMusic;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.tatarka.bindingcollectionadapter.ItemView;

/***
 * {@link ActivityMusicVM} handle multiples fragments :
 * <ul>
 * <li>{@link SpotifyMusicFragment } taht show the differents playlist to the user. Can redirect to
 * {@link FragmentSpotifyPlaylistTracks}</li>
 * <li>{@link SpotifyConnectFragment} that handle the connection to spotify</li>
 * <li>{@link FragmentLocalMusic}</li>
 * </ul>
 */
public class ActivityMusicVM extends ActivityBaseVM<ActivityMusic, ActivityMusicBinding> implements IResult, INewIntent, IPermission {


    private SpotifyMusicFragment spotifyMusicFragment;
    private SpotifyConnectFragment spotifyConnectFragment;
    private FragmentLocalArtists fragmentLocalArtists;

    @InjectExtra
    Alarm alarm;

    /***
     * @param activity
     * @param binding
     */
    public ActivityMusicVM(ActivityMusic activity, ActivityMusicBinding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        Dart.inject(this, activity);

        AlarmTrackManager.init(alarm);
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(listener -> activity.onBackPressed());
        fragmentLocalArtists = FragmentLocalArtists.newInstance();
        activity.replaceFragment(fragmentLocalArtists, false);
        createBottomNavigation(binding.bottomNavigation);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_add_all:
                    EventBus.getDefault().post(new SelectAllTracks());
                    break;
            }

            return false;
        });
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
                    activity.replaceFragment(fragmentLocalArtists, false);
                    break;
                case 1:
                    // if (spotifyManager.hasAccessToken()) {
                    if (!Prefs.contains(AppPrefs.SPOTIFY_ACCESS_CODE)) {
                        spotifyConnectFragment = SpotifyConnectFragment.newInstance();
                        activity.replaceFragment(spotifyConnectFragment, false);
                    } else {
                        loadSpotifyMusicFragment();
                    }
                    break;
                case 2:
                    activity.replaceFragment(DeezerFragment.newInstance(), false);
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
        activity.replaceFragment(spotifyMusicFragment, false);
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


    @Subscribe
    public void onEvent(SelectItemPlaylistEvent selectItemPlaylistEvent) {
        Item item = selectItemPlaylistEvent.getItem();
        activity.replaceFragment(FragmentSpotifyPlaylistTracks.newInstance(item), true);
    }

    @Subscribe
    public void onEvent(LocalArtistSelectEvent localArtistSelectEvent) {
        LocalArtist item = localArtistSelectEvent.getLocalArtist();
        activity.replaceFragment(FragmentLocalAlbums.newInstance(item), true);
    }

    @Subscribe
    public void onEvent(LocalAlbumSelectEvent localAlbumSelectEvent) {
        LocalAlbum item = localAlbumSelectEvent.getModel();
        activity.replaceFragment(FragmentLocalMusic.newInstance(item, BR.trackVM, R.layout.item_alarm_track), true);
    }


    public void onFabAddClicked(View view) {
        AlarmManager.saveAlarm(alarm, AlarmTrackManager.tracks()).subscribe(alarm1 -> {
            activity.finish();
        });
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
