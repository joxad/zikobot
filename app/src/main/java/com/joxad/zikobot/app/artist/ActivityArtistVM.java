package com.joxad.zikobot.app.artist;

import android.databinding.ObservableArrayList;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.zikobot.app.alarm.DialogFragmentSettings;
import com.joxad.zikobot.app.album.AlbumVM;
import com.joxad.zikobot.app.core.event.LocalAlbumSelectEvent;
import com.joxad.zikobot.app.core.event.player.EventAddList;
import com.joxad.zikobot.app.core.fragmentmanager.IntentManager;
import com.joxad.zikobot.app.core.module.localmusic.manager.LocalMusicManager;
import com.joxad.zikobot.app.core.module.localmusic.model.LocalAlbum;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.event.dialog.EventShowDialogSettings;
import com.joxad.zikobot.app.core.model.Artist;
import com.joxad.zikobot.app.core.model.Track;
import com.joxad.zikobot.app.core.module.localmusic.model.LocalTrack;
import com.joxad.zikobot.app.core.utils.ZikoUtils;
import com.joxad.zikobot.app.databinding.ActivityArtistBinding;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.player.PlayerVM;

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
    boolean loaded = false;

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
        binding.rvTracks.setLayoutManager(new GridLayoutManager(activity, 1));
        binding.rv.setLayoutManager(new GridLayoutManager(activity, 2));
        albums = new ObservableArrayList<>();
        tracks = new ObservableArrayList<>();
        initToolbar();
        initPlayerVM();

    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        if (loaded) return;
        handler.postDelayed(() -> {
            ZikoUtils.animateScale(binding.fabPlay);
            ZikoUtils.animateFade(binding.customToolbar.rlOverlay);
            loadLocalAlbums(15, 0);
            loadLocalTracks(15, 0);
            loaded = true;
        }, 400);
    }

    /***
     * Init the toolar
     */
    private void initToolbar() {
        ZikoUtils.prepareToolbar(activity, binding.customToolbar, artist.getName(), artist.getImage());
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
    public void onEvent(LocalAlbumSelectEvent localAlbumSelectEvent) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity, localAlbumSelectEvent.getView(), activity.getString(R.string.transition));
        activity.startActivity(IntentManager.goToAlbum(localAlbumSelectEvent.getModel()), options.toBundle());
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
     */
    private void loadLocalAlbums(int limit, int offset) {
        LocalMusicManager.getInstance().getLocalAlbums(limit, offset, artist != null ? artist.getName() : null, null).subscribe(localAlbums -> {
            Log.d(TAG, "" + localAlbums.size());
            for (LocalAlbum localAlbum : localAlbums) {
                albums.add(new AlbumVM(activity, localAlbum));
            }

        }, throwable -> {

        });
    }

    private void loadLocalTracks(int limit, int offset) {
        LocalMusicManager.getInstance().getLocalTracks(limit, offset, artist != null ? artist.getName() : null, -1, null).subscribe(localTracks -> {
            Log.d(TAG, "" + localTracks.size());
            for (LocalTrack localTrack : localTracks) {
                tracks.add(new TrackVM(activity, Track.from(localTrack)));
            }

        }, throwable -> {

        });
    }

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
