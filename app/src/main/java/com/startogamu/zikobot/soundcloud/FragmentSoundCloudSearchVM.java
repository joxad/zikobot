package com.startogamu.zikobot.soundcloud;

import android.databinding.ObservableArrayList;

import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.search.EventQueryChange;
import com.startogamu.zikobot.core.utils.ISearch;
import com.startogamu.zikobot.databinding.FragmentSoundCloudSearchBinding;
import com.startogamu.zikobot.search.SearchManager;
import com.startogamu.zikobot.album.AlbumVM;
import com.startogamu.zikobot.artist.ArtistVM;
import com.startogamu.zikobot.localtracks.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 01/08/16.
 */
public class FragmentSoundCloudSearchVM extends FragmentBaseVM<FragmentSoundCloudSearch, FragmentSoundCloudSearchBinding> implements ISearch {

    private static final String TAG = FragmentSoundCloudSearchVM.class.getSimpleName();
    public ObservableArrayList<ArtistVM> artists;
    public ObservableArrayList<AlbumVM> albums;
    public ObservableArrayList<TrackVM> tracks;
    public ItemView itemViewArtist = ItemView.of(BR.artistVM, R.layout.item_artist);
    public ItemView itemViewAlbum = ItemView.of(BR.albumVM, R.layout.item_album);
    public ItemView itemViewTrack = ItemView.of(BR.trackVM, R.layout.item_track);

    /***
     * @param fragment
     * @param binding
     */
    public FragmentSoundCloudSearchVM(FragmentSoundCloudSearch fragment, FragmentSoundCloudSearchBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void init() {
        artists = new ObservableArrayList<>();
        albums = new ObservableArrayList<>();
        tracks = new ObservableArrayList<>();
        binding.rvAlbums.setNestedScrollingEnabled(false);
        binding.rvTracks.setNestedScrollingEnabled(false);
        binding.rvArtists.setNestedScrollingEnabled(false);
    }


    @Override
    protected void onResume() {
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
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void query(String query) {

    }

}
