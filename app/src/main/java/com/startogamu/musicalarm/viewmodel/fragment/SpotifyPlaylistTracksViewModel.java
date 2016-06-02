package com.startogamu.musicalarm.viewmodel.fragment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.android.databinding.library.baseAdapters.BR;
import com.f2prateek.dart.henson.Bundler;
import com.joxad.easydatabinding.base.IVM;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.event.SelectPlaylistEvent;
import com.startogamu.musicalarm.core.event.SelectTrackEvent;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.databinding.FragmentSpotifyPlaylistTracksBinding;
import com.startogamu.musicalarm.module.alarm.manager.AlarmTrackManager;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyPlaylistItem;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyPlaylistWithTrack;
import com.startogamu.musicalarm.view.fragment.FragmentSpotifyPlaylistTracks;
import com.startogamu.musicalarm.viewmodel.items.ItemSpotifyTrackViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Subscriber;


/***
 * {@link SpotifyPlaylistTracksViewModel} will call the apimanager to get the tracks of the playlist
 */
public class SpotifyPlaylistTracksViewModel extends BaseObservable implements IVM {

    private ObservableArrayList<ItemSpotifyTrackViewModel> trackViewModels;
    private FragmentSpotifyPlaylistTracks fragment;
    private FragmentSpotifyPlaylistTracksBinding binding;
    private SpotifyPlaylistWithTrack spotifyPlaylist;

    @Bindable
    public ObservableArrayList<ItemSpotifyTrackViewModel> getItems() {
        return trackViewModels;
    }

    public ItemView itemsBinder = ItemView.of(BR.itemSpotifyTrackViewModel, R.layout.item_spotify_track);

    String playlistId;

    public SpotifyPlaylistTracksViewModel(FragmentSpotifyPlaylistTracks fragment, FragmentSpotifyPlaylistTracksBinding binding) {
        playlistId = Bundler.of(fragment.getArguments()).get().getString(EXTRA.PLAYLIST_ID);
        this.trackViewModels = new ObservableArrayList<>();
        this.fragment = fragment;
        this.binding = binding;
        EventBus.getDefault().register(this);
        Injector.INSTANCE.spotifyApi().inject(this);

        loadTracks(playlistId);
    }


    @Override
    public void init() {

    }


    private void loadTracks(String playlistId) {
        trackViewModels.clear();
        Injector.INSTANCE.spotifyApi().manager().getPlaylistTracks(playlistId).subscribe(new Subscriber<SpotifyPlaylistWithTrack>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SpotifyPlaylistWithTrack spotifyPlaylistWithTrack) {
                spotifyPlaylist = spotifyPlaylistWithTrack;
                for (SpotifyPlaylistItem playlistItem : spotifyPlaylistWithTrack.getItems()) {
                    trackViewModels.add(new ItemSpotifyTrackViewModel(fragment.getContext(), playlistItem.track));
                }
            }
        });
    }

    @Subscribe
    public void onEvent(SelectPlaylistEvent selectPlaylistEvent) {
        AlarmTrackManager.selecteAllTracks(spotifyPlaylist);
        for (ItemSpotifyTrackViewModel tracksViewModel: trackViewModels) {
            tracksViewModel.select();
        }
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
