package com.startogamu.musicalarm.viewmodel.fragment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.support.v4.app.Fragment;

import com.android.databinding.library.baseAdapters.BR;
import com.pixplicity.easyprefs.library.Prefs;
import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentSpotifyMusicBinding;
import com.startogamu.musicalarm.di.manager.spotify_api.SpotifyAPIManager;
import com.startogamu.musicalarm.model.spotify.Item;
import com.startogamu.musicalarm.model.spotify.SpotifyPlaylist;
import com.startogamu.musicalarm.utils.SpotifyPrefs;
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

    private Fragment fragment;
    private FragmentSpotifyMusicBinding binding;
    private ObservableArrayList<ItemPlaylistViewModel> userPlaylists;

    private ObservableArrayList<ItemPlaylistViewModel> topPlaylists;
    @Inject
    SpotifyAPIManager spotifyAPIManager;

    /***
     * View model use to get the playlist of the user
     *
     * @param fragment
     * @param binding
     */
    public SpotifyMusicViewModel(Fragment fragment, FragmentSpotifyMusicBinding binding) {
        userPlaylists = new ObservableArrayList<>();
        MusicAlarmApplication.get(fragment.getContext()).netComponent.inject(this);
        this.fragment = fragment;
        this.binding = binding;
        loadUserPlaylist();

    }

    private void loadUserPlaylist() {
        userPlaylists.clear();
        spotifyAPIManager.getUserPlaylists(Prefs.getString(SpotifyPrefs.ACCCES_TOKEN, ""), new Subscriber<SpotifyPlaylist>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SpotifyPlaylist spotifyPlaylist) {

                for (Item item : spotifyPlaylist.getItems()) {
                    ItemPlaylistViewModel itemPlaylistViewModel = new ItemPlaylistViewModel(fragment.getActivity(), item);
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
        topPlaylists.clear();
        spotifyAPIManager.getFeaturedPlaylists(new Subscriber<SpotifyPlaylist>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SpotifyPlaylist spotifyPlaylistWithTrack) {
                for (Item item : spotifyPlaylistWithTrack.getItems()) {
                    ItemPlaylistViewModel itemPlaylistViewModel = new ItemPlaylistViewModel(fragment.getActivity(), item);
                    topPlaylists.add(itemPlaylistViewModel);
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
    public ObservableArrayList<ItemPlaylistViewModel> getTopPlaylists() {
        return topPlaylists;
    }

    public ItemBinder<ItemPlaylistViewModel> itemUserPlaylistsBinder() {
        return new ItemBinderBase<>(BR.itemPlaylistViewModel, R.layout.item_playlist);
    }

    @Override
    public void onDestroy() {

    }
}
