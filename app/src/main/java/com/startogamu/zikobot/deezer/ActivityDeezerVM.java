package com.startogamu.zikobot.deezer;

import android.databinding.ObservableArrayList;
import android.view.View;

import com.deezer.sdk.model.Playlist;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.alarm.DialogFragmentSettings;
import com.startogamu.zikobot.core.event.dialog.EventShowDialogSettings;
import com.startogamu.zikobot.core.event.player.EventAddList;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.core.module.deezer.DeezerManager;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.databinding.ActivityDeezerBinding;
import com.startogamu.zikobot.localtracks.TrackVM;
import com.startogamu.zikobot.player.PlayerVM;

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
    public void onCreate() {
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
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        playerVM.onResume();
        loadTracks(deezerPlaylist);
        //TODO getinfos on the album
    }


    @Subscribe
    public void onEvent(EventShowDialogSettings event) {
        DialogFragmentSettings dialogFragmentSettings = DialogFragmentSettings.newInstance(event.getModel());
        dialogFragmentSettings.show(activity.getSupportFragmentManager(), DialogFragmentSettings.TAG);

    }

    @Override
    public void onPause() {
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
        DeezerManager.getInstance().playlistTracks(playlist.getId()).subscribe(deezerTracks -> {
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
        EventBus.getDefault().post(new EventAddList(tracks));
    }


    @Override
    protected boolean onBackPressed() {

        return super.onBackPressed();
    }
}
