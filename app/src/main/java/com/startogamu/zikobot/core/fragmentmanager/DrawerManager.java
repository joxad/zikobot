package com.startogamu.zikobot.core.fragmentmanager;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.navigation_manager.EventAccountSelect;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.databinding.ActivityMainBinding;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.soundcloud.model.SoundCloudUser;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyUser;
import com.startogamu.zikobot.view.Henson;
import com.startogamu.zikobot.view.activity.ActivityMain;
import com.startogamu.zikobot.viewmodel.activity.ActivityMainVM;

import org.greenrobot.eventbus.EventBus;

import lombok.Data;

/**
 * Created by josh on 16/06/16.
 */
@Data
public class DrawerManager {

    protected ProfileDrawerItem itemSpotify;
    protected ProfileDrawerItem itemSoundCloud;

    private Drawer drawer;

    private final ActivityMain activity;
    private final ActivityMainVM activityMainVM;
    private final ActivityMainBinding binding;
    private AccountHeader accountHeader;

    /***
     * Initialise the drawer manager
     */
    public void init() {
        initAccountHeader();

        PrimaryDrawerItem music = new PrimaryDrawerItem().withName(R.string.drawer_filter_music).withIcon(R.drawable.ic_music_wave).withSelectedIcon(R.drawable.ic_music_wave_selected);
        PrimaryDrawerItem alarm = new PrimaryDrawerItem().withName(R.string.drawer_alarms).withIcon(R.drawable.ic_alarm).withSelectedIcon(R.drawable.ic_alarm_selected);
        PrimaryDrawerItem about = new PrimaryDrawerItem().withName(R.string.about).withSelectable(false);
        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(accountHeader)
                .withToolbar(binding.toolbar)
                .addDrawerItems(music, alarm, about)
                .build();

        drawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            long drawerId = drawerItem.getIdentifier();
            if (drawerId == alarm.getIdentifier()) {
                activityMainVM.navigationManager.showAlarms();
            } else if (drawerId == music.getIdentifier()) {
                activityMainVM.navigationManager.showLocals();
            } else if (drawerId == about.getIdentifier()) {
                activityMainVM.navigationManager.showAbout();
            }
            return false;
        });
        activityMainVM.actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawer.getDrawerLayout(),
                R.string.drawer_open, R.string.drawer_close);
        activityMainVM.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setActionBarDrawerToggle(activityMainVM.actionBarDrawerToggle);
    }

    /***
     * Init the account manager header
     */
    private void initAccountHeader() {
        ProfileDrawerItem itemLocal = new ProfileDrawerItem().withName(activity.getString(R.string.activity_music_local))
                .withIcon(ContextCompat.getDrawable(activity, R.drawable.shape_album));

        ProfileDrawerItem itemAddAccount = new ProfileDrawerItem().withName(activity.getString(R.string.drawer_account_add))
                .withIcon(ContextCompat.getDrawable(activity, R.drawable.ic_add));
        accountHeader = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .withCurrentProfileHiddenInList(true)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        if (profile.getIdentifier() == itemLocal.getIdentifier()) {
                            EventBus.getDefault().post(new EventAccountSelect(NavigationManager.Account.local));
                            return false;
                        } else if (profile.getIdentifier() == itemAddAccount.getIdentifier()) {
                            activity.startActivity(Henson.with(activity).gotoActivitySettings().build());
                            return true;
                        }
                        if (itemSpotify != null) {
                            if (profile.getIdentifier() == itemSpotify.getIdentifier()) {
                                EventBus.getDefault().post(new EventAccountSelect(NavigationManager.Account.spotify));
                            }
                            return false;
                        }
                        if (itemSoundCloud != null) {
                            if (profile.getIdentifier() == itemSoundCloud.getIdentifier()) {
                                EventBus.getDefault().post(new EventAccountSelect(NavigationManager.Account.soundcloud));
                            }
                            return false;
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
    }

    /***
     * Manage the onresume of the activity to see of new account has been registred
     */
    public void onResume() {

//Add spotify only once
        if (itemSpotify == null) {
            if (AppPrefs.spotifyUser() != null && !AppPrefs.spotifyUser().equals("")) {
                SpotifyUser spotifyUser = AppPrefs.spotifyUser();
                itemSpotify = new ProfileDrawerItem()
                        .withName(spotifyUser.getDisplayName())
                        .withIcon(R.drawable.logo_spotify);
            }
            if (itemSpotify != null) {
                accountHeader.addProfiles(itemSpotify);
            }
        }
        if (itemSoundCloud == null) {
            if (AppPrefs.soundCloudUser() != null && !AppPrefs.soundCloudUser().equals("")) {
                SoundCloudUser soundCloudUser = AppPrefs.soundCloudUser();
                itemSoundCloud = new ProfileDrawerItem()
                        .withName(soundCloudUser.getUserName())
                        .withIcon(R.drawable.logo_soundcloud);
            }
            if (itemSoundCloud != null) {
                accountHeader.addProfiles(itemSoundCloud);
            }
        }
    }

    public boolean isDrawerOpen() {
        return drawer.isDrawerOpen();
    }

    public void closeDrawer() {
        drawer.closeDrawer();
    }
}
