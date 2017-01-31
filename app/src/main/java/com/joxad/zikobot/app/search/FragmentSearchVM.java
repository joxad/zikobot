package com.joxad.zikobot.app.search;

import android.databinding.ObservableArrayList;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.album.AlbumVM;
import com.joxad.zikobot.app.artist.ArtistVM;
import com.joxad.zikobot.data.event.search.EventQueryChange;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager;
import com.joxad.zikobot.data.module.localmusic.model.LocalAlbum;
import com.joxad.zikobot.data.module.localmusic.model.LocalArtist;
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.orhanobut.logger.Logger;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.utils.ISearch;
import com.joxad.zikobot.app.databinding.FragmentSearchBinding;

import com.joxad.zikobot.data.model.Artist;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 01/08/16.
 */
public class FragmentSearchVM extends FragmentBaseVM<FragmentSearch, FragmentSearchBinding> implements ISearch {

    private static final String TAG = FragmentSearchVM.class.getSimpleName();
    public ObservableArrayList<ArtistVM> artists;
    public ObservableArrayList<AlbumVM> albums;
    public ObservableArrayList<TrackVM> tracks;
    public ItemView itemViewArtist = ItemView.of(BR.artistVM, R.layout.item_artist);
    public ItemView itemViewAlbum = ItemView.of(BR.albumVM, R.layout.item_album);
    public ItemView itemViewTrack = ItemView.of(BR.trackVM, R.layout.item_track);

    public String query="";
    /***
     * @param fragment
     * @param binding
     */
    public FragmentSearchVM(FragmentSearch fragment, FragmentSearchBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void onCreate() {
        artists = new ObservableArrayList<>();
        albums = new ObservableArrayList<>();
        tracks = new ObservableArrayList<>();
        binding.rvAlbums.setNestedScrollingEnabled(false);
        binding.rvTracks.setNestedScrollingEnabled(false);
        binding.rvArtists.setNestedScrollingEnabled(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        if (!SearchManager.QUERY.isEmpty()) {
            query(SearchManager.QUERY);
        }
    }


    @Subscribe
    public void onReceive(EventQueryChange event) {
        query(event.getQuery());
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void query(String query) {
        if( query.length()<2) {
            clearAll();
            return;
        }
        this.query = query;
        loadArtists(15,0);
        loadAlbums(15,0);
        loadTracks(15,0);


    }

    public void loadArtists(final int limit, final int offset){
        LocalMusicManager.getInstance().getLocalArtists(15,0,query).subscribe(localArtists -> {
            Logger.d(TAG, "" + localArtists.size());
            artists.clear();
            for (LocalArtist localArtist : localArtists) {
                artists.add(new ArtistVM(fragment.getContext(), Artist.from(localArtist)));
            }

        }, throwable -> {
            Logger.e(throwable.getMessage());
            artists.clear();

        });
    }

    public void loadAlbums(final int limit, final int offset){
        LocalMusicManager.getInstance().getLocalAlbums(15,0,null, query).subscribe(localArtists -> {
            Logger.d(TAG, "" + localArtists.size());
            albums.clear();
            for (LocalAlbum localAlbum : localArtists) {
                albums.add(new AlbumVM(fragment.getContext(), localAlbum));
            }

        }, throwable -> {
            Logger.e(throwable.getMessage());
            albums.clear();
        });
    }

    public void loadTracks(final int limit, final int offset){
        LocalMusicManager.getInstance().getLocalTracks(10,0,null,-1, query).subscribe(localTracks -> {
            Logger.d("" + localTracks.size());
            tracks.clear();
            for (LocalTrack localTrack : localTracks) {
                tracks.add(new TrackVM(fragment.getContext(), Track.from(localTrack)));
            }
        }, throwable -> {
            Logger.e(throwable.getMessage());
            tracks.clear();
        });
    }

    private void clearAll() {
        artists.clear();
        albums.clear();
        tracks.clear();
    }

}
