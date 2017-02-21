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
import com.joxad.zikobot.data.model.Artist;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager;
import com.joxad.zikobot.data.module.localmusic.model.LocalAlbum;
import com.joxad.zikobot.data.module.localmusic.model.LocalArtist;
import com.joxad.zikobot.data.module.localmusic.model.LocalTrack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func3;

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

    public String query = "";
    private Subscription subscription;

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
        if (query.length() < 2) {
            clearAll();
            notifyPropertyChanged(BR.showNoResult);
            return;
        }
        this.query = query;
        if (subscription != null)
            subscription.unsubscribe();

        subscription = Observable.zip(LocalMusicManager.getInstance().getLocalArtists(15, 0, query),
                LocalMusicManager.getInstance().getLocalAlbums(15, 0, null, query),
                LocalMusicManager.getInstance().getLocalTracks(10, 0, null, -1, query), new Func3<List<LocalArtist>, List<LocalAlbum>, List<LocalTrack>, Object>() {
                    @Override
                    public Object call(List<LocalArtist> localArtists, List<LocalAlbum> localAlbums, List<LocalTrack> localTracks) {
                        artists.clear();
                        for (LocalArtist localArtist : localArtists) {
                            artists.add(new ArtistVM(fragment.getContext(), Artist.from(localArtist)));
                        }
                        albums.clear();
                        for (LocalAlbum localAlbum : localAlbums) {
                            albums.add(new AlbumVM(fragment.getContext(), localAlbum));
                        }
                        tracks.clear();
                        for (LocalTrack localTrack : localTracks) {
                            tracks.add(new TrackVM(fragment.getContext(), Track.from(localTrack)));
                        }
                        return null;
                    }
                }).subscribe(o -> {
            notifyPropertyChanged(BR.showNoResult);
        }, throwable -> {
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
