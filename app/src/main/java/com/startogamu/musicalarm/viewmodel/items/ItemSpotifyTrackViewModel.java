package com.startogamu.musicalarm.viewmodel.items;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.startogamu.musicalarm.databinding.FragmentSpotifyPlaylistTracksBinding;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyTrack;
import com.startogamu.musicalarm.view.fragment.SpotifyPlaylistTracksFragment;
import com.startogamu.musicalarm.viewmodel.ViewModel;

import java.util.concurrent.TimeUnit;

/***
 *
 */
public class ItemSpotifyTrackViewModel extends BaseObservable implements ViewModel {

    private SpotifyTrack track;

    private SpotifyPlaylistTracksFragment fragment;
    private FragmentSpotifyPlaylistTracksBinding binding;

    /***
     * @param fragment
     * @param track
     */
    public ItemSpotifyTrackViewModel(SpotifyPlaylistTracksFragment fragment, SpotifyTrack track) {
        this.fragment = fragment;
        this.track = track;
    }


    public void setTrack(SpotifyTrack track) {
        this.track = track;
        notifyChange();
    }


    public void onTrackClicked(View view) {
        fragment.selectTrack(track);
    }

    @Bindable
    public String getName() {
        return track.getName();
    }


    @Bindable
    public String getImageUrl() {
        return track.getAlbum().getImages().get(0).getUrl();
    }

    @Bindable
    public String getArtistName() {
        return track.getArtists().get(0).getName();
    }

    @Bindable
    public String getTime() {
        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(track.getDuration()),
                TimeUnit.MILLISECONDS.toSeconds(track.getDuration()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(track.getDuration()))
        );
    }

    @Override
    public void onDestroy() {

    }
}
