package com.startogamu.zikobot.deezer;

import android.databinding.ObservableArrayList;
import android.view.View;

import com.deezer.sdk.model.Playlist;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.dialog.EventShowDialogAlarm;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.databinding.ActivityDeezerBinding;
import com.startogamu.zikobot.module.deezer.DeezerManager;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.alarm.DialogFragmentAlarms;
import com.startogamu.zikobot.localtracks.TrackVM;
import com.startogamu.zikobot.home.PlayerVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 25/08/16.
 */
public class ActivityDeezerVM extends ActivityBaseVM<ActivityDeezer, ActivityDeezerBinding> {


    public ItemView itemViewTrack = ItemView.of(BR.trackVM, R.layout.item_track);

    public PlayerVM playerVM;
    public ObservableArrayList<TrackVM> tracks;

    public Playlist deezerPlaylist;

    /***
     * @param activity
     * @param binding
     */
    public ActivityDeezerVM(ActivityDeezer activity, ActivityDeezerBinding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {
        deezerPlaylist = Parcels.unwrap(activity.getIntent().getParcelableExtra(EXTRA.PLAYLIST));
        tracks = new ObservableArrayList<>();
        binding.rv.setNestedScrollingEnabled(false);
        initToolbar();
        initPlayerVM();
    }

    /***
     * Init the toolar
     */
    private void initToolbar() {
        ZikoUtils.prepareToolbar(activity, binding.customToolbar, deezerPlaylist.getTitle(), deezerPlaylist.getBigImageUrl());
        ZikoUtils.animateScale(binding.fabPlay);
    }


    /***
     *
     */

    private void initPlayerVM() {
        playerVM = new PlayerVM(activity, binding.viewPlayer);
    }


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        playerVM.onResume();
        loadTracks(deezerPlaylist);
        //TODO getinfos on the album
    }


    @Subscribe
    public void onEvent(EventShowDialogAlarm event) {
        DialogFragmentAlarms dialogFragmentAlarms = DialogFragmentAlarms.newInstance(event.getModel());
        dialogFragmentAlarms.show(activity.getSupportFragmentManager(), DialogFragmentAlarms.TAG);

    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        playerVM.onPause();
    }

    /***
     * FInd the list of track from the playlist
     *
     * @param playlist
     */
    private void loadTracks(Playlist playlist) {
        tracks.clear();
        DeezerManager.playlistTracks(playlist.getId()).subscribe(deezerTracks -> {
            for (com.deezer.sdk.model.Track track : deezerTracks) {
                tracks.add(new TrackVM(activity, Track.from(track)));
            }
        }, throwable -> {

        });
    }


    /**
     * Play all the tracks of the album
     *
     * @param view
     */
    public void onPlay(View view) {
        EventBus.getDefault().post(new EventAddTrackToPlayer(tracks));
    }

    @Override
    public void destroy() {

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
