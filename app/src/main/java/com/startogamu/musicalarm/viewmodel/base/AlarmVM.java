package com.startogamu.musicalarm.viewmodel.base;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.joxad.easydatabinding.base.BaseVM;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ItemAlarmBinding;
import com.startogamu.musicalarm.databinding.ViewAlarmBinding;
import com.startogamu.musicalarm.module.alarm.manager.AlarmManager;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.alarm.object.AlarmTrack;
import com.startogamu.musicalarm.view.Henson;

import java.util.Calendar;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.functions.Action1;

/**
 * Created by josh on 29/05/16.
 */
public class AlarmVM extends BaseVM<Alarm> {
    ViewAlarmBinding binding;


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

    /***
     * @param binding
     */
    public void initViewAlarmBinding(ViewAlarmBinding binding) {
        this.binding = binding;
        initModel();

        RxTextView.textChanges(binding.etName).skip(1).subscribe(charSequence -> {
            alarmName = charSequence.toString();
            model.setName(alarmName);
        });
        RxCompoundButton.checkedChanges(binding.cbMonday).subscribe(aBoolean -> {
            model.activeDay(Calendar.MONDAY, aBoolean);
        });
        RxCompoundButton.checkedChanges(binding.cbTuesday).subscribe(aBoolean -> {
            model.activeDay(Calendar.TUESDAY, aBoolean);
        });
        RxCompoundButton.checkedChanges(binding.cbWednesday).subscribe(aBoolean -> {
            model.activeDay(Calendar.WEDNESDAY, aBoolean);
        });
        RxCompoundButton.checkedChanges(binding.cbThursday).subscribe(aBoolean -> {
            model.activeDay(Calendar.THURSDAY, aBoolean);
        });
        RxCompoundButton.checkedChanges(binding.cbFriday).subscribe(aBoolean -> {
            model.activeDay(Calendar.FRIDAY, aBoolean);
        });

        RxCompoundButton.checkedChanges(binding.cbSaturday).subscribe(aBoolean -> {
            model.activeDay(Calendar.SATURDAY, aBoolean);
        });
        RxCompoundButton.checkedChanges(binding.cbSunday).subscribe(aBoolean -> {
            model.activeDay(Calendar.SUNDAY, aBoolean);
        });
    }


    public void initItemAlarmBinding(ItemAlarmBinding binding) {
        RxCompoundButton.checkedChanges(binding.swActivated).subscribe(status -> {
            updateStatus(status);
        });
    }

    private void updateTimeSelected(int currentHour, int currentMin) {
        model.setMinute(currentMin);
        model.setHour(currentHour);
    }

    /**
     * Init the VM according to the alarm
     */
    private void initModel() {
        alarmName = model.getName();
        updateSelectedDays();
        refreshTracks();
        initHour();

    }

    private void initHour() {
        Calendar alarmDate = Calendar.getInstance();
        alarmDate.set(Calendar.HOUR, model.getHour());
        alarmDate.set(Calendar.MINUTE, model.getMinute());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            binding.timePicker.setMinute(model.getMinute());
            binding.timePicker.setHour(model.getHour());

        } else {
            binding.timePicker.setCurrentHour(Integer.valueOf(model.getHour()));
            binding.timePicker.setCurrentMinute(Integer.valueOf(model.getMinute()));

        }
    }

    /***
     * Check the activated days
     */
    private void updateSelectedDays() {
        binding.cbMonday.setChecked(model.isDayActive(Calendar.MONDAY));
        binding.cbTuesday.setChecked(model.isDayActive(Calendar.TUESDAY));
        binding.cbWednesday.setChecked(model.isDayActive(Calendar.WEDNESDAY));
        binding.cbThursday.setChecked(model.isDayActive(Calendar.THURSDAY));
        binding.cbFriday.setChecked(model.isDayActive(Calendar.FRIDAY));
        binding.cbSaturday.setChecked(model.isDayActive(Calendar.SATURDAY));
        binding.cbSunday.setChecked(model.isDayActive(Calendar.SUNDAY));
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
        model.getTracks().remove(adapterPosition);
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
        int min = 0;

        int hour = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = binding.timePicker.getHour();
            min = binding.timePicker.getMinute();

        } else {
            hour = binding.timePicker.getCurrentHour();
            min = binding.timePicker.getCurrentMinute();

        }

        Log.d(AlarmVM.class.getSimpleName(), "hours " + hour + "minu" + min);
        updateTimeSelected(hour, min);
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

}
