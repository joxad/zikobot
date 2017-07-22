package com.joxad.zikobot.app.alarm;

import android.os.Bundle;

import com.joxad.easydatabinding.bottomsheet.DialogBottomSheetBase;
import com.joxad.zikobot.app.BR;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.databinding.DialogFragmentSettingsBinding;
import com.joxad.zikobot.data.model.Album;
import com.joxad.zikobot.data.model.Playlist;
import com.joxad.zikobot.data.model.Track;
import com.joxad.zikobot.data.module.localmusic.model.LocalAlbum;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;
import com.joxad.zikobot.data.module.spotify_api.model.Item;

import org.parceler.Parcels;

/**
 * Created by josh on 20/06/16.
 */
public class DialogFragmentSettings extends DialogBottomSheetBase<DialogFragmentSettingsBinding, DialogFragmentSettingsVM> {


    public static final String TAG = DialogFragmentSettings.class.getSimpleName();



    public static DialogFragmentSettings newInstance(Item track) {
        DialogFragmentSettings fragment = new DialogFragmentSettings();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA.PLAYLIST, Parcels.wrap(track));
        fragment.setArguments(bundle);
        return fragment;
    }


    public static DialogFragmentSettings newInstance(SoundCloudPlaylist track) {
        DialogFragmentSettings fragment = new DialogFragmentSettings();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA.PLAYLIST_SC, Parcels.wrap(track));
        fragment.setArguments(bundle);
        return fragment;
    }
    public static DialogFragmentSettings newInstance(Track track) {
        DialogFragmentSettings fragment = new DialogFragmentSettings();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA.TRACK, Parcels.wrap(track));
        fragment.setArguments(bundle);
        return fragment;
    }

    public static DialogFragmentSettings newInstance(LocalAlbum album) {
        DialogFragmentSettings fragment = new DialogFragmentSettings();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA.LOCAL_ALBUM, Parcels.wrap(Album.from(album)));
        fragment.setArguments(bundle);
        return fragment;
    }


    public static DialogFragmentSettings newInstance(Album album) {
        DialogFragmentSettings fragment = new DialogFragmentSettings();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA.LOCAL_ALBUM, Parcels.wrap(album));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int data() {
        return BR.fragmentSettingsVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.dialog_fragment_settings;
    }

    @Override
    public DialogFragmentSettingsVM baseFragmentVM(DialogFragmentSettingsBinding binding) {
        return new DialogFragmentSettingsVM(this,binding);
    }


}
