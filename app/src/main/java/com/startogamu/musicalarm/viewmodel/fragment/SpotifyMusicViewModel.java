package com.startogamu.musicalarm.viewmodel.fragment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.android.databinding.library.baseAdapters.BR;
import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentSpotifyMusicBinding;
import com.startogamu.musicalarm.di.manager.spotify_api.SpotifyAPIManager;
import com.startogamu.musicalarm.model.spotify.Item;
import com.startogamu.musicalarm.model.spotify.SpotifyFeaturedPlaylist;
import com.startogamu.musicalarm.model.spotify.SpotifyPlaylist;
import com.startogamu.musicalarm.view.fragment.SpotifyMusicFragment;
import com.startogamu.musicalarm.viewmodel.ViewModel;
import com.startogamu.musicalarm.viewmodel.items.ItemPlaylistViewModel;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by josh on 26/03/16.
 */
public class SpotifyMusicViewModel extends BaseObservable implements ViewModel {

    private SpotifyMusicFragment fragment;
    private FragmentSpotifyMusicBinding binding;
    private ObservableArrayList<ItemPlaylistViewModel> userPlaylists;

    private ObservableArrayList<ItemPlaylistViewModel> featuredPlaylists;
    @Inject
    SpotifyAPIManager spotifyAPIManager;

    /***
     * View model use to get the playlist of the user
     *
     * @param fragment
     * @param binding
     */
    public SpotifyMusicViewModel(SpotifyMusicFragment fragment, FragmentSpotifyMusicBinding binding) {
        userPlaylists = new ObservableArrayList<>();
        featuredPlaylists = new ObservableArrayList<>();
        MusicAlarmApplication.get(fragment.getActivity()).netComponent.inject(this);
        this.fragment = fragment;
        this.binding = binding;
        loadUserPlaylist();
        loadTopPlaylists();
    }

    /***
     * Call {@link SpotifyAPIManager} to find the current user playlists
     */
    private void loadUserPlaylist() {
        userPlaylists.clear();
        spotifyAPIManager.getUserPlaylists(new Subscriber<SpotifyPlaylist>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SpotifyPlaylist spotifyPlaylist) {

                for (Item item : spotifyPlaylist.getItems()) {
                    ItemPlaylistViewModel itemPlaylistViewModel = new ItemPlaylistViewModel(fragment, item);
                    userPlaylists.add(itemPlaylistViewModel);
                }
            }
        });
    }

    /***
     * This will call spotify api to get the top plyalist
     * and put it in the horizontal recyclerview on the top
     */
    public void loadTopPlaylists() {
        featuredPlaylists.clear();
        spotifyAPIManager.getFeaturedPlaylists(new Subscriber<SpotifyFeaturedPlaylist>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SpotifyFeaturedPlaylist spotifyFeaturedPlaylist) {
                for (Item item : spotifyFeaturedPlaylist.getSpotifyPlaylist().getItems()) {
                    ItemPlaylistViewModel itemPlaylistViewModel = new ItemPlaylistViewModel(fragment, item);
                    featuredPlaylists.add(itemPlaylistViewModel);
                }
            }
        });
    }


    @Bindable
    public ObservableArrayList<ItemPlaylistViewModel> getUserPlaylists() {
        return userPlaylists;
    }

    public ItemBinder<ItemPlaylistViewModel> itemTopPlaylistBinder() {
        return new ItemBinderBase<>(BR.itemPlaylistViewModel, R.layout.item_top_playlist);
    }

    @Bindable
    public ObservableArrayList<ItemPlaylistViewModel> getFeaturedPlaylists() {
        return featuredPlaylists;
    }

    public ItemBinder<ItemPlaylistViewModel> itemUserPlaylistsBinder() {
        return new ItemBinderBase<>(BR.itemPlaylistViewModel, R.layout.item_playlist);
    }

    @Override
    public void onDestroy() {

    }
}
