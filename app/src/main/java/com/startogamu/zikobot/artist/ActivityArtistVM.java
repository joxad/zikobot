package com.startogamu.zikobot.artist;

import android.animation.ObjectAnimator;
import android.databinding.ObservableArrayList;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.LocalAlbumSelectEvent;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.databinding.ActivityArtistBinding;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.module.content_resolver.model.LocalTrack;
import com.startogamu.zikobot.module.zikobot.model.Artist;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.viewmodel.base.AlbumVM;
import com.startogamu.zikobot.viewmodel.base.TrackVM;
import com.startogamu.zikobot.viewmodel.custom.PlayerVM;

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


        ZikoUtils.prepareToolbar(activity,binding.customToolbar, artist.getName(),artist.getImage());

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
        loadLocalMusic();
        //TODO getinfos on the artist

    }


    @Subscribe
    public void onEvent(LocalAlbumSelectEvent localAlbumSelectEvent) {
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, localAlbumSelectEvent.getView(), activity.getString(R.string.transition));
        activity.startActivity(IntentManager.goToAlbum(localAlbumSelectEvent.getModel()), options.toBundle());
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
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
