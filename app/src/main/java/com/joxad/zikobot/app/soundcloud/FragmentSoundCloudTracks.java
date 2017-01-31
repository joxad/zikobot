package com.joxad.zikobot.app.soundcloud;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.Menu;
import android.view.MenuInflater;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.fragment.v4.FragmentBase;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.databinding.FragmentSoundCloudTracksBinding;
import com.joxad.zikobot.data.module.soundcloud.model.SoundCloudPlaylist;

import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 31/03/16.
 */
public class FragmentSoundCloudTracks extends FragmentBase<FragmentSoundCloudTracksBinding, FragmentSoundCloudTracksVM> {

    public final static String TAG = FragmentSoundCloudTracks.class.getSimpleName();

    public static FragmentSoundCloudTracks newInstance(SoundCloudPlaylist item, int dataVm, @LayoutRes int layout) {
        FragmentSoundCloudTracks fragment = new FragmentSoundCloudTracks();
        Bundle bundle = new Bundle();
        if (item != null) {
            bundle.putParcelable(EXTRA.PLAYLIST, Parcels.wrap(item));
        }
        bundle.putInt(EXTRA.DATA_VM, dataVm);
        bundle.putInt(EXTRA.LAYOUT, layout);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int data() {
        return BR.fragmentSoundCloudTracksVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_sound_cloud_tracks;
    }

    @Override
    public FragmentSoundCloudTracksVM baseFragmentVM(FragmentSoundCloudTracksBinding binding) {
        return new FragmentSoundCloudTracksVM(this, binding) {
            @Override
            public ItemView getItemView() {
                return ItemView.of(fragment.getArguments().getInt(EXTRA.DATA_VM), getArguments().getInt(EXTRA.LAYOUT));
            }
        };
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_music, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


}
