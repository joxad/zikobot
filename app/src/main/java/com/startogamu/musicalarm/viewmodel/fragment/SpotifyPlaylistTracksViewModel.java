package com.startogamu.musicalarm.viewmodel.fragment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.android.databinding.library.baseAdapters.BR;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentSpotifyPlaylistTracksBinding;
import com.startogamu.musicalarm.view.fragment.SpotifyPlaylistTracksFragment;
import com.startogamu.musicalarm.viewmodel.RecyclerViewViewModel;
import com.startogamu.musicalarm.viewmodel.items.ItemSpotifyTrackViewModel;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;


/***
 * {@link SpotifyPlaylistTracksViewModel} will call the apimanager to get the tracks of the playlist
 */
public class SpotifyPlaylistTracksViewModel extends BaseObservable implements RecyclerViewViewModel<ItemSpotifyTrackViewModel> {

    private ObservableArrayList<ItemSpotifyTrackViewModel> trackViewModels;
    private SpotifyPlaylistTracksFragment fragment;
    private FragmentSpotifyPlaylistTracksBinding binding;

    public SpotifyPlaylistTracksViewModel(SpotifyPlaylistTracksFragment fragment, FragmentSpotifyPlaylistTracksBinding binding) {
        this.trackViewModels = new ObservableArrayList<>();
        this.fragment = fragment;
        this.binding = binding;
    }

    @Override
    public void onDestroy() {

    }


    @Bindable
    @Override
    public ObservableArrayList<ItemSpotifyTrackViewModel> getItems() {
        return trackViewModels;
    }

    @Override
    public ItemBinder<ItemSpotifyTrackViewModel> getItemsBinder() {
        return new ItemBinderBase<>(BR.itemSpotifyTrackViewModel, R.layout.item_spotify_track);
    }
}
