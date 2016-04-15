package com.startogamu.musicalarm.viewmodel.items;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.view.Henson;

/**
 * Created by josh on 09/03/16.
 */
public class ItemAlarmVM extends BaseVM<Alarm> {

    /***
     * @param context
     * @param model
     */
    public ItemAlarmVM(Context context, Alarm model) {
        super(context, model);
    }

    @Override
    public void init() {

    }


    /***
     * Click event of an item of alarm
     *
     * @param view
     */
    public void onItemClick(View view) {
        context.startActivity(Henson.with(context)
                .gotoActivityAlarm().alarm(model).build());
    }


    @Bindable
    public String getName() {
        return model.getName();
    }

    @Bindable
    public String getAlarmTime() {
        return String.format("%d H %02d", model.getHour(), model.getMinute());
    }


    @Override
    public void destroy() {

    }
}
