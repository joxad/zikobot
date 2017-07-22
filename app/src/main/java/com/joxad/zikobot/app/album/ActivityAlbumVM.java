package com.joxad.zikobot.app.album;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.alarm.DialogFragmentSettings;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.core.utils.ZikoUtils;
import com.joxad.zikobot.app.core.viewutils.EndlessRecyclerViewScrollListener;
import com.joxad.zikobot.app.databinding.ActivityAlbumBinding;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.player.PlayerVM;
import com.joxad.zikobot.app.player.event.EventAddList;
import com.joxad.zikobot.data.event.dialog.EventShowDialogSettings;
import com.joxad.zikobot.data.model.Album;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager;
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;
import com.joxad.zikobot.data.module.spotify_api.manager.SpotifyApiManager;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyTrack;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

import static com.joxad.zikobot.data.model.TYPE.LOCAL;
import static com.joxad.zikobot.data.model.TYPE.SPOTIFY;

/**
 * Created by josh on 09/08/16.
 */
public class ActivityAlbumVM extends ActivityBaseVM<ActivityAlbum, ActivityAlbumBinding> {


    private static final String TAG = ActivityAlbumVM.class.getSimpleName();
    public ItemBinding itemViewTrack = ItemBinding.of(BR.trackVM, R.layout.item_track);

    public PlayerVM playerVM;
    public ObservableArrayList<TrackVM> tracks;

    public Album album;

    /***
     * @param activity
     * @param binding
     */
    public ActivityAlbumVM(ActivityAlbum activity, ActivityAlbumBinding binding,@Nullable Bundle saved) {
        super(activity, binding,saved);
    }

    @Override
    public void onCreate(@Nullable Bundle saved) {
        album = Parcels.unwrap(activity.getIntent().getParcelableExtra(EXTRA.LOCAL_ALBUM));
        tracks = new ObservableArrayList<>();
        binding.rv.setLayoutManager(new GridLayoutManager(activity, 1));
        binding.rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(binding.rv.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                switch (album.getType()) {
                    case LOCAL:
                        loadLocalMusic(15, totalItemsCount);
                        break;
                }

            }
        });
        initToolbar();
        initMenu();
        initPlayerVM();
    }


    private void initMenu() {
        binding.customToolbar.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    showDialogEdit();
                    break;

            }
            return false;
        });

    }


    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        handler.postDelayed(() -> {
            ZikoUtils.animateScale(binding.fabPlay);
            ZikoUtils.animateFade(binding.customToolbar.rlOverlay);
            switch (album.getType()) {
                case LOCAL:
                    loadLocalMusic(15, 0);
                    break;
                case SPOTIFY:
                    loadSpotifyTracks();
                    break;
            }

        }, 400);

    }

    /***
     * Init the toolar
     */
    private void initToolbar() {
        ZikoUtils.prepareToolbar(activity, binding.customToolbar, album.getName(), album.getImage());
        binding.customToolbar.mainCollapsing.setOnClickListener(v -> {
            showDialogEdit();
        });
    }

    private void showDialogEdit() {
        DialogFragmentSettings dialogFragmentSettings = DialogFragmentSettings.newInstance(album);
        dialogFragmentSettings.show(activity.getSupportFragmentManager(), DialogFragmentSettings.TAG);
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
     * Load the local music
     *
     * @param limit
     * @param offset
     */
    public void loadLocalMusic(int limit, int offset) {
        LocalMusicManager.getInstance().getLocalTracks(limit, offset, null, Long.parseLong(album.getId()), null).subscribe(localTracks -> {
            Logger.d(TAG, "" + localTracks.size());
            for (LocalTrack localTrack : localTracks) {
                tracks.add(new TrackVM(activity, Track.from(localTrack)));
            }

        }, throwable -> {
            Logger.d(TAG, throwable.getLocalizedMessage());
        });
    }

    private void loadSpotifyTracks() {

        SpotifyApiManager.getInstance().getAlbumTracks(album.getId()).subscribe(spotifyResultAlbum -> {
            for (SpotifyTrack track : spotifyResultAlbum.getTracks()) {
                tracks.add(new TrackVM(activity, Track.from(track, album.getImage())));
            }
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());
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
        return false;
    }
}
