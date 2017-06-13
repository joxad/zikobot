package com.joxad.zikobot.app.home;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.alarm.FragmentAlarms;
import com.joxad.zikobot.app.album.FragmentLocalAlbums;
import com.joxad.zikobot.app.artist.FragmentLocalArtists;
import com.joxad.zikobot.app.core.fragmentmanager.NavigationManager;
import com.joxad.zikobot.app.databinding.ActivityMainBinding;
import com.joxad.zikobot.app.deezer.FragmentDeezerPlaylists;
import com.joxad.zikobot.app.home.event.EventNoInternet;
import com.joxad.zikobot.app.localtracks.FragmentLocalTracks;
import com.joxad.zikobot.app.player.PlayerVM;
import com.joxad.zikobot.app.soundcloud.FragmentSoundCloudPlaylists;
import com.joxad.zikobot.app.spotify.FragmentSpotifyPlaylists;
import com.joxad.zikobot.app.youtube.download.EventDownloadDone;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.event.EventShowMessage;
import com.joxad.zikobot.data.module.spotify_auth.manager.SpotifyAuthManager;
import com.joxad.zikobot.data.module.spotify_auth.model.SpotifyToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;

import rx.functions.Action1;

/**
 * Created by josh on 08/06/16.
 */
public class ActivityMainVM extends ActivityBaseVM<ActivityMain, ActivityMainBinding> {

    public NavigationManager navigationManager;
    public PlayerVM playerVM;
    private AlertDialog alertDialog;

    private ViewPagerAdapter tabAdapter;
    private FragmentAlarms fragmentAlarms;
    private int currentPosition;

    /***
     * @param activity
     * @param binding
     */
    public ActivityMainVM(ActivityMain activity, ActivityMainBinding binding) {
        super(activity, binding);
    }

    @Override
    public void onCreate() {
        initNavigationManager();
        initTabLayout();
        initPlayerVM();
        initToolbar();
        initMenu();

    }

    private void initTabLayout() {
        fragmentAlarms = FragmentAlarms.newInstance();
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        tabAdapter = new ViewPagerAdapter(activity, activity.getSupportFragmentManager());
        binding.viewPager.setAdapter(tabAdapter);
        // Give the TabLayout the ViewPager
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        tabAdapter.addFragment(activity.getString(R.string.drawer_my_playlists), fragmentAlarms);
        tabAdapter.addFragment(activity.getString(R.string.drawer_filter_artiste), FragmentLocalArtists.newInstance());
        tabAdapter.addFragment(activity.getString(R.string.drawer_filter_album), FragmentLocalAlbums.newInstance(null));
        tabAdapter.addFragment(activity.getString(R.string.drawer_filter_tracks), FragmentLocalTracks.newInstance(null, BR.trackVM, R.layout.item_track));
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    private void initNavigationManager() {
        navigationManager = new NavigationManager(activity);
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
        playerVM = new PlayerVM(activity, binding.viewPlayer);
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
        EventBus.getDefault().register(this);
        if (currentPosition == 0 || currentPosition == 1)
            fragmentAlarms.refreshAlarms();
        navigationManager.onResume();
        playerVM.onResume();

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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(EventDownloadDone e) {
        Toast.makeText(activity, "Download done", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPause() {
        super.onPause();
        playerVM.onPause();
        navigationManager.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected boolean onBackPressed() {
        return playerVM.onBackPressed();
    }


}
