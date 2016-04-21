package com.startogamu.musicalarm.viewmodel.fragment;

import android.app.TimePickerDialog;
import android.view.View;

import com.f2prateek.dart.Dart;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.joxad.easydatabinding.utils.ObservableString;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.databinding.FragmentAlarmInfoBinding;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.view.fragment.AlarmInfoFragment;

import org.parceler.Parcels;

import java.util.Calendar;

import lombok.Getter;

/**
 * Created by josh on 31/03/16.
 */
public class AlarmInfoVM extends FragmentBaseVM<AlarmInfoFragment, FragmentAlarmInfoBinding> {
    public ObservableString selectedTime;
    public String alarmName;

    @Getter
    Alarm alarm;

    /***
     * @param fragment
     * @param binding
     */
    public AlarmInfoVM(AlarmInfoFragment fragment, FragmentAlarmInfoBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void init() {
        selectedTime = new ObservableString();
        initModel();

        RxTextView.textChanges(binding.etName).skip(1).subscribe(charSequence -> {
            alarmName = charSequence.toString();
            alarm.setName(alarmName);
        });
        RxCompoundButton.checkedChanges(binding.cbMonday).subscribe(aBoolean -> {
            alarm.activeDay(Calendar.MONDAY);
        });
        RxCompoundButton.checkedChanges(binding.cbTuesday).subscribe(aBoolean -> {
            alarm.activeDay(Calendar.TUESDAY);
        });
        RxCompoundButton.checkedChanges(binding.cbWednesday).subscribe(aBoolean -> {
            alarm.activeDay(Calendar.WEDNESDAY);
        });
        RxCompoundButton.checkedChanges(binding.cbThursday).subscribe(aBoolean -> {
            alarm.activeDay(Calendar.THURSDAY);
        });
        RxCompoundButton.checkedChanges(binding.cbFriday).subscribe(aBoolean -> {
            alarm.activeDay(Calendar.FRIDAY);
        });

        RxCompoundButton.checkedChanges(binding.cbSaturday).subscribe(aBoolean -> {
            alarm.activeDay(Calendar.SATURDAY);
        });
        RxCompoundButton.checkedChanges(binding.cbSunday).subscribe(aBoolean -> {
            alarm.activeDay(Calendar.SUNDAY);
        });
    }

    private void initModel() {
        alarm = Parcels.unwrap(Dart.get(fragment.getArguments(), EXTRA.ALARM));
        alarmName = alarm.getName();
        updateSelectedDays(alarm);
        updateSelectedTime(alarm);
    }

    private void updateSelectedDays(Alarm alarm) {
        binding.cbMonday.setChecked(alarm.isDayActive(Calendar.MONDAY));
        binding.cbTuesday.setChecked(alarm.isDayActive(Calendar.TUESDAY));
        binding.cbWednesday.setChecked(alarm.isDayActive(Calendar.WEDNESDAY));
        binding.cbThursday.setChecked(alarm.isDayActive(Calendar.THURSDAY));
        binding.cbFriday.setChecked(alarm.isDayActive(Calendar.FRIDAY));
        binding.cbSaturday.setChecked(alarm.isDayActive(Calendar.SATURDAY));
        binding.cbSunday.setChecked(alarm.isDayActive(Calendar.SUNDAY));
    }

    @Override
    public void destroy() {

    }


    /***
     * @param view
     */
    public void onTimeClick(View view) {
        new TimePickerDialog(fragment.getActivity(), (view1, hourOfDay, minute) -> {
            alarm.setHour(hourOfDay);
            alarm.setMinute(minute);
            updateSelectedTime(alarm);
        }, alarm.getHour(), alarm.getMinute(), true).show();
    }

    /***
     * @param alarm
     */
    private void updateSelectedTime(Alarm alarm) {
        selectedTime.set(String.format("%d H %02d", alarm.getHour(), alarm.getMinute()));
    }

}
