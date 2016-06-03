package com.startogamu.musicalarm.viewmodel.base;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.module.alarm.manager.AlarmManager;
import com.startogamu.musicalarm.module.alarm.model.Alarm;
import com.startogamu.musicalarm.module.alarm.model.AlarmTrack;
import com.startogamu.musicalarm.view.Henson;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 29/05/16.
 */
public class AlarmVM extends BaseVM<Alarm> {


    public String alarmName;

    public ObservableArrayList<TrackVM> tracksVms;
    public ItemView itemView = ItemView.of(BR.trackVM, R.layout.item_track);


    /***
     * @param context
     * @param model
     */
    public AlarmVM(Context context, Alarm model) {
        super(context, model);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init() {
        tracksVms = new ObservableArrayList<>();
    }

    public void updateTimeSelected(int currentHour, int currentMin) {
        model.setMinute(currentMin);
        model.setHour(currentHour);
    }

    /**
     * Init the VM according to the alarm
     */
    public void initModel() {
        alarmName = model.getName();
        refreshTracks();
    }


    /***
     * TRACK MANAGEMENT
     */

    /**
     * Add track
     *
     * @param alarmTrack
     */
    public void addTrack(AlarmTrack alarmTrack) {
        model.getTracks().add(alarmTrack);
        save();
    }

    /***
     *
     */
    public void refreshTracks() {
        tracksVms.clear();
        for (AlarmTrack alarmTrack : model.getTracks()) {
            TrackVM itemTrackViewModel = new TrackVM(context, alarmTrack);
            tracksVms.add(itemTrackViewModel);
        }
    }


    /***
     * @param adapterPosition
     */
    public void removeTrack(int adapterPosition) {
        tracksVms.remove(adapterPosition);
        model.getTracks().get(adapterPosition).delete();
    }


    /***
     * DB Methods
     */

    public void delete() {
        AlarmManager.deleteAlarm(model);
    }


    /***
     * Save the model to the DB
     *
     * @return
     */
    public rx.Observable<Alarm> save() {
        return AlarmManager.saveAlarm(model, model.getTracks());
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
        return String.format("%d : %02d", model.getHour(), model.getMinute());
    }

    public void updateStatus(boolean activated) {
        model.setActive(activated ? 1 : 0);
        model.save();
    }

    @Bindable
    public boolean isActivated() {
        return model.getActive() == 1;
    }

    public void updateName(CharSequence charSequence) {
        alarmName = charSequence.toString();
        model.setName(alarmName);
    }

    public void activeDay(int day, Boolean aBoolean) {
        model.activeDay(day, aBoolean);
    }

    public int getMinute() {
        return model.getMinute();
    }

    public int getHour() {
        return model.getHour();
    }

    public boolean isDayActive(int day) {
        return model.isDayActive(day);
    }


}
