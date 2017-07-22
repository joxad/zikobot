package com.joxad.zikobot.app.soundcloud;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.fragmentmanager.IntentManager;
import com.joxad.zikobot.app.databinding.FragmentSoundCloudPlaylistsBinding;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.module.soundcloud.manager.SoundCloudApiManager;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;
import com.orhanobut.logger.Logger;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by josh on 26/03/16.
 */
public class FragmentSoundCloudPlaylistsVM extends FragmentBaseVM<FragmentSoundCloudPlaylists, FragmentSoundCloudPlaylistsBinding> {

    private static final String TAG = "FragmentSoundCloudPlaylistsVM";
    public ObservableArrayList<SoundCloudPlaylistVM> userPlaylists;
    public ItemBinding itemPlaylist = ItemBinding.of(BR.playlistVM, R.layout.item_soundcloud_playlist);

    /***
     * View model use to get the playlist of the user
     *
     * @param fragment
     * @param binding
     */
    public FragmentSoundCloudPlaylistsVM(FragmentSoundCloudPlaylists fragment, FragmentSoundCloudPlaylistsBinding binding,@Nullable Bundle saved) {
        super(fragment, binding,saved);
    }

    @Override
    public void onCreate(@Nullable Bundle saved) {
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
        SoundCloudApiManager.getInstance().userPlaylists(AppPrefs.soundCloudUser().getId())
                .zipWith(SoundCloudApiManager.getInstance().favoriteTracks(AppPrefs.soundCloudUser().getId()),
                        (soundCloudPlaylists, soundCloudTracks) -> {
                            userPlaylists.clear();
                            if (!soundCloudTracks.isEmpty())
                                userPlaylists.add(new SoundCloudPlaylistVM(fragment.getContext(),
                                        SoundCloudPlaylist.favorite(AppPrefs.soundCloudUser(), soundCloudTracks)));
                            for (SoundCloudPlaylist playlist : soundCloudPlaylists) {
                                if (!playlist.getSoundCloudTracks().isEmpty())
                                    userPlaylists.add(new SoundCloudPlaylistVM(fragment.getContext(), playlist));
                            }
                            return true;
                        }).subscribe(aBoolean -> Logger.d("Soundcloud " + aBoolean),
                throwable -> Logger.e("Soundcloud " + throwable.getLocalizedMessage()));
    }


    public void goToSettings(View view) {
        fragment.startActivity(IntentManager.goToSettings());
    }
}
