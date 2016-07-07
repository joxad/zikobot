package com.startogamu.zikobot.viewmodel.fragment.deezer;

import android.databinding.ObservableArrayList;
import android.support.design.widget.Snackbar;

import com.android.databinding.library.baseAdapters.BR;
import com.deezer.sdk.model.Playlist;
import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.navigation_manager.EventCollapseToolbar;
import com.startogamu.zikobot.core.event.navigation_manager.EventTabBars;
import com.startogamu.zikobot.databinding.FragmentDeezerPlaylistsBinding;
import com.startogamu.zikobot.module.deezer.manager.DeezerManager;
import com.startogamu.zikobot.view.fragment.deezer.FragmentDeezerPlaylists;
import com.startogamu.zikobot.viewmodel.items.ItemDeezerPlaylistVM;

import org.greenrobot.eventbus.EventBus;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 07/07/16.
 */
public class FragmentDeezerPlaylistsVM extends FragmentBaseVM<FragmentDeezerPlaylists, FragmentDeezerPlaylistsBinding> {


    private static final String TAG = "FragmentSpotifyPlaylists";
    public ObservableArrayList<ItemDeezerPlaylistVM> userPlaylists;
    public ItemView itemPlaylist = ItemView.of(BR.playlistVM, R.layout.item_playlist_deezer);

    /***
     * @param fragment
     * @param binding
     */
    public FragmentDeezerPlaylistsVM(FragmentDeezerPlaylists fragment, FragmentDeezerPlaylistsBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void init() {
        userPlaylists = new ObservableArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().post(new EventCollapseToolbar(null, null));
        EventBus.getDefault().post(new EventTabBars(true, TAG));

        loadUserPlaylist();
    }

    /***
     * Call {@link com.startogamu.zikobot.module.spotify_api.manager.SpotifyApiManager} to find the current user playlists
     */
    private void loadUserPlaylist() {
        userPlaylists.clear();

        DeezerManager.playlists().subscribe(playlists -> {
            for (Playlist playlist : playlists) {
                userPlaylists.add(new ItemDeezerPlaylistVM(fragment.getContext(), playlist));
            }
        }, throwable -> {
            Snackbar.make(binding.getRoot(), throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
        });
    }


    @Override
    public void destroy() {

    }
}
