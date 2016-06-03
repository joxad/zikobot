package com.startogamu.musicalarm.viewmodel.fragment;

import android.databinding.ObservableArrayList;
import android.support.design.widget.Snackbar;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentSpotifyMusicBinding;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.module.mock.Mock;
import com.startogamu.musicalarm.module.spotify_api.model.Item;
import com.startogamu.musicalarm.view.fragment.SpotifyMusicFragment;
import com.startogamu.musicalarm.viewmodel.items.ItemPlaylistViewModel;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Subscription;

/**
 * Created by josh on 26/03/16.
 */
public class SpotifyMusicViewModel extends FragmentBaseVM<SpotifyMusicFragment, FragmentSpotifyMusicBinding> {

    public ObservableArrayList<ItemPlaylistViewModel> userPlaylists;

    Subscription wsWatcherSubscription;

    public ItemView itemUserPlaylistsBinder = ItemView.of(BR.playlistViewModel, R.layout.item_playlist);

    /***
     * View model use to get the playlist of the user
     *
     * @param fragment
     * @param binding
     */
    public SpotifyMusicViewModel(SpotifyMusicFragment fragment, FragmentSpotifyMusicBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void init() {
        userPlaylists = new ObservableArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserPlaylist();
    }

    /***
     * Call {@link com.startogamu.musicalarm.module.spotify_api.manager.SpotifyApiManager} to find the current user playlists
     */
    private void loadUserPlaylist() {
        userPlaylists.clear();
        userPlaylists.addAll(Mock.playlists(fragment.getContext()));
        Injector.INSTANCE.spotifyApi().manager().getUserPlaylists().subscribe(spotifyPlaylist -> {
            userPlaylists.clear();
            for (Item playlist : spotifyPlaylist.getItems()) {
                userPlaylists.add(new ItemPlaylistViewModel(fragment.getContext(), playlist));
            }
        }, throwable -> {
            Snackbar.make(binding.getRoot(), throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
        });
    }


    @Override
    public void destroy() {
        if (wsWatcherSubscription != null && !wsWatcherSubscription.isUnsubscribed())
            wsWatcherSubscription.unsubscribe();
    }
}
