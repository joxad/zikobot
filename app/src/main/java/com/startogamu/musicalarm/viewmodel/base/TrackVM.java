package com.startogamu.musicalarm.viewmodel.base;

import android.content.Context;
import android.databinding.Bindable;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.musicalarm.module.alarm.object.AlarmTrack;

/**
 * Created by josh on 30/05/16.
 */
public class TrackVM extends BaseVM<AlarmTrack> {
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
}
