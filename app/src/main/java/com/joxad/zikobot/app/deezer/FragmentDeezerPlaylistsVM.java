package com.joxad.zikobot.app.deezer;

import android.databinding.ObservableArrayList;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.deezer.sdk.model.Playlist;
import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.fragmentmanager.IntentManager;
import com.joxad.zikobot.app.databinding.FragmentDeezerPlaylistsBinding;
import com.joxad.zikobot.data.module.deezer.DeezerManager;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 07/07/16.
 */
public class FragmentDeezerPlaylistsVM extends FragmentBaseVM<FragmentDeezerPlaylists, FragmentDeezerPlaylistsBinding> {


    private static final String TAG = "FragmentDeezerPlaylistsVM";
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
        DeezerManager.getInstance().current().subscribe(user -> {
            loadUserPlaylist();

        }, throwable -> {

        });
    }

    /***
     */
    private void loadUserPlaylist() {

        userPlaylists.clear();

        DeezerManager.getInstance().playlists().subscribe(playlists -> {
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
