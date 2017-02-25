package com.joxad.zikobot.app.search;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.album.AlbumVM;
import com.joxad.zikobot.app.artist.ArtistVM;
import com.joxad.zikobot.app.core.utils.ISearch;
import com.joxad.zikobot.app.databinding.FragmentSearchBinding;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.data.event.search.EventQueryChange;
import com.joxad.zikobot.data.model.Album;
import com.joxad.zikobot.data.model.Artist;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager;
import com.joxad.zikobot.data.module.localmusic.model.LocalAlbum;
import com.joxad.zikobot.data.module.localmusic.model.LocalArtist;
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Subscription;

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

    public String currentQuery = "";
    private Subscription artistSubscription, trackSubscription, albumSubscription, trackByArtistSubscription;

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
        query(SearchManager.QUERY);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
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


        if (currentQuery.equals("")) {
            currentQuery = query;
        } else {
            if (currentQuery.equals(query)) {
                return;
            } else {
                currentQuery = query;
            }
        }

        if (query.length() < 2) {
            clearAll();
            notifyPropertyChanged(BR.showNoResult);
            return;
        }
        if (artistSubscription != null)
            artistSubscription.unsubscribe();

        loadArtists(15, 0);
        loadAlbums(15, 0);
        loadTracks(15, 0);


    }

    public void loadArtists(final int limit, final int offset) {
        if (artistSubscription != null)
            artistSubscription.unsubscribe();
        artistSubscription = LocalMusicManager.getInstance().getLocalArtists(limit, offset, currentQuery).subscribe(localArtists -> {
            Logger.d(TAG, "" + localArtists.size());
            artists.clear();
            for (LocalArtist localArtist : localArtists) {
                artists.add(new ArtistVM(fragment.getContext(), Artist.from(localArtist)));
            }

            notifyPropertyChanged(BR.showNoResult);
        }, throwable -> {
            Logger.e(throwable.getMessage());
            artists.clear();

            notifyPropertyChanged(BR.showNoResult);
        });
    }

    public void loadAlbums(final int limit, final int offset) {
        if (albumSubscription != null)
            albumSubscription.unsubscribe();
        albumSubscription = LocalMusicManager.getInstance().getLocalAlbums(15, 0, null, currentQuery).subscribe(localArtists -> {
            Logger.d(TAG, "" + localArtists.size());
            albums.clear();
            for (LocalAlbum localAlbum : localArtists) {
                albums.add(new AlbumVM(fragment.getContext(), Album.from(localAlbum)));
            }

            notifyPropertyChanged(BR.showNoResult);
        }, throwable -> {
            Logger.e(throwable.getMessage());
            albums.clear();

            notifyPropertyChanged(BR.showNoResult);
        });
    }

    public void loadTracks(final int limit, final int offset) {
        if (trackSubscription != null)
            trackSubscription.unsubscribe();
        tracks.clear();

        trackSubscription = LocalMusicManager.getInstance().getLocalTracks(10, 0, null, -1, currentQuery).subscribe(localTracks -> {
            Logger.d("" + localTracks.size());
            for (LocalTrack localTrack : localTracks) {
                tracks.add(new TrackVM(fragment.getContext(), Track.from(localTrack)));
            }

            notifyPropertyChanged(BR.showNoResult);
        }, throwable -> {
            Logger.e(throwable.getMessage());

            notifyPropertyChanged(BR.showNoResult);
        });
        if (trackByArtistSubscription != null)
            trackByArtistSubscription.unsubscribe();

        trackByArtistSubscription = LocalMusicManager.getInstance().getLocalTracks(10, 0, currentQuery, -1, null).subscribe(localTracks -> {
            Logger.d("" + localTracks.size());
            for (LocalTrack localTrack : localTracks) {
                tracks.add(new TrackVM(fragment.getContext(), Track.from(localTrack)));
            }

            notifyPropertyChanged(BR.showNoResult);
        }, throwable -> {
            Logger.e(throwable.getMessage());
            notifyPropertyChanged(BR.showNoResult);
        });
    }


    private void clearAll() {
        artists.clear();
        albums.clear();
        tracks.clear();
    }

    @Bindable
    public boolean getShowNoResult() {
        return SearchManager.QUERY.length() > 2 && artists.isEmpty() && albums.isEmpty() && tracks.isEmpty();
    }
}
