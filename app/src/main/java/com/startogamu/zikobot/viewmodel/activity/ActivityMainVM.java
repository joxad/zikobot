package com.startogamu.zikobot.viewmodel.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.easydatabinding.activity.INewIntent;
import com.joxad.easydatabinding.activity.IPermission;
import com.joxad.easydatabinding.activity.IResult;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.NavigationManager;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.databinding.ActivityMainBinding;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyUser;
import com.startogamu.zikobot.view.Henson;
import com.startogamu.zikobot.view.activity.ActivityMain;
import com.startogamu.zikobot.viewmodel.custom.PlayerVM;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by josh on 08/06/16.
 */
public class ActivityMainVM extends ActivityBaseVM<ActivityMain, ActivityMainBinding> implements IResult, IPermission, INewIntent {
    public ActionBarDrawerToggle actionBarDrawerToggle;

    public NavigationManager navigationManager;

    public PlayerVM playerVM;
    private Drawer drawer;

    /***
     * @param activity
     * @param binding
     */
    public ActivityMainVM(ActivityMain activity, ActivityMainBinding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {
        initNavigationManager();
        initToolbar();
        initDrawer();
        initPlayerVM();
    }

    private void initNavigationManager() {
        navigationManager = new NavigationManager(this, activity, binding);
        navigationManager.init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationManager.subscribe();

    }

    @Override
    protected void onPause() {
        super.onPause();
        navigationManager.unsubscribe();
    }

    private void initToolbar() {
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
    }

    /***
     * Init the menu drawer layout that will contains the other way to play music
     */
    private void initDrawer() {
        ProfileDrawerItem itemLocal = new ProfileDrawerItem().withName(activity.getString(R.string.activity_music_local))
                .withIcon(ContextCompat.getDrawable(activity, R.drawable.ic_folder));
        ProfileDrawerItem itemSpotify = null;
        if (AppPrefs.spotifyUser() != null && !AppPrefs.spotifyUser().equals("")) {
            SpotifyUser spotifyUser = AppPrefs.spotifyUser();
            itemSpotify = new ProfileDrawerItem()
                    .withName(spotifyUser.getDisplayName())
                    .withIcon(spotifyUser.getImages().get(0).getUrl());
        }


        ProfileDrawerItem itemAddAccount = new ProfileDrawerItem().withName(activity.getString(R.string.drawer_account_add))
                .withIcon(ContextCompat.getDrawable(activity, R.drawable.ic_add));
        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .withCurrentProfileHiddenInList(true)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        if (profile.getIdentifier() == itemLocal.getIdentifier()) {
                            navigationManager.showLocals();
                            return false;
                        } else if (profile.getIdentifier() == itemAddAccount.getIdentifier()) {
                            activity.startActivity(Henson.with(activity).gotoActivitySettings().build());
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .addProfiles(itemLocal, itemAddAccount)
                .withOnAccountHeaderListener((view, profile, currentProfile) -> false)
                .build();


        if (itemSpotify != null)
            accountHeader.addProfiles(itemSpotify);
        PrimaryDrawerItem music = new PrimaryDrawerItem().withName(R.string.drawer_filter_music).withIcon(R.drawable.ic_music_wave);
        PrimaryDrawerItem alarm = new PrimaryDrawerItem().withName(R.string.drawer_alarms).withIcon(R.drawable.ic_alarm);
        PrimaryDrawerItem about = new PrimaryDrawerItem().withName(R.string.about);

        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(accountHeader)
                .withToolbar(binding.toolbar)
                .addDrawerItems(music, alarm, about)
                .build();

        drawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            if (drawerItem.getIdentifier() == alarm.getIdentifier()) {
                navigationManager.showAlarms();
            } else if (drawerItem.getIdentifier() == music.getIdentifier()) {
                navigationManager.showLocals();
            }
            return false;
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawer.getDrawerLayout(),
                R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setActionBarDrawerToggle(actionBarDrawerToggle);
    }

    /***
     *
     */
    private void initPlayerVM() {
        playerVM = new PlayerVM(activity);
        binding.viewPlayer.setPlayerVM(playerVM);
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
        navigationManager.onActivityResult(requestCode, resultCode, data);
    }

    /***
     * @param intent
     */
    @Override
    public void onNewIntent(Intent intent) {
        navigationManager.onNewIntent(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        navigationManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    @Override
    protected boolean onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
            return false;
        }
        return super.onBackPressed();
    }
}
