package com.startogamu.zikobot.core.fragmentmanager;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.deezer.sdk.model.Playlist;
import com.joxad.easydatabinding.activity.IPermission;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventSelectItemNetwork;
import com.startogamu.zikobot.core.event.EventShowArtistDetail;
import com.startogamu.zikobot.core.event.LocalAlbumSelectEvent;
import com.startogamu.zikobot.core.event.SelectItemPlaylistEvent;
import com.startogamu.zikobot.core.event.alarm.EventAlarmSelect;
import com.startogamu.zikobot.core.event.deezer.SelectDeezerItemPlaylistEvent;
import com.startogamu.zikobot.core.event.dialog.EventShowDialogAlarm;
import com.startogamu.zikobot.core.event.player.EventShowTab;
import com.startogamu.zikobot.core.event.soundcloud.SelectSCItemPlaylistEvent;
import com.startogamu.zikobot.databinding.ActivityMainBinding;
import com.startogamu.zikobot.module.soundcloud.model.SoundCloudPlaylist;
import com.startogamu.zikobot.module.spotify_api.model.Item;
import com.startogamu.zikobot.module.tablature.TablatureManager;
import com.startogamu.zikobot.view.activity.ActivityMain;
import com.startogamu.zikobot.view.fragment.alarm.DialogFragmentAlarms;
import com.startogamu.zikobot.viewmodel.activity.ActivityMainVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import lombok.Data;

import static android.R.attr.fragment;

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

    /***
     * Show the libs used in the projects
     */
    public void showAbout() {
        new LibsBuilder()
                .withActivityStyle(Libs.ActivityStyle.LIGHT)
                .start(activity);
    }

    public void showAccounts() {
        activity.startActivity(IntentManager.goToSettings());
    }

    @Subscribe
    public void onEvent(SelectItemPlaylistEvent selectItemPlaylistEvent) {
        Item item = selectItemPlaylistEvent.getItem();
        activity.startActivity(IntentManager.goToSpotifyPlaylist(item));
    }

    @Subscribe
    public void onEvent(SelectSCItemPlaylistEvent selectItemPlaylistEvent) {
        SoundCloudPlaylist item = selectItemPlaylistEvent.getItem();
        activity.startActivity(IntentManager.goToSoundCloudPlaylist(item));
    }

    @Subscribe
    public void onEvent(SelectDeezerItemPlaylistEvent selectItemPlaylistEvent) {
        Playlist item = selectItemPlaylistEvent.getItem();
        activity.startActivity(IntentManager.goToDeezerPlaylist(item));
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

    @Subscribe
    public void onEvent(EventShowTab event) {
        TablatureManager.showTab(activity, event.getTrackVM().getName(), event.getTrackVM().getArtistName());
    }

    @Subscribe
    public void onEvent(EventShowDialogAlarm event) {
        DialogFragmentAlarms dialogFragmentAlarms = DialogFragmentAlarms.newInstance(event.getModel());
        dialogFragmentAlarms.show(activity.getSupportFragmentManager(), DialogFragmentAlarms.TAG);

    }

    @Subscribe
    public void onReceive(EventSelectItemNetwork eventSelectItemNetwork) {
        activity.startActivity(IntentManager.goToLocalNetwork(eventSelectItemNetwork.getModel().getMedia().getUri().toString()));
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

}
