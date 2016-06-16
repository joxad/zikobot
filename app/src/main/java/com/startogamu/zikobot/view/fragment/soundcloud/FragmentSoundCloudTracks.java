package com.startogamu.zikobot.view.fragment.soundcloud;

import android.support.annotation.LayoutRes;
import android.view.Menu;
import android.view.MenuInflater;

import com.android.databinding.library.baseAdapters.BR;
import com.f2prateek.dart.henson.Bundler;
import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentSoundCloudTracksBinding;
import com.startogamu.zikobot.module.soundcloud.model.SoundCloudPlaylist;
import com.startogamu.zikobot.viewmodel.fragment.soundcloud.FragmentSoundCloudTracksVM;

import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 31/03/16.
 */
public class FragmentSoundCloudTracks extends FragmentBase<FragmentSoundCloudTracksBinding, FragmentSoundCloudTracksVM> {

    public final static String TAG = FragmentSoundCloudTracks.class.getSimpleName();

    public static FragmentSoundCloudTracks newInstance(SoundCloudPlaylist item, int dataVm, @LayoutRes int layout) {
        FragmentSoundCloudTracks fragment = new FragmentSoundCloudTracks();

        Bundler bundler = Bundler.create()
                .put(EXTRA.DATA_VM, dataVm)
                .put(EXTRA.LAYOUT, layout);
        if (item != null) {
            bundler.put(EXTRA.PLAYLIST, Parcels.wrap(item));

        }
        fragment.setArguments(bundler.get());
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
