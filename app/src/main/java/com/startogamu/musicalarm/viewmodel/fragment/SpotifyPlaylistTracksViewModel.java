package com.startogamu.musicalarm.viewmodel.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.android.databinding.library.baseAdapters.BR;
import com.f2prateek.dart.henson.Bundler;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.databinding.FragmentSpotifyPlaylistTracksBinding;
import com.startogamu.musicalarm.module.alarm.object.AlarmTrack;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyPlaylistItem;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyPlaylistWithTrack;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyTrack;
import com.startogamu.musicalarm.view.fragment.SpotifyPlaylistTracksFragment;
import com.startogamu.musicalarm.viewmodel.ViewModel;
import com.startogamu.musicalarm.viewmodel.items.ItemSpotifyTrackViewModel;

import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Subscriber;


/***
 * {@link SpotifyPlaylistTracksViewModel} will call the apimanager to get the tracks of the playlist
 */
public class SpotifyPlaylistTracksViewModel extends BaseObservable implements ViewModel {

    private ObservableArrayList<ItemSpotifyTrackViewModel> trackViewModels;
    private SpotifyPlaylistTracksFragment fragment;
    private FragmentSpotifyPlaylistTracksBinding binding;

    @Bindable
    public ObservableArrayList<ItemSpotifyTrackViewModel> getItems() {
        return trackViewModels;
    }

    public ItemView itemsBinder = ItemView.of(BR.itemSpotifyTrackViewModel, R.layout.item_spotify_track);


    String playlistId;

    public SpotifyPlaylistTracksViewModel(SpotifyPlaylistTracksFragment fragment, FragmentSpotifyPlaylistTracksBinding binding) {
        playlistId = Bundler.of(fragment.getArguments()).get().getString(EXTRA.PLAYLIST_ID);
        this.trackViewModels = new ObservableArrayList<>();
        this.fragment = fragment;
        this.binding = binding;
        Injector.INSTANCE.spotifyApi().inject(this);

        loadTracks(playlistId);
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
                for (SpotifyPlaylistItem playlistItem : spotifyPlaylistWithTrack.getItems()) {
                    trackViewModels.add(new ItemSpotifyTrackViewModel(fragment, playlistItem.track));
                }
            }
        });
    }

    @Override
    public void onDestroy() {

    }


    /***
     * Selection of a track
     *
     * @param track
     */
    public void selectTrack(SpotifyTrack track) {
        AlarmTrack alarmTrack = new AlarmTrack();
        alarmTrack.setType(AlarmTrack.TYPE.SPOTIFY);
        alarmTrack.setRef("spotify:track:" + track.getId());
        alarmTrack.setImageUrl(track.getAlbum().getImages().get(0).url);
        alarmTrack.setArtistName(track.getArtists().get(0).getName());
        alarmTrack.setName(track.getName());
        Intent intent = new Intent();
        intent.putExtra(EXTRA.TRACK, Parcels.wrap(alarmTrack));
        fragment.getActivity().setResult(Activity.RESULT_OK, intent);
        fragment.getActivity().finish();
    }
}
