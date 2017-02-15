package com.joxad.zikobot.app.artist;

import android.databinding.ObservableArrayList;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.alarm.DialogFragmentSettings;
import com.joxad.zikobot.app.album.AlbumVM;
import com.joxad.zikobot.app.core.fragmentmanager.IntentManager;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.core.utils.ZikoUtils;
import com.joxad.zikobot.app.databinding.ActivityArtistBinding;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.player.PlayerVM;
import com.joxad.zikobot.app.player.event.EventAddList;
import com.joxad.zikobot.app.soundcloud.SoundCloudPlaylistVM;
import com.joxad.zikobot.data.event.LocalAlbumSelectEvent;
import com.joxad.zikobot.data.event.dialog.EventShowDialogSettings;
import com.joxad.zikobot.data.model.Artist;
import com.joxad.zikobot.data.model.TYPE;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager;
import com.joxad.zikobot.data.module.localmusic.model.LocalAlbum;
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;
import com.joxad.zikobot.data.module.soundcloud.manager.SoundCloudApiManager;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 08/08/16.
 */
public class ActivityArtistVM extends ActivityBaseVM<ActivityArtist, ActivityArtistBinding> {

    public ItemView itemViewAlbum = ItemView.of(BR.albumVM, R.layout.item_album);
    public ItemView itemViewTrack = ItemView.of(BR.trackVM, R.layout.item_track);
    public ItemView itemViewSCPlaylist = ItemView.of(BR.playlistVM, R.layout.item_soundcloud_playlist);

    public PlayerVM playerVM;
    public ObservableArrayList<AlbumVM> albums;
    public ObservableArrayList<SoundCloudPlaylistVM> soundCloudPlaylistVMs;
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
        albums = new ObservableArrayList<>();
        tracks = new ObservableArrayList<>();
        soundCloudPlaylistVMs = new ObservableArrayList<>();
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
            switch (artist.getType()) {
                case TYPE.DEEZER:
                    break;
                case TYPE.SOUNDCLOUD:
                    loadSoundCloudData();
                    break;
                case TYPE.LOCAL:
                    loadLocalData();
                    break;
                default:
                    break;
            }

            loaded = true;
        }, 400);
    }

    private void loadSoundCloudData() {
        loadSoundCloudPlaylist();
    }

    private void loadLocalData() {
        loadLocalAlbums(15, 0);
        loadLocalTracks(15, 0);
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
    private void loadSoundCloudPlaylist() {
        SoundCloudApiManager.getInstance().userPlaylists(artist.getId()).subscribe(soundCloudPlaylists -> {
            for (SoundCloudPlaylist soundCloudPlaylist : soundCloudPlaylists) {
                soundCloudPlaylistVMs.add(new SoundCloudPlaylistVM(activity, soundCloudPlaylist));
            }
        }, throwable -> Logger.e(throwable.getLocalizedMessage()));
    }

    /***
     * Load the local music
     */
    private void loadLocalAlbums(int limit, int offset) {
        LocalMusicManager.getInstance().getLocalAlbums(limit, offset, artist != null ? artist.getName() : null, null).subscribe(localAlbums -> {
            for (LocalAlbum localAlbum : localAlbums) {
                albums.add(new AlbumVM(activity, localAlbum));
            }
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());
        });
    }

    private void loadLocalTracks(int limit, int offset) {
        LocalMusicManager.getInstance().getLocalTracks(limit, offset, artist != null ? artist.getName() : null, -1, null).subscribe(localTracks -> {
            for (LocalTrack localTrack : localTracks) {
                tracks.add(new TrackVM(activity, Track.from(localTrack)));
            }

        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());
        });
    }

    public void onPlay(@SuppressWarnings("unused") View view) {
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
