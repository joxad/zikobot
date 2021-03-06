package com.joxad.zikobot.app.localtracks;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.joxad.zikobot.app.alarm.AlarmTrackManager;
import com.joxad.zikobot.app.player.event.EventPlayTrack;
import com.joxad.zikobot.data.AppPrefs;
import com.joxad.zikobot.data.event.dialog.EventShowDialogSettings;
import com.joxad.zikobot.data.model.Track;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by josh on 30/05/16.
 */
public class TrackVM extends BaseVM<Track> {

    public boolean isChecked = false;
    public ObservableBoolean isPlaying;

    /***
     * @param context
     * @param model
     */
    public TrackVM(Context context, Track model) {
        super(context, model);
    }

    @Override
    public void onCreate() {
        isPlaying = new ObservableBoolean(false);
    }


    @Bindable
    public String getImageUrl() {
        return model.getImageUrl();
    }

    @Bindable
    public String getName() {
        return model.getName();
    }

    @Bindable
    public String getArtistName() {
        return model.getArtistName();
    }

    @Bindable
    public String getInfos() {
        return String.format("%s - %s", model.getName(), model.getArtistName());
    }

    @Bindable
    public int getType() {
        return model.getType();
    }

    @Bindable
    public long getDuration() {
        return model.getDuration();
    }

    public void onTrackClicked(View view) {
        isChecked = !isChecked;
        if (isChecked) {
            AlarmTrackManager.selectTrack(model);
        } else {
            AlarmTrackManager.removeTrack(model);

        }
        notifyChange();
    }


    public void onMoreClicked(View view) {
        EventBus.getDefault().post(new EventShowDialogSettings(model));

    }

    /***
     * Event called when we want to play a song
     *
     * @param view
     */
    public void onTrackPlay(View view) {
        EventBus.getDefault().post(new EventPlayTrack(this));
    }

    public Track getModel() {
        return model;
    }


    public boolean onLongClick(View view) {
        EventBus.getDefault().post(new EventShowDialogSettings(model));
        return true;
    }

    public String getRef() {
        return model.getRef();
    }


}
