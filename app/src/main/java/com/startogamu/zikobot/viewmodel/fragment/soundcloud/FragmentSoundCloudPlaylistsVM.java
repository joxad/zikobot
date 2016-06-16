package com.startogamu.zikobot.viewmodel.fragment.soundcloud;

import android.databinding.ObservableArrayList;
import android.support.design.widget.Snackbar;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.navigation_manager.EventCollapseToolbar;
import com.startogamu.zikobot.core.event.navigation_manager.EventTabBars;
import com.startogamu.zikobot.databinding.FragmentSoundCloudPlaylistsBinding;
import com.startogamu.zikobot.module.component.Injector;
import com.startogamu.zikobot.module.mock.Mock;
import com.startogamu.zikobot.module.spotify_api.model.Item;
import com.startogamu.zikobot.view.fragment.soundcloud.FragmentSoundCloudPlaylists;
import com.startogamu.zikobot.viewmodel.items.ItemPlaylistViewModel;

import org.greenrobot.eventbus.EventBus;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 26/03/16.
 */
public class FragmentSoundCloudPlaylistsVM extends FragmentBaseVM<FragmentSoundCloudPlaylists, FragmentSoundCloudPlaylistsBinding> {

    private static final String TAG = "FragmentSpotifyPlaylists";
    public ObservableArrayList<ItemPlaylistViewModel> userPlaylists;
    public ItemView itemPlaylist = ItemView.of(BR.playlistVM, R.layout.item_playlist);

    /***
     * View model use to get the playlist of the user
     *
     * @param fragment
     * @param binding
     */
    public FragmentSoundCloudPlaylistsVM(FragmentSoundCloudPlaylists fragment, FragmentSoundCloudPlaylistsBinding binding) {
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

    }
}
