package com.startogamu.zikobot.home;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.easydatabinding.activity.IPermission;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventShowMessage;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.core.fragmentmanager.NavigationManager;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.databinding.ActivityMainBinding;
import com.startogamu.zikobot.localnetwork.FragmentLocalNetwork;
import com.startogamu.zikobot.module.spotify_auth.manager.SpotifyAuthManager;
import com.startogamu.zikobot.alarm.FragmentAlarms;
import com.startogamu.zikobot.deezer.FragmentDeezerPlaylists;
import com.startogamu.zikobot.album.FragmentLocalAlbums;
import com.startogamu.zikobot.artist.FragmentLocalArtists;
import com.startogamu.zikobot.localtracks.FragmentLocalTracks;
import com.startogamu.zikobot.soundcloud.FragmentSoundCloudPlaylists;
import com.startogamu.zikobot.spotify.FragmentSpotifyPlaylists;

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
    public void init() {
        initSpotify();
        initNavigationManager();
        initTabLayout();
        initPlayerVM();
        initToolbar();
        initMenu();

        if (AppPrefs.isFirstStart()) {
            activity.startActivity(IntentManager.goToTuto());
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
        tabAdapter.addFragment(activity.getString(R.string.drawer_filter_network), FragmentLocalNetwork.newInstance(null, null));
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
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
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

/*TODO test play from other app
        if (activity.getIntent().getData() != null) {
            Uri uri = activity.getIntent().getData();
            ObservableArrayList<TrackVM> trackVMs = new ObservableArrayList<>();
            Track track = new Track();
            track.setType(TYPE.LOCAL);
            track.setName(uri.getLastPathSegment());
            track.setRef(uri.getEncodedPath());
            trackVMs.add(new TrackVM(activity, track));
            EventBus.getDefault().post(new EventAddTrackToPlayer(trackVMs));
        }*/
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

    /***
     * @param view
     */
    public void fabClicked(View view) {
        activity.startActivity(IntentManager.goToSearch());
    }


    @Override
    protected void onPause() {
        super.onPause();
        playerVM.onPause();
        navigationManager.onPause();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        navigationManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected boolean onBackPressed() {
        if (playerVM.isExpanded.get()) {
            playerVM.close();
            return false;
        }
        return super.onBackPressed();
    }


}
