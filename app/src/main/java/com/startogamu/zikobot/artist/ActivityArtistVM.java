package com.startogamu.zikobot.artist;

import android.databinding.ObservableArrayList;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.ActivityArtistBinding;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.module.content_resolver.model.LocalTrack;
import com.startogamu.zikobot.module.zikobot.model.Artist;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.viewmodel.base.AlbumVM;
import com.startogamu.zikobot.viewmodel.base.TrackVM;
import com.startogamu.zikobot.viewmodel.custom.PlayerVM;

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
    public void init() {
        artist = Parcels.unwrap(activity.getIntent().getParcelableExtra(EXTRA.LOCAL_ARTIST));
        albums = new ObservableArrayList<>();
        tracks = new ObservableArrayList<>();

        binding.rv.setNestedScrollingEnabled(false);
        binding.rvTracks.setNestedScrollingEnabled(false);
        initToolbar();
        initPlayerVM();
    }

    /***
     * Init the toolar
     */
    private void initToolbar() {
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        binding.mainCollapsing.setTitle(artist.getName());
        binding.title.setText("");
        binding.rlToolbarImage.setVisibility(View.VISIBLE);
        Glide.with(activity).load(artist.getImage()).placeholder(R.drawable.ic_vinyl).into(binding.ivToolbar);
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
        playerVM.onResume();
        loadLocalMusic();
        //TODO getinfos on the artist

    }

    @Override
    protected void onPause() {
        super.onPause();
        playerVM.onPause();
    }

    /***
     * Load the local music
     */
    public void loadLocalMusic() {
        albums.clear();
        tracks.clear();
        Injector.INSTANCE.contentResolverComponent().localMusicManager().getLocalTracks(artist != null ? artist.getName() : null, -1, null).subscribe(localTracks -> {
            Log.d(TAG, "" + localTracks.size());
            for (LocalTrack localTrack : localTracks) {
                tracks.add(new TrackVM(activity, Track.from(localTrack)));
            }

        }, throwable -> {

        });
        Injector.INSTANCE.contentResolverComponent().localMusicManager().getLocalAlbums(artist != null ? artist.getName() : null, null).subscribe(localAlbums -> {
            Log.d(TAG, "" + localAlbums.size());
            for (LocalAlbum localAlbum : localAlbums) {
                albums.add(new AlbumVM(activity, localAlbum));
            }

        }, throwable -> {

        });
    }

    public void onPlay(View view) {

    }

    @Override
    public void destroy() {

    }
}
