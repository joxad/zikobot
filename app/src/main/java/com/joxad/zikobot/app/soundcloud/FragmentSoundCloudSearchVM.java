package com.joxad.zikobot.app.soundcloud;

import android.databinding.ObservableArrayList;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.album.AlbumVM;
import com.joxad.zikobot.app.artist.ArtistVM;
import com.joxad.zikobot.app.core.utils.ISearch;
import com.joxad.zikobot.app.databinding.FragmentSoundCloudSearchBinding;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.app.search.SearchManager;
import com.joxad.zikobot.data.event.search.EventQueryChange;
import com.joxad.zikobot.data.model.Artist;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.soundcloud.manager.SoundCloudApiManager;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudTrack;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudUser;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Subscription;

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

    Subscription artistSubscription, trackSubscription, playlistSubscription;

    /***
     * @param fragment
     * @param binding
     */
    public FragmentSoundCloudSearchVM(FragmentSoundCloudSearch fragment, FragmentSoundCloudSearchBinding binding) {
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
        if (SearchManager.QUERY.length() > 2)
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

        if (artistSubscription != null)
            artistSubscription.unsubscribe();
        if (trackSubscription != null)
            trackSubscription.unsubscribe();
        if (playlistSubscription != null)
            playlistSubscription.unsubscribe();
        if (query.length()<2) {
            tracks.clear();
            albums.clear();
            artists.clear();
            return;
        }
        trackSubscription = SoundCloudApiManager.getInstance().search(query).subscribe(soundCloudTracks -> {
            tracks.clear();

            for (SoundCloudTrack soundCloudTrack : soundCloudTracks) {
                tracks.add(new TrackVM(fragment.getContext(), Track.from(soundCloudTrack, fragment.getString(R.string.soundcloud_id))));
            }
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());
        });

        playlistSubscription = SoundCloudApiManager.getInstance().searchPlaylists(query).subscribe(soundCloudTracks -> {
            for (SoundCloudPlaylist soundCloudTrack : soundCloudTracks) {
                //albums.add(new AlbumVM(fragment.getContext(), Album.from(, fragment.getString(R.string.soundcloud_id))));
            }
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());

        });


        artistSubscription = SoundCloudApiManager.getInstance().searchUsers(query).subscribe(soundCloudUsers -> {
            artists.clear();

            for (SoundCloudUser soundCloudUser : soundCloudUsers) {
                artists.add(new ArtistVM(fragment.getContext(), Artist.from(soundCloudUser)));
            }
        }, throwable -> {
            Logger.e(throwable.getLocalizedMessage());

        });
    }

}
