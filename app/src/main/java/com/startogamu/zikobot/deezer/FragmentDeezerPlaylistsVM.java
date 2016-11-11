package com.startogamu.zikobot.deezer;

import android.databinding.ObservableArrayList;
import android.support.design.widget.Snackbar;
import android.view.View;


import com.deezer.sdk.model.Playlist;
import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.fragmentmanager.IntentManager;
import com.startogamu.zikobot.databinding.FragmentDeezerPlaylistsBinding;
import com.startogamu.zikobot.core.module.deezer.DeezerManager;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 07/07/16.
 */
public class FragmentDeezerPlaylistsVM extends FragmentBaseVM<FragmentDeezerPlaylists, FragmentDeezerPlaylistsBinding> {


    private static final String TAG = "FragmentSpotifyPlaylists";
    public ObservableArrayList<DeezerPlaylistVM> userPlaylists;
    public ItemView itemPlaylist = ItemView.of(BR.playlistVM, R.layout.item_playlist_deezer);

    /***
     * @param fragment
     * @param binding
     */
    public FragmentDeezerPlaylistsVM(FragmentDeezerPlaylists fragment, FragmentDeezerPlaylistsBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void onCreate() {
        userPlaylists = new ObservableArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();

        checkAccount();
    }

    private void checkAccount() {
        DeezerManager.current().subscribe(user -> {
            loadUserPlaylist();

        }, throwable -> {

        });
    }

    /***
     * Call {@link com.startogamu.zikobot.module.spotify_api.manager.SpotifyApiManager} to find the current user playlists
     */
    private void loadUserPlaylist() {

        userPlaylists.clear();

        DeezerManager.playlists().subscribe(playlists -> {
            for (Playlist playlist : playlists) {
                userPlaylists.add(new DeezerPlaylistVM(fragment.getContext(), playlist));
            }
        }, throwable -> {
            Snackbar.make(binding.getRoot(), throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
        });

    }


    public void goToSettings(View view) {
        fragment.startActivity(IntentManager.goToSettings());
    }

}
