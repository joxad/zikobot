package com.startogamu.zikobot.viewmodel.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.easydatabinding.activity.INewIntent;
import com.joxad.easydatabinding.activity.IPermission;
import com.joxad.easydatabinding.activity.IResult;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventFabClicked;
import com.startogamu.zikobot.core.event.EventShowMessage;
import com.startogamu.zikobot.core.event.dialog.EventShowDialogAlarm;
import com.startogamu.zikobot.core.event.player.EventPlayListClicked;
import com.startogamu.zikobot.core.event.player.EventShowTab;
import com.startogamu.zikobot.core.fragmentmanager.DrawerManager;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.core.fragmentmanager.NavigationManager;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.databinding.ActivityMainBinding;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.tablature.TablatureManager;
import com.startogamu.zikobot.view.activity.ActivityMain;
import com.startogamu.zikobot.view.fragment.alarm.DialogFragmentAlarms;
import com.startogamu.zikobot.view.fragment.alarm.FragmentAlarms;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalAlbums;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalArtists;
import com.startogamu.zikobot.view.fragment.local.FragmentLocalTracks;
import com.startogamu.zikobot.view.fragment.soundcloud.FragmentSoundCloudPlaylists;
import com.startogamu.zikobot.view.fragment.spotify.FragmentSpotifyConnect;
import com.startogamu.zikobot.view.fragment.spotify.FragmentSpotifyPlaylists;
import com.startogamu.zikobot.viewmodel.custom.PlayerVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;

/**
 * Created by josh on 08/06/16.
 */
public class ActivityMainVM extends ActivityBaseVM<ActivityMain, ActivityMainBinding> implements IResult, IPermission, INewIntent {
    public ActionBarDrawerToggle actionBarDrawerToggle;

    public NavigationManager navigationManager;
    public DrawerManager drawerManager;
    public PlayerVM playerVM;
    public ObservableBoolean fabVisible, tabLayoutVisible;
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
        fabVisible = new ObservableBoolean(false);
        tabLayoutVisible = new ObservableBoolean(true);
        initSpotify();
        initNavigationManager();
        initTabLayout();
        initToolbar();
        initDrawer();
        initPlayerVM();
        initMenu();
    }

    private void initTabLayout() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        tabAdapter = new ViewPagerAdapter(activity, activity.getSupportFragmentManager());
        binding.viewPager.setAdapter(tabAdapter);
        // Give the TabLayout the ViewPager
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                fabVisible.set(position == 0);
            }
        });
        fabVisible.set(true);
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
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
    }


    /***
     * Init the menu drawer layout that will contains the other way to play music
     */
    private void initDrawer() {
        drawerManager = new DrawerManager(activity, this, binding);
        drawerManager.init();
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
                case R.id.action_search:
                    navigationManager.showSearch();
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
        if (AppPrefs.isFirstStart()) {
            activity.startActivity(IntentManager.goToTuto());
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


    @Subscribe
    public void onEvent(EventShowTab event) {
        TablatureManager.showTab(activity, event.getTrackVM().getName(), event.getTrackVM().getArtistName());
    }

    @Subscribe
    public void onEvent(EventShowDialogAlarm event) {
        DialogFragmentAlarms dialogFragmentAlarms = DialogFragmentAlarms.newInstance(event.getModel());
        dialogFragmentAlarms.show(activity.getSupportFragmentManager(), DialogFragmentAlarms.TAG);

    }

    /***
     * Called when the play button is called in above a list of tracks
     *
     * @param view
     */
    public void onPlayListClicked(View view) {
        EventBus.getDefault().post(new EventPlayListClicked());
    }

    /***
     * @param view
     */
    public void fabClicked(View view) {
        EventBus.getDefault().post(new EventFabClicked());
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

    /***
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    /***
     * @param intent
     */
    @Override
    public void onNewIntent(Intent intent) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        navigationManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean onBackPressed() {
        if (drawerManager.isDrawerOpen()) {
            drawerManager.closeDrawer();
            return false;
        }
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
                    if (AppPrefs.spotifyUser()!=null) {
                        return FragmentSpotifyPlaylists.newInstance();
                    }
                    if (AppPrefs.soundCloudUser()!=null){
                        return FragmentSoundCloudPlaylists.newInstance();
                    }
                case 5:
                    return FragmentSoundCloudPlaylists.newInstance();
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
                    return activity.getString(R.string.spotify);
                case 5:
                    return activity.getString(R.string.soundcloud);
                default:

                    return null;
            }
        }

    }


}
