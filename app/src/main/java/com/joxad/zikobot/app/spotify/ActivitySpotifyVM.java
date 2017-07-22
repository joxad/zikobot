package com.joxad.zikobot.app.spotify;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.alarm.DialogFragmentSettings;
import com.joxad.zikobot.app.core.mock.Mock;
import com.joxad.zikobot.app.core.utils.Constants;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.core.utils.ZikoUtils;
import com.joxad.zikobot.app.core.viewutils.EndlessRecyclerViewScrollListener;
import com.joxad.zikobot.app.databinding.ActivitySpotifyBinding;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.player.PlayerVM;
import com.joxad.zikobot.app.player.event.EventAddList;
import com.joxad.zikobot.data.event.EventShowMessage;
import com.joxad.zikobot.data.event.dialog.EventShowDialogSettings;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.spotify_api.manager.SpotifyApiManager;
import com.joxad.zikobot.data.module.spotify_api.model.Item;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyPlaylistItem;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by josh on 09/08/16.
 */
public class ActivitySpotifyVM extends ActivityBaseVM<ActivitySpotify, ActivitySpotifyBinding> {


    private static final String TAG = ActivitySpotifyVM.class.getSimpleName();
    public ItemBinding itemViewTrack = ItemBinding.of(BR.trackVM, R.layout.item_track);

    public PlayerVM playerVM;
    public ObservableArrayList<TrackVM> tracks;
    public Item album;
    private AlertDialog alertDialog;

    private int lastItem=0;
    /***
     * @param activity
     * @param binding
     */
    public ActivitySpotifyVM(ActivitySpotify activity, ActivitySpotifyBinding binding,@Nullable Bundle savedInstanceState) {
        super(activity, binding,savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        album = Parcels.unwrap(activity.getIntent().getParcelableExtra(EXTRA.PLAYLIST));
        tracks = new ObservableArrayList<>();

        binding.rv.setLayoutManager(new GridLayoutManager(activity,1));
        binding.rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(binding.rv.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Logger.d("Page %d",page);
                loadTracks(album, Constants.PAGINATION_LIMIT, page*Constants.PAGINATION_LIMIT);
            }
        });
        initToolbar();
        initPlayerVM();
    }


    /***
     * Init the toolar
     */
    private void initToolbar() {
        ZikoUtils.prepareToolbar(activity, binding.customToolbar, album.getName(), album.getImages().get(0).getUrl());
        ZikoUtils.animateScale(binding.fabPlay);
    }


    /***
     *
     */

    private void initPlayerVM() {
        playerVM = new PlayerVM(activity, binding.viewPlayer);
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        loadTracks(album,Constants.PAGINATION_LIMIT,0);

    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        playerVM.onResume();
    }


    @Subscribe
    public void onEvent(EventShowDialogSettings event) {
        DialogFragmentSettings dialogFragmentSettings = DialogFragmentSettings.newInstance(event.getModel());
        dialogFragmentSettings.show(activity.getSupportFragmentManager(), DialogFragmentSettings.TAG);

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
    private void loadTracks(Item playlist, int limit,int offset) {
        SpotifyApiManager.getInstance().getPlaylistTracks(playlist.getId(),limit,offset).subscribe(spotifyPlaylistWithTrack -> {
            for (SpotifyPlaylistItem playlistItem : spotifyPlaylistWithTrack.getItems()) {
                tracks.add(new TrackVM(activity, Track.from(playlistItem.track)));
            }
        }, throwable -> {
            Snackbar.make(binding.getRoot(), throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
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
        if (playerVM.onBackPressed()) {
            binding.fabPlay.setVisibility(View.GONE);
            return true;
        }
        return super.onBackPressed();
    }
}
