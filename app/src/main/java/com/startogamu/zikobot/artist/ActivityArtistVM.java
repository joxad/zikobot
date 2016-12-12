package com.startogamu.zikobot.artist;

import android.databinding.ObservableArrayList;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.LocalAlbumSelectEvent;
import com.startogamu.zikobot.core.event.dialog.EventShowDialogAlarm;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.core.viewutils.EndlessRecyclerViewScrollListener;
import com.startogamu.zikobot.databinding.ActivityArtistBinding;

import com.startogamu.zikobot.core.module.localmusic.manager.LocalMusicManager;
import com.startogamu.zikobot.core.module.localmusic.model.LocalAlbum;
import com.startogamu.zikobot.core.module.localmusic.model.LocalTrack;
import com.startogamu.zikobot.core.model.Artist;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.alarm.DialogFragmentAlarms;
import com.startogamu.zikobot.album.AlbumVM;
import com.startogamu.zikobot.localtracks.TrackVM;
import com.startogamu.zikobot.player.PlayerVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 08/08/16.
 */
public class ActivityArtistVM extends ActivityBaseVM<ActivityArtist, ActivityArtistBinding> {

    private static final String TAG = ActivityArtistVM.class.getSimpleName();
    public ItemView itemViewAlbum = ItemView.of(BR.albumVM, R.layout.item_album);
    public ItemView itemViewTrack = ItemView.of(BR.trackVM, R.layout.item_track);

    public PlayerVM playerVM;
    public ObservableArrayList<AlbumVM> albums;
    public ObservableArrayList<TrackVM> tracks;

    Artist artist;

    /***
     * @param activity
     * @param binding
     */
    public ActivityArtistVM(ActivityArtist activity, ActivityArtistBinding binding) {
        super(activity, binding);
    }

    @Override
    public void onCreate() {
        artist = Parcels.unwrap(activity.getIntent().getParcelableExtra(EXTRA.LOCAL_ARTIST));
        albums = new ObservableArrayList<>();
        tracks = new ObservableArrayList<>();

        binding.rv.setNestedScrollingEnabled(false);
        binding.rv.setLayoutManager(new GridLayoutManager(activity, 2));
        binding.rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(binding.rv.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadLocalAlbums(15, totalItemsCount);
            }
        });

        binding.rvTracks.setNestedScrollingEnabled(false);
        binding.rvTracks.setLayoutManager(new GridLayoutManager(activity, 1));
        binding.rvTracks.addOnScrollListener(new EndlessRecyclerViewScrollListener(binding.rvTracks.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadLocalTracks(15, totalItemsCount);
            }
        });
        initToolbar();
        initPlayerVM();

    }

    /***
     * Init the toolar
     */
    private void initToolbar() {
        ZikoUtils.prepareToolbar(activity, binding.customToolbar, artist.getName(), artist.getImage());
        ZikoUtils.animateScale(binding.fabPlay);
    }


    /***
     *
     */

    private void initPlayerVM() {
        playerVM = new PlayerVM(activity);
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        playerVM.onResume();
        albums.clear();
        tracks.clear();
        loadLocalAlbums(15,0);
        loadLocalTracks(15,0);
        //TODO getinfos on the artist

    }


    @Subscribe
    public void onEvent(LocalAlbumSelectEvent localAlbumSelectEvent) {
        activity.startActivity(IntentManager.goToAlbum(localAlbumSelectEvent.getModel()));
    }


    @Subscribe
    public void onEvent(EventShowDialogAlarm event) {
        DialogFragmentAlarms dialogFragmentAlarms = DialogFragmentAlarms.newInstance(event.getModel());
        dialogFragmentAlarms.show(activity.getSupportFragmentManager(), DialogFragmentAlarms.TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        playerVM.onPause();
    }

    /***
     * Load the local music
     */
    public void loadLocalAlbums(int limit, int offset) {
        LocalMusicManager.getInstance().getLocalAlbums(limit, offset, artist != null ? artist.getName() : null, null).subscribe(localAlbums -> {
            Log.d(TAG, "" + localAlbums.size());
            for (LocalAlbum localAlbum : localAlbums) {
                albums.add(new AlbumVM(activity, localAlbum));
            }

        }, throwable -> {

        });
    }

    public void loadLocalTracks(int limit, int offset) {
        LocalMusicManager.getInstance().getLocalTracks(limit, offset, artist != null ? artist.getName() : null, -1, null).subscribe(localTracks -> {
            Log.d(TAG, "" + localTracks.size());
            for (LocalTrack localTrack : localTracks) {
                tracks.add(new TrackVM(activity, Track.from(localTrack)));
            }

        }, throwable -> {

        });
    }

    public void onPlay(View view) {
        EventBus.getDefault().post(new EventAddTrackToPlayer(tracks));
    }

    @Override
    protected boolean onBackPressed() {

        return super.onBackPressed();
    }

}
