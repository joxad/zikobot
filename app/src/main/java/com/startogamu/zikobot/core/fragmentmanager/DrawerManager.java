package com.startogamu.zikobot.core.fragmentmanager;

import android.content.Intent;
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
import com.startogamu.zikobot.module.deezer.manager.DeezerManager;
import com.startogamu.zikobot.module.soundcloud.model.SoundCloudUser;
import com.startogamu.zikobot.module.spotify_api.model.SpotifyUser;
import com.startogamu.zikobot.view.activity.ActivityMain;
import com.startogamu.zikobot.view.activity.fingerprint.ActivityFingerprint;
import com.startogamu.zikobot.viewmodel.activity.ActivityMainVM;

import org.greenrobot.eventbus.EventBus;

import lombok.Data;

/**
 * Created by josh on 16/06/16.
 */
@Data
public class DrawerManager {
    protected ProfileDrawerItem itemLocal;
    protected ProfileDrawerItem itemSpotify;
    protected ProfileDrawerItem itemSoundCloud;
    protected ProfileDrawerItem itemDeezer;

    private ProfileDrawerItem itemAddAccount;
    private Drawer drawer;

    private final ActivityMain activity;
    private final ActivityMainVM activityMainVM;
    private final ActivityMainBinding binding;
    private AccountHeader accountHeader;


    /***
     * Initialise the drawer manager
     */
    public void init() {
      //  initAccountHeader();

        PrimaryDrawerItem music = new PrimaryDrawerItem().withName(R.string.drawer_filter_music).withIcon(R.drawable.ic_music_wave).withSelectedIcon(R.drawable.ic_music_wave_selected);
        PrimaryDrawerItem accounts = new PrimaryDrawerItem().withName(R.string.activity_my_account).withSelectable(false);
        PrimaryDrawerItem fingerprint = new PrimaryDrawerItem().withName(R.string.activity_fingerprint).withSelectable(false);

        PrimaryDrawerItem about = new PrimaryDrawerItem().withName(R.string.menu_about).withSelectable(false);
        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(accountHeader)
                .withToolbar(binding.toolbar)
                .addDrawerItems(music, accounts, fingerprint, about)
                .build();

        drawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            long drawerId = drawerItem.getIdentifier();
            if (drawerId == music.getIdentifier()) {
                //  activityMainVM.navigationManager.showLocals();
            } else if (drawerId == about.getIdentifier()) {
                //activityMainVM.navigationManager.showAbout();
            } else if (drawerId == accounts.getIdentifier()) {
                activity.startActivity(IntentManager.goToSettings());
            } else if (drawerId == fingerprint.getIdentifier()) {
                activity.startActivity(new Intent(activity, ActivityFingerprint.class));
            }
            return false;
        });
        activityMainVM.actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawer.getDrawerLayout(),
                R.string.drawer_open, R.string.drawer_close);
        activityMainVM.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setActionBarDrawerToggle(activityMainVM.actionBarDrawerToggle);
    }


    public boolean isDrawerOpen() {
        return drawer.isDrawerOpen();
    }

    public void closeDrawer() {
        drawer.closeDrawer();
    }
}
