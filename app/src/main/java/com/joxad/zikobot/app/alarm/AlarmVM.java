package com.joxad.zikobot.app.alarm;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.base.BaseVM;
import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.alarm.event.EventAlarmSelect;
import com.joxad.zikobot.app.alarm.event.EventEditAlarm;
import com.joxad.zikobot.app.core.utils.ZikoUtils;
import com.joxad.zikobot.app.localtracks.TrackVM;
import com.joxad.zikobot.data.model.Alarm;
import com.joxad.zikobot.data.model.Track;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 29/05/16.
 */
public abstract class AlarmVM extends BaseVM<Alarm> {

    public String alarmName;

    public ObservableArrayList<TrackVM> tracksVms;
    public ObservableBoolean isExpanded;
    private NotificationManager notificationManager;

    /***
     * @param context
     * @param model
     */
    public AlarmVM(Context context, Alarm model) {
        super(context, model);
    }

    public abstract ItemView itemView();

    public Alarm getModel() {
        return model;
    }

    @Override
    public void onCreate() {
        isExpanded = new ObservableBoolean(false);
        tracksVms = new ObservableArrayList<>();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

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
        AlarmManager.deleteAlarm(model);

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
        EventBus.getDefault().post(new EventAlarmSelect(model, view));
    }

    public void showDialog(View view) {
        EventBus.getDefault().post(new EventEditAlarm(model));

    }

    @Bindable
    public String getName() {
        return model.getName();
    }

    @Bindable
    public String getAlarmTime() {
        return String.format("%02d: %02d", ZikoUtils.amPmHour(model.getHour()), model.getMinute());
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
//test notification
            if (model.getActive() == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                        && !notificationManager.isNotificationPolicyAccessGranted()) {

                    Intent intent = new Intent(
                            android.provider.Settings
                                    .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

                    context.startActivity(intent);
                }
            }

        }
        model.save();
        notifyChange();
    }


    @Bindable
    public boolean isActivated() {
        return model.getActive() == 1;
    }


    public void activeDay(int day, Boolean aBoolean) {
        model.activeDay(day, aBoolean);
        model.save();
        notifyChange();
    }


    @Bindable
    public int getMinute() {
        return model.getMinute();
    }

    @Bindable
    public int getHour() {
        return model.getHour();
    }

    public void activeMonday(@SuppressWarnings("unused") View view) {
        activeDay(Calendar.MONDAY, !isMondayActive());
        notifyPropertyChanged(BR.mondayActive);
    }

    public void activeTuesday(@SuppressWarnings("unused") View view) {
        activeDay(Calendar.TUESDAY, !isTuesdayActive());
        notifyPropertyChanged(BR.tuesdayActive);
    }

    public void activeWed(@SuppressWarnings("unused") View view) {
        activeDay(Calendar.WEDNESDAY, !isWedActive());
        notifyPropertyChanged(BR.wedActive);
    }

    public void activeThursday(@SuppressWarnings("unused") View view) {
        activeDay(Calendar.THURSDAY, !isThursdayActive());
        notifyPropertyChanged(BR.thursdayActive);
    }

    public void activeFriday(@SuppressWarnings("unused") View view) {
        activeDay(Calendar.FRIDAY, !isFridayActive());
        notifyPropertyChanged(BR.fridayActive);
    }

    public void activeSaturday(@SuppressWarnings("unused") View view) {
        activeDay(Calendar.SATURDAY, !isSaturdayActive());
        notifyPropertyChanged(BR.saturdayActive);
    }

    public void activeSunday(@SuppressWarnings("unused") View view) {
        activeDay(Calendar.SUNDAY, !isSundayActive());
        notifyPropertyChanged(BR.sundayActive);
    }

    @Bindable
    public boolean isMondayActive() {
        return model.isDayActive(Calendar.MONDAY);
    }

    @Bindable
    public boolean isTuesdayActive() {
        return model.isDayActive(Calendar.TUESDAY);
    }

    @Bindable
    public boolean isWedActive() {
        return model.isDayActive(Calendar.WEDNESDAY);
    }

    @Bindable
    public boolean isThursdayActive() {
        return model.isDayActive(Calendar.THURSDAY);
    }

    @Bindable
    public boolean isFridayActive() {
        return model.isDayActive(Calendar.FRIDAY);
    }

    @Bindable
    public boolean isSaturdayActive() {
        return model.isDayActive(Calendar.SATURDAY);
    }

    @Bindable
    public boolean isSundayActive() {
        return model.isDayActive(Calendar.SUNDAY);
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
        if (!model.getTracks().isEmpty()) {
            return model.getTracks().get(0).getImageUrl();
        }
        return null;
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

    public void updateName(String name) {
        model.setName(name);
        notifyChange();
    }

    @Bindable
    public String getTransition() {
        return context.getString(R.string.transition) + model.getId();
    }


    public void updateModel(Alarm alarm) {
        model = alarm;
        notifyChange();
    }
}
