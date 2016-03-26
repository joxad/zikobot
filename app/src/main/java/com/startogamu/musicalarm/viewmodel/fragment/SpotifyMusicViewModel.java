package com.startogamu.musicalarm.viewmodel.fragment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.support.v4.app.Fragment;

import com.android.databinding.library.baseAdapters.BR;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentSpotifyMusicBinding;
import com.startogamu.musicalarm.di.manager.spotify_api.SpotifyAPIManager;
import com.startogamu.musicalarm.model.spotify.Item;
import com.startogamu.musicalarm.model.spotify.SpotifyPlaylist;
import com.startogamu.musicalarm.viewmodel.ItemAlarmViewModel;
import com.startogamu.musicalarm.viewmodel.ViewModel;
import com.startogamu.musicalarm.viewmodel.items.ItemPlaylistViewModel;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by josh on 26/03/16.
 */
public class SpotifyMusicViewModel extends BaseObservable implements ViewModel {

    private Fragment fragment;
    private FragmentSpotifyMusicBinding binding;
    private ObservableArrayList<ItemPlaylistViewModel> observableArrayList;

    @Inject
    SpotifyAPIManager spotifyAPIManager;

    public SpotifyMusicViewModel(Fragment fragment, FragmentSpotifyMusicBinding binding) {
        observableArrayList = new ObservableArrayList<>();
        MusicAlarmApplication.get(fragment.getContext()).netComponent.inject(this);
        this.fragment = fragment;
        this.binding = binding;
        loadPlaylists();

    }

    private void loadPlaylists() {
        spotifyAPIManager.getPlaylist("", new Subscriber<SpotifyPlaylist>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SpotifyPlaylist spotifyPlaylist) {
                for (Item item : spotifyPlaylist.getItems()) {
                    ItemPlaylistViewModel itemPlaylistViewModel = new ItemPlaylistViewModel(fragment.getContext(), item);
                    observableArrayList.add(itemPlaylistViewModel);
                }
            }
        });
    }


    @Bindable
    public ObservableArrayList<ItemPlaylistViewModel> getItemViewModels() {
        return observableArrayList;
    }

    public ItemBinder<ItemPlaylistViewModel> itemViewBinder() {
        return new ItemBinderBase<>(BR.itemPlaylistViewModel, R.layout.item_playlist);
    }

    @Override
    public void onDestroy() {

    }
}
