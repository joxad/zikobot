package com.startogamu.zikobot.album;

import android.databinding.ObservableArrayList;
import android.view.View;

import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.databinding.ActivityAlbumBinding;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.content_resolver.model.LocalTrack;
import com.startogamu.zikobot.module.zikobot.model.Album;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.viewmodel.base.TrackVM;
import com.startogamu.zikobot.viewmodel.custom.PlayerVM;

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
    public void init() {
        album = Parcels.unwrap(activity.getIntent().getParcelableExtra(EXTRA.LOCAL_ALBUM));
        tracks = new ObservableArrayList<>();
        binding.rv.setNestedScrollingEnabled(false);
        initToolbar();
        initPlayerVM();
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
    protected void onResume() {
        super.onResume();
        playerVM.onResume();
        loadLocalMusic();
        //TODO getinfos on the album
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
        tracks.clear();
        Injector.INSTANCE.contentResolverComponent().localMusicManager().getLocalTracks(null, album.getId(), null).subscribe(localTracks -> {
            Logger.d(TAG, "" + localTracks.size());
            for (LocalTrack localTrack : localTracks) {
                tracks.add(new TrackVM(activity, Track.from(localTrack)));
            }

        }, throwable -> {
            Logger.d(TAG, throwable.getLocalizedMessage());
        });
    }

    public void onPlay(View view) {

    }

    @Override
    public void destroy() {

    }
}
