package com.startogamu.musicalarm.viewmodel.fragment;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;

import com.android.databinding.library.baseAdapters.BR;
import com.f2prateek.dart.henson.Bundler;
import com.joxad.easydatabinding.base.IVM;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.event.SelectAllTracks;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.databinding.FragmentSpotifyPlaylistTracksBinding;
import com.startogamu.musicalarm.module.alarm.model.AlarmTrack;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.module.spotify_api.model.SpotifyPlaylistItem;
import com.startogamu.musicalarm.module.spotify_api.model.SpotifyPlaylistWithTrack;
import com.startogamu.musicalarm.view.fragment.FragmentSpotifyPlaylistTracks;
import com.startogamu.musicalarm.viewmodel.base.TrackVM;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.tatarka.bindingcollectionadapter.ItemView;


/***
 * {@link SpotifyPlaylistTracksViewModel} will call the apimanager to get the tracks of the playlist
 */
public class SpotifyPlaylistTracksViewModel extends BaseObservable implements IVM {

    public ObservableArrayList<TrackVM> items;
    private FragmentSpotifyPlaylistTracks fragment;
    private FragmentSpotifyPlaylistTracksBinding binding;
    private SpotifyPlaylistWithTrack spotifyPlaylist;


    public ItemView itemsBinder = ItemView.of(BR.trackVM, R.layout.item_alarm_track);

    String playlistId;

    public SpotifyPlaylistTracksViewModel(FragmentSpotifyPlaylistTracks fragment, FragmentSpotifyPlaylistTracksBinding binding) {
        playlistId = Bundler.of(fragment.getArguments()).get().getString(EXTRA.PLAYLIST_ID);
        this.items = new ObservableArrayList<>();
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
        items.clear();
        Injector.INSTANCE.spotifyApi().manager().getPlaylistTracks(playlistId).subscribe(spotifyPlaylistWithTrack -> {
            spotifyPlaylist = spotifyPlaylistWithTrack;
            for (SpotifyPlaylistItem playlistItem : spotifyPlaylistWithTrack.getItems()) {
                items.add(new TrackVM(fragment.getContext(), AlarmTrack.from(playlistItem.track)));
            }
        }, throwable -> {

        });
    }

    @Subscribe
    public void onEvent(SelectAllTracks selectAllTracks) {
        for (TrackVM trackVM : items) {
            trackVM.select();
        }
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
