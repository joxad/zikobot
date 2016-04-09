package com.startogamu.musicalarm.viewmodel.fragment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.android.databinding.library.baseAdapters.BR;
import com.raizlabs.android.dbflow.converter.BooleanConverter;
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

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func2;

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
    public final ObservableBoolean showProgress = new ObservableBoolean(true);

    Observable<SpotifyPlaylist> playlistObservable;
    Observable<SpotifyFeaturedPlaylist> featuredPlaylistObservable;
    Subscription wsWatcherSubscription;

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
        watchWS();
    }

    private void watchWS() {
        wsWatcherSubscription = Observable.combineLatest(featuredPlaylistObservable, playlistObservable,
                (SpotifyFeaturedPlaylist spotifyFeaturePlaylist, SpotifyPlaylist spotifyPlaylist) -> {

                    if (spotifyPlaylist != null) {
                        for (Item item : spotifyPlaylist.getItems()) {
                            ItemPlaylistViewModel itemPlaylistViewModel = new ItemPlaylistViewModel(fragment, item);
                            userPlaylists.add(itemPlaylistViewModel);
                        }
                    }

                    if (spotifyFeaturePlaylist != null) {
                        for (Item item : spotifyFeaturePlaylist.getSpotifyPlaylist().getItems()) {
                            ItemPlaylistViewModel itemPlaylistViewModel = new ItemPlaylistViewModel(fragment, item);
                            featuredPlaylists.add(itemPlaylistViewModel);
                        }
                    }
                    return (spotifyFeaturePlaylist == null && spotifyPlaylist == null);
                }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                showProgress.set(aBoolean);
            }
        });
    }

    /***
     * Call {@link SpotifyAPIManager} to find the current user playlists
     */
    private void loadUserPlaylist() {
        userPlaylists.clear();
        playlistObservable = spotifyAPIManager.getUserPlaylists();
    }

    /***
     * This will call spotify api to get the top plyalist
     * and put it in the horizontal recyclerview on the top
     */
    public void loadTopPlaylists() {
        featuredPlaylists.clear();
        featuredPlaylistObservable = spotifyAPIManager.getFeaturedPlaylists();
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
        if (wsWatcherSubscription != null && !wsWatcherSubscription.isUnsubscribed())
            wsWatcherSubscription.unsubscribe();
    }
}
