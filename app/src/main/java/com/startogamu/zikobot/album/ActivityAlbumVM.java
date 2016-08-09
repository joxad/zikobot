package com.startogamu.zikobot.album;

import android.databinding.ObservableArrayList;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.artist.ActivityArtistVM;
import com.startogamu.zikobot.databinding.ActivityAlbumBinding;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.content_resolver.model.LocalAlbum;
import com.startogamu.zikobot.module.content_resolver.model.LocalTrack;
import com.startogamu.zikobot.module.lyrics.model.Album;
import com.startogamu.zikobot.module.zikobot.model.Artist;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.viewmodel.base.AlbumVM;
import com.startogamu.zikobot.viewmodel.base.TrackVM;
import com.startogamu.zikobot.viewmodel.custom.PlayerVM;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 09/08/16.
 */
public class ActivityAlbumVM extends ActivityBaseVM<ActivityAlbum, ActivityAlbumBinding> {


    private static final String TAG = ActivityAlbumVM.class.getSimpleName();
    public ItemView itemViewTrack = ItemView.of(BR.trackVM, R.layout.item_track);

    public PlayerVM playerVM;
    public ObservableArrayList<TrackVM> tracks;




    /***
     * @param activity
     * @param binding
     */
    public ActivityAlbumVM(ActivityAlbum activity, ActivityAlbumBinding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {
        tracks = new ObservableArrayList<>();

        binding.rv.setNestedScrollingEnabled(false);
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

     //   binding.mainCollapsing.setTitle(artist.getName());
        binding.title.setText("");
        binding.rlToolbarImage.setVisibility(View.VISIBLE);
      //  Glide.with(activity).load(artist.getImage()).placeholder(R.drawable.ic_vinyl).into(binding.ivToolbar);
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
        Injector.INSTANCE.contentResolverComponent().localMusicManager().getLocalTracks(null, -1, null).subscribe(localTracks -> {
            Log.d(TAG, "" + localTracks.size());
            for (LocalTrack localTrack : localTracks) {
                tracks.add(new TrackVM(activity, Track.from(localTrack)));
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
