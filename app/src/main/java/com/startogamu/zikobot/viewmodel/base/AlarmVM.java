package com.startogamu.zikobot.viewmodel.base;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.zikobot.core.event.alarm.EventAlarmSelect;
import com.startogamu.zikobot.core.receiver.AlarmReceiver;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.module.zikobot.manager.AlarmManager;
import com.startogamu.zikobot.module.zikobot.model.Alarm;
import com.startogamu.zikobot.module.zikobot.model.Track;

import org.greenrobot.eventbus.EventBus;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 29/05/16.
 */
public abstract class AlarmVM extends BaseVM<Alarm> {


    public String alarmName;

    public ObservableArrayList<TrackVM> tracksVms;
    public abstract ItemView itemView();

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
     * @param track
     */
    public void addTrack(Track track) {
        model.getTracks().add(track);
        save();
    }

    /***
     *
     */
    public void refreshTracks() {
        tracksVms.clear();
        for (Track track : model.getTracks()) {
            TrackVM itemTrackViewModel = new TrackVM(context, track);
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
        android.app.AlarmManager alarmMgr = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        AlarmManager.deleteAlarm(model);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA.ALARM_ID, model.getId());
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmMgr.cancel(alarmIntent);
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
        EventBus.getDefault().post(new EventAlarmSelect(model));
    }


    @Bindable
    public String getName() {
        return model.getName();
    }

    @Bindable
    public String getAlarmTime() {
        int hour = model.getHour();
        if (hour >= 12)
            hour -= 12;
        return String.format("%02d: %02d", hour, model.getMinute());
    }

    @Bindable
    public String getAlarmTimeAmPm() {
        return ZikoUtils.amPm(model.getHour());
    }

    public void updateStatus( boolean active) {

        model.setActive(active ? 1 : 0);
        model.save();
        AlarmManager.prepareAlarm(context,model);

    }

    @Bindable
    public boolean isActivated() {
        return model.getActive() == 1;
    }

    public void updateName(CharSequence charSequence) {
        alarmName = charSequence.toString();
        model.setName(alarmName);
    }

    /***
     * @param textView
     * @param day
     */
    public void handleTextClickDay(TextView textView, int day) {
        boolean status = !model.isDayActive(day);
        textView.setSelected(status);
        activeDay(day, status);
    }

    public void activeDay(int day, Boolean aBoolean) {
        model.activeDay(day, aBoolean);
        notifyChange();
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

    public boolean hasTracks() {
        return tracksVms.size() > 0;
    }

    @Bindable
    public int getVolume() {
        return model.getVolume();
    }

    public void updateRepeated(boolean checked) {
        model.setRepeated(checked ? 1 : 0);
        notifyChange();
    }


    public void updateRandom(boolean checked) {
        model.setRandomTrack(checked ? 1 : 0);
        notifyChange();
    }

    @Bindable
    public boolean isRepeated() {
        return model.getRepeated() == 1;
    }

    public void updateVolume(int progress) {
        model.setVolume(progress);
        notifyChange();
    }


}
