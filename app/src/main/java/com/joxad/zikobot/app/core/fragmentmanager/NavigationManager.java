package com.joxad.zikobot.app.core.fragmentmanager;

import android.Manifest;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityOptionsCompat;

import com.deezer.sdk.model.Playlist;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.alarm.DialogFragmentSettings;
import com.joxad.zikobot.app.alarm.DialogPlaylistEdit;
import com.joxad.zikobot.app.alarm.event.EventAlarmSelect;
import com.joxad.zikobot.app.alarm.event.EventEditAlarm;
import com.joxad.zikobot.app.deezer.event.SelectDeezerItemPlaylistEvent;
import com.joxad.zikobot.app.home.event.EventAskPermissionStorage;
import com.joxad.zikobot.app.home.event.EventPermissionRefresh;
import com.joxad.zikobot.app.player.event.EventShowTab;
import com.joxad.zikobot.data.event.EventSelectItemNetwork;
import com.joxad.zikobot.data.event.EventShowArtistDetail;
import com.joxad.zikobot.data.event.LocalAlbumSelectEvent;
import com.joxad.zikobot.data.event.SelectItemPlaylistEvent;
import com.joxad.zikobot.data.event.dialog.EventShowDialogAlbumSettings;
import com.joxad.zikobot.data.event.dialog.EventShowDialogSettings;
import com.joxad.zikobot.data.event.soundcloud.SelectSCItemPlaylistEvent;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;
import com.joxad.zikobot.data.module.spotify_api.model.Item;
import com.joxad.zikobot.data.module.tablature.TablatureManager;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import lombok.Data;

/**
 * Created by josh on 10/06/16.
 */
@Data
public class NavigationManager  {


    Handler handler = new Handler(Looper.getMainLooper());

    private final RxPermissions rxPermissions;
    public enum Account {local, spotify, deezer, soundcloud}

    private Account current = Account.local;

    private final Activity activity;
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

    public void goToSearch() {
        activity.startActivity(IntentManager.goToSearch());
    }

    @Subscribe
    public void onEvent(SelectItemPlaylistEvent selectItemPlaylistEvent) {
        Item item = selectItemPlaylistEvent.getItem();
        activity.startActivity(IntentManager.goToSpotifyPlaylist(item));
    }

    @Subscribe
    public void onEvent(SelectSCItemPlaylistEvent selectItemPlaylistEvent) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity, selectItemPlaylistEvent.getView(), activity.getString(R.string.transition));
        SoundCloudPlaylist item = selectItemPlaylistEvent.getItem();
        activity.startActivity(IntentManager.goToSoundCloudPlaylist(item), options.toBundle());
    }

    @Subscribe
    public void onEvent(SelectDeezerItemPlaylistEvent selectItemPlaylistEvent) {
        Playlist item = selectItemPlaylistEvent.getItem();
        activity.startActivity(IntentManager.goToDeezerPlaylist(item));
    }

    @Subscribe
    public void onEvent(EventShowArtistDetail eventShowArtistDetail) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity, eventShowArtistDetail.getView(), activity.getString(R.string.transition));
        activity.startActivity(IntentManager.goToArtist(eventShowArtistDetail.getArtist()), options.toBundle());
    }


    @Subscribe
    public void onEvent(EventEditAlarm editAlarm) {
        DialogPlaylistEdit dialogPlaylistEdit = DialogPlaylistEdit.newInstance(editAlarm.getAlarm());
        dialogPlaylistEdit.show(supportFragmentManager, DialogPlaylistEdit.TAG);
        dialogPlaylistEdit.setOnDismissListener(() -> {
           // SnackUtils.showConfirm(binding.getRoot(),R.string.alarm_confirm_edit);
            EventBus.getDefault().post(new EventRefreshAlarms(editAlarm.getAlarm().getId()));
        });
    }

    @Subscribe
    public void onEvent(EventAlarmSelect eventAlarmSelect) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity, eventAlarmSelect.getView(), activity.getString(R.string.transition));
        activity.startActivity(IntentManager.goToAlarm(eventAlarmSelect.getModel()),options.toBundle());
    }

    @Subscribe
    public void onEvent(LocalAlbumSelectEvent localAlbumSelectEvent) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity, localAlbumSelectEvent.getView(), activity.getString(R.string.transition));
        activity.startActivity(IntentManager.goToAlbum(localAlbumSelectEvent.getModel()), options.toBundle());
    }

    @Subscribe
    public void onEvent(EventShowTab event) {
        TablatureManager.showTab(activity, event.getTrackVM().getName(), event.getTrackVM().getArtistName());
    }

    @Subscribe
    public void onEvent(EventShowDialogSettings event) {
        DialogFragmentSettings dialogFragmentSettings = DialogFragmentSettings.newInstance(event.getModel());
        dialogFragmentSettings.show(supportFragmentManager, DialogFragmentSettings.TAG);
    }

    @Subscribe
    public void onEvent(EventShowDialogAlbumSettings event) {
        DialogFragmentSettings dialogFragmentSettings = DialogFragmentSettings.newInstance(event.getModel());
        dialogFragmentSettings.show(supportFragmentManager, DialogFragmentSettings.TAG);
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

    @Subscribe
    public void onReceive(EventAskPermissionStorage eventAskPermissionStorage) {
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                EventBus.getDefault().post(new EventPermissionRefresh());
            }
        });
    }

}
