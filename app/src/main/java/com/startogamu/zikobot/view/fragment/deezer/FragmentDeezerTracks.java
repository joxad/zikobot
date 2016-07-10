package com.startogamu.zikobot.view.fragment.deezer;

import android.support.annotation.LayoutRes;
import android.view.Menu;
import android.view.MenuInflater;

import com.android.databinding.library.baseAdapters.BR;
import com.deezer.sdk.model.Playlist;
import com.f2prateek.dart.henson.Bundler;
import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.FragmentDeezerTracksBinding;
import com.startogamu.zikobot.viewmodel.fragment.deezer.FragmentDeezerTracksVM;

import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 31/03/16.
 */
public class FragmentDeezerTracks extends FragmentBase<FragmentDeezerTracksBinding, FragmentDeezerTracksVM> {

    public final static String TAG = FragmentDeezerTracks.class.getSimpleName();

    public static FragmentDeezerTracks newInstance(Playlist item, int dataVm, @LayoutRes int layout) {
        FragmentDeezerTracks fragment = new FragmentDeezerTracks();

        Bundler bundler = Bundler.create()
                .put(EXTRA.DATA_VM, dataVm)
                .put(EXTRA.LAYOUT, layout);
        if (item != null) {
            bundler.put(EXTRA.PLAYLIST, item);

        }
        fragment.setArguments(bundler.get());
        return fragment;
    }

    @Override
    public int data() {
        return BR.fragmentDeezerTracksVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_deezer_tracks;
    }

    @Override
    public FragmentDeezerTracksVM baseFragmentVM(FragmentDeezerTracksBinding binding) {
        return new FragmentDeezerTracksVM(this, binding) {
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
