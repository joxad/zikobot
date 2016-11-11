package com.startogamu.zikobot.spotify;

import android.databinding.ObservableArrayList;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.databinding.FragmentSpotifyPlaylistsBinding;

import com.startogamu.zikobot.core.module.mock.Mock;
import com.startogamu.zikobot.core.module.spotify_api.manager.SpotifyApiManager;
import com.startogamu.zikobot.core.module.spotify_api.model.Item;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 26/03/16.
 */
public class FragmentSpotifyPlaylistsVM extends FragmentBaseVM<FragmentSpotifyPlaylists, FragmentSpotifyPlaylistsBinding> {

    private static final String TAG = "FragmentSpotifyPlaylists";
    public ObservableArrayList<ItemPlaylistViewModel> userPlaylists;
    public ItemView itemPlaylist = ItemView.of(BR.playlistVM, R.layout.item_playlist);

    /***
     * View model use to get the playlist of the user
     *
     * @param fragment
     * @param binding
     */
    public FragmentSpotifyPlaylistsVM(FragmentSpotifyPlaylists fragment, FragmentSpotifyPlaylistsBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void onCreate() {
        userPlaylists = new ObservableArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserPlaylist();
    }

    /***
     * Call {@link com.startogamu.zikobot.module.spotify_api.manager.SpotifyApiManager} to find the current user playlists
     */
    private void loadUserPlaylist() {
        if (AppPrefs.spotifyUser()==null)return;
        userPlaylists.clear();
        userPlaylists.addAll(Mock.playlists(fragment.getContext()));
        SpotifyApiManager.getInstance().getUserPlaylists().subscribe(spotifyPlaylist -> {
            userPlaylists.clear();
            for (Item playlist : spotifyPlaylist.getItems()) {
                userPlaylists.add(new ItemPlaylistViewModel(fragment.getContext(), playlist));
            }
        }, throwable -> {
            Snackbar.make(binding.getRoot(), throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
        });
    }




    public void goToSettings(View view) {
        fragment.startActivity(IntentManager.goToSettings());
    }

}
