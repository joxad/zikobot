package com.startogamu.zikobot.viewmodel.base;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.zikobot.module.alarm.manager.AlarmTrackManager;
import com.startogamu.zikobot.module.alarm.model.AlarmTrack;

/**
 * Created by josh on 30/05/16.
 */
public class TrackVM extends BaseVM<AlarmTrack> {

    public boolean isChecked = false;

    /***
     * @param context
     * @param model
     */
    public TrackVM(Context context, AlarmTrack model) {
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

    public void select() {
        isChecked = !isChecked;
        AlarmTrackManager.selectTrack(model);
        notifyChange();

    }

    /**
     * {@link }
     * @param track
     */
    public void updateTrack(AlarmTrack track) {
        this.model = track;
        notifyChange();
    }
}
