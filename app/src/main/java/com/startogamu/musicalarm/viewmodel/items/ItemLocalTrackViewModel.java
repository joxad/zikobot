package com.startogamu.musicalarm.viewmodel.items;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.startogamu.musicalarm.databinding.FragmentSpotifyPlaylistTracksBinding;
import com.startogamu.musicalarm.model.LocalTrack;
import com.startogamu.musicalarm.model.spotify.SpotifyTrack;
import com.startogamu.musicalarm.view.fragment.LocalMusicFragment;
import com.startogamu.musicalarm.view.fragment.SpotifyPlaylistTracksFragment;
import com.startogamu.musicalarm.viewmodel.ViewModel;
import com.startogamu.musicalarm.viewmodel.fragment.LocalMusicViewModel;

import java.io.File;
import java.util.concurrent.TimeUnit;

/***
 *
 */
public class ItemLocalTrackViewModel extends BaseObservable implements ViewModel {

    private LocalTrack track;

    private LocalMusicFragment fragment;

    /***
     * @param fragment
     * @param track
     */
    public ItemLocalTrackViewModel(LocalMusicFragment fragment, LocalTrack track) {
        this.fragment = fragment;
        this.track = track;
    }


    public void setTrack(LocalTrack track) {
        this.track = track;
        notifyChange();
    }


    public void onTrackClicked(View view) {
        fragment.selectTrack(track);
    }

    @Bindable
    public String getName() {
        return track.getTitle();
    }

    @Bindable
    public String getArtistName() {
        return track.getArtist();
    }

    @Bindable
    public String getTime() {
        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(track.getDuration()),
                TimeUnit.MILLISECONDS.toSeconds(track.getDuration()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(track.getDuration()))
        );
    }


    @Bindable
    public String getImagePath() {
        if(track.getArtPath() != null) {
            return  track.getArtPath();
        }
        return null;
    }

    @Override
    public void onDestroy() {

    }
}
