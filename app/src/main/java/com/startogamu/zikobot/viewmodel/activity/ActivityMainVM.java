package com.startogamu.zikobot.viewmodel.activity;

import android.app.Activity;
import android.database.Cursor;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.easydatabinding.activity.IPermission;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventShowMessage;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.core.fragmentmanager.NavigationManager;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.databinding.ActivityMainBinding;
import com.startogamu.zikobot.localnetwork.FragmentLocalNetwork;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.zikobot.model.TYPE;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.view.activity.ActivityMain;
import com.startogamu.zikobot.view.fragment.alarm.FragmentAlarms;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalAlbums;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalArtists;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalTracks;
import com.startogamu.zikobot.view.fragment.soundcloud.FragmentSoundCloudPlaylists;
import com.startogamu.zikobot.view.fragment.spotify.FragmentSpotifyPlaylists;
import com.startogamu.zikobot.viewmodel.base.TrackVM;
import com.startogamu.zikobot.viewmodel.custom.PlayerVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;

/**
 * Created by josh on 08/06/16.
 */
public class ActivityMainVM extends ActivityBaseVM<ActivityMain, ActivityMainBinding> implements IPermission {

    public NavigationManager navigationManager;
    public PlayerVM playerVM;
    public ObservableBoolean tabLayoutVisible;
    private AlertDialog alertDialog;
    @Nullable
    @InjectExtra
    String fromWidget;
    private ViewPagerAdapter tabAdapter;

    /***
     * @param activity
     * @param binding
     */
    public ActivityMainVM(ActivityMain activity, ActivityMainBinding binding) {
        super(activity, binding);
        Dart.inject(this, activity);
        Injector.INSTANCE.spotifyAuth().inject(this);
        Injector.INSTANCE.playerComponent().inject(this);
    }

    @Override
    public void init() {
        tabLayoutVisible = new ObservableBoolean(true);
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
    }

    /***
     * Refresh token of spotify if existing
     */
    private void initSpotify() {
        if (AppPrefs.spotifyUser() == null)
            return;
        try {
            Injector.INSTANCE.spotifyAuth().manager().refreshToken(activity, () -> {
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

        tabAdapter.notifyDataSetChanged();

        if (activity.getIntent().getData() != null) {
            Uri uri = activity.getIntent().getData();
            ObservableArrayList<TrackVM> trackVMs = new ObservableArrayList<>();
            Track track = new Track();
            track.setType(TYPE.LOCAL);
            track.setName(uri.getLastPathSegment());
            track.setRef(uri.getEncodedPath());
            trackVMs.add(new TrackVM(activity, track));
            EventBus.getDefault().post(new EventAddTrackToPlayer(trackVMs));
        }
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


    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 4;
        private Activity activity;

        public ViewPagerAdapter(Activity activity, android.support.v4.app.FragmentManager fragmentManager) {
            super(fragmentManager);
            this.activity = activity;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            int spotify = AppPrefs.spotifyUser() == null ? 0 : 1;
            int soundcloud = AppPrefs.soundCloudUser() == null ? 0 : 1;

            return NUM_ITEMS + spotify + soundcloud;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return FragmentAlarms.newInstance();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return FragmentLocalArtists.newInstance();
                case 2: // Fragment # 1 - This will show SecondFragment
                    return FragmentLocalAlbums.newInstance(null);
                case 3:
                    return FragmentLocalTracks.newInstance(null, BR.trackVM, R.layout.item_track);
                case 4:
                    if (AppPrefs.spotifyUser() != null) {
                        return FragmentSpotifyPlaylists.newInstance();
                    }
                case 5:
                    return FragmentSoundCloudPlaylists.newInstance();
                case 6:
                    return FragmentLocalNetwork.newInstance();

                default:
                    return null;
            }
        }


        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return activity.getString(R.string.drawer_my_playlists);
                case 1:
                    return activity.getString(R.string.drawer_filter_artiste);
                case 2:
                    return activity.getString(R.string.drawer_filter_album);
                case 3:
                    return activity.getString(R.string.drawer_filter_tracks);
                case 4:
                    if (AppPrefs.spotifyUser() != null) {
                        return activity.getString(R.string.spotify);
                    }
                    if (AppPrefs.soundCloudUser() != null) {
                        return activity.getString(R.string.soundcloud);
                    }
                case 5:
                    return activity.getString(R.string.soundcloud);
                case 6:
                    return "Local Network";

                default:

                    return null;
            }
        }
    }
}
