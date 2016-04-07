package com.startogamu.musicalarm.viewmodel.items;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.view.Henson;
import com.startogamu.musicalarm.viewmodel.ViewModel;

/**
 * Created by josh on 09/03/16.
 */
public class ItemAlarmViewModel extends BaseObservable implements ViewModel {

    private Alarm alarm;
    private Context context;

    public ItemAlarmViewModel(Context context, Alarm alarm) {
        this.alarm = alarm;
        this.context = context;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
        notifyChange();
    }

    /***
     * Click event of an item of alarm
     *
     * @param view
     */
    public void onItemClick(View view) {
        context.startActivity(Henson.with(context)
                .gotoAlarmActivity().alarm(alarm).build());
    }

    @Override
    public void onDestroy() {

    }


    @Bindable
    public String getName() {
        return alarm.getName();
    }

    @Bindable
    public String getAlarmTime() {
        return String.format("%d H %02d", alarm.getHour(), alarm.getMinute());
    }
}
