package com.startogamu.musicalarm.viewmodel.fragment;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.android.databinding.library.baseAdapters.BR;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentSpotifyMusicBinding;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.module.spotify_api.object.Item;
import com.startogamu.musicalarm.view.fragment.SpotifyMusicFragment;
import com.startogamu.musicalarm.viewmodel.ViewModel;
import com.startogamu.musicalarm.viewmodel.items.ItemPlaylistViewModel;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Subscription;

/**
 * Created by josh on 26/03/16.
 */
public class SpotifyMusicViewModel extends BaseObservable implements ViewModel {

    private SpotifyMusicFragment fragment;
    private FragmentSpotifyMusicBinding binding;
    public ObservableArrayList<ItemPlaylistViewModel> userPlaylists;
    public final ObservableBoolean showProgress = new ObservableBoolean(true);

    Subscription wsWatcherSubscription;

    public ItemView itemUserPlaylistsBinder = ItemView.of(BR.playlistViewModel, R.layout.item_playlist);

    /***
     * View model use to get the playlist of the user
     *
     * @param fragment
     * @param binding
     */
    public SpotifyMusicViewModel(SpotifyMusicFragment fragment, FragmentSpotifyMusicBinding binding) {
        userPlaylists = new ObservableArrayList<>();
        this.fragment = fragment;
        this.binding = binding;
        loadUserPlaylist();
    }


    /***
     * Call {@link com.startogamu.musicalarm.module.spotify_api.manager.SpotifyApiManager} to find the current user playlists
     */
    private void loadUserPlaylist() {
        userPlaylists.clear();
        Injector.INSTANCE.spotifyApi().manager().getUserPlaylists().subscribe(spotifyPlaylist -> {
            for (Item playlist : spotifyPlaylist.getItems()) {
                userPlaylists.add(new ItemPlaylistViewModel(fragment, playlist));
            }
        }, throwable -> {

        });
    }

    @Override
    public void onDestroy() {
        if (wsWatcherSubscription != null && !wsWatcherSubscription.isUnsubscribed())
            wsWatcherSubscription.unsubscribe();
    }
}
