package com.startogamu.musicalarm.viewmodel.items;

import android.databinding.BaseObservable;

import com.startogamu.musicalarm.databinding.FragmentSpotifyPlaylistTracksBinding;
import com.startogamu.musicalarm.view.fragment.SpotifyPlaylistTracksFragment;
import com.startogamu.musicalarm.viewmodel.ViewModel;

/***
 *
 */
public class ItemSpotifyTrackViewModel extends BaseObservable implements ViewModel {



    private SpotifyPlaylistTracksFragment fragment;
    private FragmentSpotifyPlaylistTracksBinding binding;

    /***
     * @param fragment
     * @param binding
     */
    public ItemSpotifyTrackViewModel(SpotifyPlaylistTracksFragment fragment, FragmentSpotifyPlaylistTracksBinding binding) {
        this.fragment = fragment;
        this.binding = binding;
    }


    @Override
    public void onDestroy() {

    }
}
