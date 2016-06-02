package com.startogamu.musicalarm.viewmodel.items;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.view.View;

import com.joxad.easydatabinding.base.IVM;
import com.startogamu.musicalarm.core.event.RemoveLocalTrackEvent;
import com.startogamu.musicalarm.core.event.RemoveTrackEvent;
import com.startogamu.musicalarm.core.event.SelectLocalTrackEvent;
import com.startogamu.musicalarm.core.event.SelectTrackEvent;
import com.startogamu.musicalarm.module.spotify_api.object.SpotifyTrack;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

/***
 *
 */
public class ItemSpotifyTrackViewModel extends BaseObservable implements IVM {

    private final Context context;
    private SpotifyTrack track;

    @Bindable
    public boolean isChecked;

    /***
     * @param
     * @param track
     */
    public ItemSpotifyTrackViewModel(Context context, SpotifyTrack track) {
        this.context = context;
        this.track = track;
    }


    public void setTrack(SpotifyTrack track) {
        this.track = track;
        notifyChange();
    }


    public void onTrackClicked(View view) {
        isChecked = !isChecked;
        if (isChecked) {
            EventBus.getDefault().post(new SelectTrackEvent(track));
        } else {
            EventBus.getDefault().post(new RemoveTrackEvent(track));

        }
        notifyChange();

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
    public void init() {

    }

    @Override
    public void destroy() {

    }

    public void select() {
        isChecked = !isChecked;
        notifyChange();

    }
}
