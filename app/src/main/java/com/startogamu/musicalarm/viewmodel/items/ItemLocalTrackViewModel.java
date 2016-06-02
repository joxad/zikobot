package com.startogamu.musicalarm.viewmodel.items;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.IVM;
import com.startogamu.musicalarm.core.event.RemoveLocalTrackEvent;
import com.startogamu.musicalarm.core.event.SelectLocalTrackEvent;
import com.startogamu.musicalarm.module.alarm.object.LocalTrack;
import com.startogamu.musicalarm.view.fragment.LocalMusicFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

/***
 *
 */
public class ItemLocalTrackViewModel extends BaseObservable implements IVM {

    public boolean isChecked = false;
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

    @Override
    public void init() {

    }

    public void setTrack(LocalTrack track) {
        this.track = track;
        notifyChange();
    }


    public void onTrackClicked(View view) {
        isChecked = !isChecked;
        if (isChecked) {
            EventBus.getDefault().post(new SelectLocalTrackEvent(track));
        } else {
            EventBus.getDefault().post(new RemoveLocalTrackEvent(track));

        }
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
        if (track.getArtPath() != null) {
            return track.getArtPath();
        }
        return null;
    }


    @Override
    public void destroy() {

    }
}
