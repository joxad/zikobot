package com.joxad.zikobot.app.soundcloud;

import android.databinding.ObservableArrayList;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.fragmentmanager.IntentManager;
import com.joxad.zikobot.app.databinding.FragmentSoundCloudPlaylistsBinding;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.module.soundcloud.manager.SoundCloudApiManager;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 26/03/16.
 */
public class FragmentSoundCloudPlaylistsVM extends FragmentBaseVM<FragmentSoundCloudPlaylists, FragmentSoundCloudPlaylistsBinding> {

    private static final String TAG = "FragmentSoundCloudPlaylistsVM";
    public ObservableArrayList<SoundCloudPlaylistVM> userPlaylists;
    public ItemView itemPlaylist = ItemView.of(BR.playlistVM, R.layout.item_soundcloud_playlist);

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
    public void onCreate() {
        userPlaylists = new ObservableArrayList<>();
        loadUserPlaylist();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /***
     */
    private void loadUserPlaylist() {
        if (AppPrefs.soundCloudUser() == null) return;
        userPlaylists.clear();
        //userPlaylists.addAll(Mock.scPlaylists(fragment.getContext()));
        SoundCloudApiManager.getInstance().userPlaylists(AppPrefs.soundCloudUser().getId()).subscribe(soundCloudPlaylists -> {
            userPlaylists.clear();
            for (SoundCloudPlaylist playlist : soundCloudPlaylists) {
                if (!playlist.getSoundCloudTracks().isEmpty())
                    userPlaylists.add(new SoundCloudPlaylistVM(fragment.getContext(), playlist));
            }
        }, throwable -> {
            Snackbar.make(binding.getRoot(), throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
        });
    }


    public void goToSettings(View view) {
        fragment.startActivity(IntentManager.goToSettings());
    }
}
