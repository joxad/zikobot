package com.startogamu.zikobot.alarm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.joxad.easydatabinding.base.BaseVM;
import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.event.alarm.EventAlarmSelect;
import com.startogamu.zikobot.core.receiver.AlarmReceiver;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.ZikoUtils;
import com.startogamu.zikobot.core.model.Alarm;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.localtracks.TrackVM;

import org.greenrobot.eventbus.EventBus;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 29/05/16.
 */
public abstract class AlarmVM extends BaseVM<Alarm> {

    public String alarmName;

    public ObservableArrayList<TrackVM> tracksVms;
    public ObservableBoolean isExpanded;
    public abstract ItemView itemView();

    public Alarm getModel() {
        return model;
    }
    /***
     * @param context
     * @param model
     */
    public AlarmVM(Context context, Alarm model) {
        super(context, model);
    }


    @Override
    public void init() {
        isExpanded = new ObservableBoolean(false);
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
        AlarmTrackManager.removeTrack(tracksVms.get(adapterPosition).getModel());
        try {
            tracksVms.get(adapterPosition).getModel().delete();
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        tracksVms.remove(adapterPosition);
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
        return AlarmManager.saveAlarm(model);
    }


    /***
     * Click event of an item of alarm
     *
     * @param view
     */
    public void onItemClick(View view) {
        EventBus.getDefault().post(new EventAlarmSelect(model, view.findViewById(R.id.iv_playlist)));
    }

    @Bindable
    public String getName() {
        return model.getName();
    }

    @Bindable
    public String getAlarmTime() {
        return String.format("%02d: %02d",ZikoUtils.amPmHour(model.getHour()), model.getMinute());
    }

    @Bindable
    public String getAlarmTimeAmPm() {
        return ZikoUtils.amPm(model.getHour());
    }

    public void updateStatus(boolean active) {
        if (model.getTracks().isEmpty()) {
            Toast.makeText(context, "Tu n'as pas de chanson sur cette alarme !", Toast.LENGTH_SHORT).show();
            model.setActive(0);
        } else {
            model.setActive(active ? 1 : 0);
            AlarmManager.prepareAlarm(context, model);

        }
        model.save();
        notifyChange();
    }

    @Bindable
    public boolean isActivated() {
        return model.getActive() == 1;
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

    @Bindable
    public String getImageUrl() {
        if ( !model.getTracks().isEmpty()){
            return model.getTracks().get(0).getImageUrl();
        }
        return null;
    }

    public void updateRandom(boolean checked) {
        model.setRandomTrack(checked ? 1 : 0);
        notifyChange();
    }

    public void expand(View view){
        isExpanded.set(!isExpanded.get());
    }

    @Bindable
    public boolean isRepeated() {
        return model.getRepeated() == 1;
    }

    public void updateVolume(int progress) {
        model.setVolume(progress);
        notifyChange();
    }

    public void updateName(String name) {
        model.setName(name);
        notifyChange();
    }


    @Override
    public void destroy() {

    }
}
