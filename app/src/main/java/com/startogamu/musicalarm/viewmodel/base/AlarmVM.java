package com.startogamu.musicalarm.viewmodel.base;

import android.content.Context;
import android.databinding.Bindable;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.musicalarm.module.alarm.object.Alarm;

/**
 * Created by josh on 29/05/16.
 */
public class AlarmVM extends BaseVM<Alarm> {
    /***
     * @param context
     * @param model
     */
    public AlarmVM(Context context, Alarm model) {
        super(context, model);
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Bindable
    public boolean isActivated() {
        return model.getActive()==1;
    }
}
