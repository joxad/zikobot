package com.joxad.zikobot.app.spotify;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.album.AlbumVM;
import com.joxad.zikobot.app.artist.ArtistVM;
import com.joxad.zikobot.app.core.utils.ISearch;
import com.joxad.zikobot.app.databinding.FragmentSpotifySearchBinding;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.search.SearchManager;
import com.joxad.zikobot.data.event.search.EventQueryChange;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.spotify_api.manager.SpotifyApiManager;
import com.joxad.zikobot.data.module.spotify_api.model.SpotifyTrack;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Subscription;

/**
 * Created by josh on 01/08/16.
 */
public class FragmentSpotifySearchVM extends FragmentBaseVM<FragmentSpotifySearch, FragmentSpotifySearchBinding> implements ISearch {

    private static final String TAG = FragmentSpotifySearchVM.class.getSimpleName();
    public ObservableArrayList<ArtistVM> artists;
    public ObservableArrayList<AlbumVM> albums;
    public ObservableArrayList<TrackVM> tracks;
    public ItemView itemViewArtist = ItemView.of(BR.artistVM, R.layout.item_artist);
    public ItemView itemViewAlbum = ItemView.of(BR.albumVM, R.layout.item_album);
    public ItemView itemViewTrack = ItemView.of(BR.trackVM, R.layout.item_track);

    private Subscription trackSubscription;
    private String currentQuery;

    /***
     * @param fragment
     * @param binding
     */
    public FragmentSpotifySearchVM(FragmentSpotifySearch fragment, FragmentSpotifySearchBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void onCreate() {
        currentQuery = "";
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
        if (trackSubscription != null) {
            trackSubscription.unsubscribe();
        }
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
            tracks.clear();
            notifyPropertyChanged(BR.showNoResult);
            return;
        }

        trackSubscription = SpotifyApiManager.getInstance().search(10, 0, query).subscribe(spotifySearchResult -> {
            tracks.clear();
            for (SpotifyTrack item : spotifySearchResult.tracks.getItems()) {
                tracks.add(new TrackVM(fragment.getContext(), Track.from(item)));
            }
            notifyPropertyChanged(BR.showNoResult);
        }, throwable -> {
            notifyPropertyChanged(BR.showNoResult);
            Logger.d(throwable.getMessage());
        });
    }

    @Bindable
    public boolean getShowNoResult() {
        return SearchManager.QUERY.length() > 2 && artists.isEmpty() && albums.isEmpty() && tracks.isEmpty();
    }

}
