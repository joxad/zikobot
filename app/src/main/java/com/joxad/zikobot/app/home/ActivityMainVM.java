package com.joxad.zikobot.app.home;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.easydatabinding.activity.IPermission;
import com.joxad.zikobot.app.alarm.FragmentAlarms;
import com.joxad.zikobot.app.album.FragmentLocalAlbums;
import com.joxad.zikobot.data.event.EventShowMessage;
import com.joxad.zikobot.app.core.fragmentmanager.NavigationManager;
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager;
import com.joxad.zikobot.app.deezer.FragmentDeezerPlaylists;
import com.joxad.zikobot.app.player.PlayerVM;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.app.databinding.ActivityMainBinding;
import com.joxad.zikobot.app.artist.FragmentLocalArtists;
import com.joxad.zikobot.app.localtracks.FragmentLocalTracks;
import com.joxad.zikobot.app.soundcloud.FragmentSoundCloudPlaylists;
import com.joxad.zikobot.app.spotify.FragmentSpotifyPlaylists;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;

/**
 * Created by josh on 08/06/16.
 */
public class ActivityMainVM extends ActivityBaseVM<ActivityMain, ActivityMainBinding> implements IPermission {

    public NavigationManager navigationManager;
    public PlayerVM playerVM;
    private AlertDialog alertDialog;

    private ViewPagerAdapter tabAdapter;

    /***
     * @param activity
     * @param binding
     */
    public ActivityMainVM(ActivityMain activity, ActivityMainBinding binding) {
        super(activity, binding);
    }

    @Override
    public void onCreate() {
        initSpotify();
        initNavigationManager();
        initTabLayout();
        initPlayerVM();
        initToolbar();
        initMenu();

        if (AppPrefs.isFirstStart()) {
//            activity.startActivity(IntentManager.goToTuto());
        }
       
    }

    private void initTabLayout() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        tabAdapter = new ViewPagerAdapter(activity, activity.getSupportFragmentManager());
        binding.viewPager.setAdapter(tabAdapter);
        // Give the TabLayout the ViewPager
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        tabAdapter.addFragment(activity.getString(R.string.drawer_my_playlists), FragmentAlarms.newInstance());
        tabAdapter.addFragment(activity.getString(R.string.drawer_filter_artiste), FragmentLocalArtists.newInstance());
        tabAdapter.addFragment(activity.getString(R.string.drawer_filter_album), FragmentLocalAlbums.newInstance(null));
        tabAdapter.addFragment(activity.getString(R.string.drawer_filter_tracks), FragmentLocalTracks.newInstance(null, BR.trackVM, R.layout.item_track));
      //  tabAdapter.addFragment(activity.getString(R.string.drawer_filter_network), FragmentLocalNetwork.newInstance(null, null));
    }

    /***
     * Refresh token of spotify if existing
     */
    private void initSpotify() {
        if (AppPrefs.spotifyUser() == null)
            return;
        try {
            SpotifyAuthManager.getInstance().refreshToken(activity, () -> {
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private void initNavigationManager() {
        navigationManager = new NavigationManager(this, activity, binding, activity.getSupportFragmentManager());
    }

    /***
     * Init the toolar
     */
    private void initToolbar() {
        activity.setSupportActionBar(binding.toolbar);
    }

    /***
     *
     */
    private void initPlayerVM() {
        playerVM = new PlayerVM(activity,binding.viewPlayer);
    }

    /**
     * Init the action on the toolbar menu
     */
    private void initMenu() {
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_account:
                    navigationManager.showAccounts();
                    break;
                case R.id.action_about:
                    navigationManager.showAbout();
                    break;
                case R.id.action_search:
                    navigationManager.goToSearch();
                    break;
            }
            return false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationManager.onResume();
        playerVM.onResume();
        EventBus.getDefault().register(this);

        if (AppPrefs.spotifyUser() != null)
            tabAdapter.addFragment(activity.getString(R.string.activity_music_spotify), FragmentSpotifyPlaylists.newInstance());
        if (AppPrefs.soundCloudUser() != null) {
            tabAdapter.addFragment(activity.getString(R.string.soundcloud), FragmentSoundCloudPlaylists.newInstance());
        }
        if (AppPrefs.deezerUser() != null) {
            tabAdapter.addFragment(activity.getString(R.string.activity_music_deezer), FragmentDeezerPlaylists.newInstance());
        }
        tabAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(EventShowMessage event) {
        if (alertDialog != null && alertDialog.isShowing())
            return;
        alertDialog = new AlertDialog.Builder(activity)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(event.getTitle())
                .setMessage(event.getString())
                .create();
        alertDialog.show();
    }


    @Override
    public void onPause() {
        super.onPause();
        playerVM.onPause();
        navigationManager.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        navigationManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected boolean onBackPressed() {
        return playerVM.onBackPressed();
    }


}
