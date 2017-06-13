package com.joxad.zikobot.app.spotify;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.fragmentmanager.IntentManager;
import com.joxad.zikobot.app.databinding.FragmentSpotifyPlaylistsBinding;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.module.spotify_api.manager.SpotifyApiManager;
import com.joxad.zikobot.data.module.spotify_api.model.Item;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;
import com.orhanobut.logger.Logger;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 26/03/16.
 */
public class FragmentSpotifyPlaylistsVM extends FragmentBaseVM<FragmentSpotifyPlaylists, FragmentSpotifyPlaylistsBinding> {

    private static final String TAG = "FragmentSpotifyPlaylists";
    public ObservableArrayList<ItemPlaylistViewModel> userPlaylists;
    public ItemView itemPlaylist = ItemView.of(BR.playlistVM, R.layout.item_playlist);
    private Merlin merlin;
    public ObservableBoolean isConnectedToInternet;

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
        merlin = new Merlin.Builder().withAllCallbacks().build(fragment.getContext());

        isConnectedToInternet = new ObservableBoolean(MerlinsBeard.from(fragment.getContext()).isConnected());

        userPlaylists = new ObservableArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        merlin.bind();
        merlin.registerConnectable(() -> {
            loadUserPlaylist();
            isConnectedToInternet.set(true);
        });
        merlin.registerDisconnectable(() -> isConnectedToInternet.set(false));
        merlin.registerBindable(networkStatus -> {
            Logger.d("networkstatus" + networkStatus.isAvailable());
        });
    }

    @Override
    public void onPause() {
        merlin.unbind();
        super.onPause();
    }

    /***
     * Call {@link com.joxad.zikobot} to find the current user playlists
     */
    private void loadUserPlaylist() {
        if (AppPrefs.spotifyUser() == null) return;
        userPlaylists.clear();
        // userPlaylists.addAll(Mock.playlists(fragment.getContext()));
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
