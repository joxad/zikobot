package com.startogamu.musicalarm.viewmodel.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.android.databinding.library.baseAdapters.BR;
import com.f2prateek.dart.henson.Bundler;
import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentSpotifyPlaylistTracksBinding;
import com.startogamu.musicalarm.di.manager.spotify_api.SpotifyAPIManager;
import com.startogamu.musicalarm.model.AlarmTrack;
import com.startogamu.musicalarm.model.spotify.SpotifyPlaylistItem;
import com.startogamu.musicalarm.model.spotify.SpotifyPlaylistWithTrack;
import com.startogamu.musicalarm.model.spotify.SpotifyTrack;
import com.startogamu.musicalarm.utils.EXTRA;
import com.startogamu.musicalarm.view.fragment.SpotifyPlaylistTracksFragment;
import com.startogamu.musicalarm.viewmodel.RecyclerViewViewModel;
import com.startogamu.musicalarm.viewmodel.items.ItemSpotifyTrackViewModel;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import org.parceler.Parcels;

import javax.inject.Inject;

import rx.Subscriber;


/***
 * {@link SpotifyPlaylistTracksViewModel} will call the apimanager to get the tracks of the playlist
 */
public class SpotifyPlaylistTracksViewModel extends BaseObservable implements RecyclerViewViewModel<ItemSpotifyTrackViewModel> {

    private ObservableArrayList<ItemSpotifyTrackViewModel> trackViewModels;
    private SpotifyPlaylistTracksFragment fragment;
    private FragmentSpotifyPlaylistTracksBinding binding;

    String playlistId;
    @Inject
    SpotifyAPIManager spotifyAPIManager;

    public SpotifyPlaylistTracksViewModel(SpotifyPlaylistTracksFragment fragment, FragmentSpotifyPlaylistTracksBinding binding) {
        playlistId = Bundler.of(fragment.getArguments()).get().getString(EXTRA.PLAYLIST_ID);
        MusicAlarmApplication.get(fragment.getActivity()).netComponent.inject(this);
        this.trackViewModels = new ObservableArrayList<>();
        this.fragment = fragment;
        this.binding = binding;
        loadTracks(playlistId);
    }

    private void loadTracks(String playlistId) {
        trackViewModels.clear();
        spotifyAPIManager.getPlaylistTracks(playlistId, new Subscriber<SpotifyPlaylistWithTrack>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SpotifyPlaylistWithTrack spotifyPlaylistWithTrack) {
                for (SpotifyPlaylistItem playlistItem : spotifyPlaylistWithTrack.getItems()) {
                    trackViewModels.add(new ItemSpotifyTrackViewModel(fragment, playlistItem.track));
                }
            }
        });
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

    /***
     * Selection of a track
     *
     * @param track
     */
    public void selectTrack(SpotifyTrack track) {
        AlarmTrack alarmTrack = new AlarmTrack();
        alarmTrack.setType(AlarmTrack.TYPE.SPOTIFY);
        alarmTrack.setRef(track.getId());
        alarmTrack.setImageUrl(track.getAlbum().getImages().get(0).url);
        alarmTrack.setArtistName(track.getArtists().get(0).getName());
        alarmTrack.setName(track.getName());
        Intent intent = new Intent();
        intent.putExtra(EXTRA.TRACK, Parcels.wrap(alarmTrack));
        fragment.getActivity().setResult(Activity.RESULT_OK, intent);
        fragment.getActivity().finish();
    }
}
