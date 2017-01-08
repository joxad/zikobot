package com.startogamu.zikobot.album;

import android.databinding.ObservableArrayList;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.alarm.DialogFragmentSettings;
import com.startogamu.zikobot.core.event.dialog.EventShowDialogSettings;
import com.startogamu.zikobot.core.event.player.EventAddList;
import com.startogamu.zikobot.core.model.Album;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.core.module.localmusic.manager.LocalMusicManager;
import com.startogamu.zikobot.core.module.localmusic.model.LocalTrack;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.core.viewutils.EndlessRecyclerViewScrollListener;
import com.startogamu.zikobot.databinding.ActivityAlbumBinding;
import com.startogamu.zikobot.localtracks.TrackVM;
import com.startogamu.zikobot.player.PlayerVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 09/08/16.
 */
public class ActivityAlbumVM extends ActivityBaseVM<ActivityAlbum, ActivityAlbumBinding> {


    private static final String TAG = ActivityAlbumVM.class.getSimpleName();
    public ItemView itemViewTrack = ItemView.of(BR.trackVM, R.layout.item_track);

    public PlayerVM playerVM;
    public ObservableArrayList<TrackVM> tracks;

    public Album album;

    /***
     * @param activity
     * @param binding
     */
    public ActivityAlbumVM(ActivityAlbum activity, ActivityAlbumBinding binding) {
        super(activity, binding);
    }

    @Override
    public void onCreate() {
        album = Parcels.unwrap(activity.getIntent().getParcelableExtra(EXTRA.LOCAL_ALBUM));
        tracks = new ObservableArrayList<>();
        binding.rv.setLayoutManager(new GridLayoutManager(activity, 1));
        binding.rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(binding.rv.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadLocalMusic(15, totalItemsCount);
            }
        });
        initToolbar();
        initPlayerVM();
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        handler.postDelayed(() -> {
            ZikoUtils.animateScale(binding.fabPlay);
            ZikoUtils.animateFade(binding.customToolbar.rlOverlay);
            loadLocalMusic(15, 0);
        }, 400);

    }

    /***
     * Init the toolar
     */
    private void initToolbar() {
        ZikoUtils.prepareToolbar(activity, binding.customToolbar, album.getName(), album.getImage());
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
        LocalMusicManager.getInstance().getLocalTracks(limit, offset, null, album.getId(), null).subscribe(localTracks -> {
            Logger.d(TAG, "" + localTracks.size());
            for (LocalTrack localTrack : localTracks) {
                tracks.add(new TrackVM(activity, Track.from(localTrack)));
            }

        }, throwable -> {
            Logger.d(TAG, throwable.getLocalizedMessage());
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
