package com.startogamu.zikobot.viewmodel.base;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.EventShowMessage;
import com.startogamu.zikobot.core.event.player.EventPlayTrack;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.module.zikobot.manager.AlarmTrackManager;
import com.startogamu.zikobot.module.zikobot.model.TYPE;
import com.startogamu.zikobot.module.zikobot.model.Track;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by josh on 30/05/16.
 */
public class TrackVM extends BaseVM<Track> {

    public boolean isChecked = false;

    /***
     * @param context
     * @param model
     */
    public TrackVM(Context context, Track model) {
        super(context, model);
    }

    @Override
    public void init() {
    }

    @Override
    public void destroy() {

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

    public void onTrackClicked(View view) {
        isChecked = !isChecked;
        if (isChecked) {
            AlarmTrackManager.selectTrack(model);
        } else {
            AlarmTrackManager.removeTrack(model);

        }
        notifyChange();
    }

    /***
     * Event called when we want to play a song
     *
     * @param view
     */
    public void onTrackPlay(View view) {
        EventBus.getDefault().post(new EventPlayTrack(this));
    }

    public void select() {
        isChecked = !isChecked;
        AlarmTrackManager.selectTrack(model);
        notifyChange();

    }

    /**
     * {@link }
     *
     * @param track
     */
    public void updateTrack(Track track) {
        this.model = track;
        notifyChange();
    }

    public Track getModel() {
        return model;
    }
}
