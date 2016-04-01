package com.startogamu.musicalarm.viewmodel.fragment;

import android.app.TimePickerDialog;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.f2prateek.dart.henson.Bundler;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.startogamu.musicalarm.BR;
import com.startogamu.musicalarm.databinding.FragmentAlarmInfoBinding;
import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.utils.EXTRA;
import com.startogamu.musicalarm.view.fragment.AlarmInfoFragment;
import com.startogamu.musicalarm.viewmodel.ViewModel;

import org.parceler.Parcels;

/**
 * Created by josh on 31/03/16.
 */
public class AlarmInfoViewModel extends BaseObservable implements ViewModel {
    private String alarmSelectedTime = "";
    private String alarmName = "";

    private AlarmInfoFragment context;
    private FragmentAlarmInfoBinding binding;

    Alarm alarm;

    public AlarmInfoViewModel(AlarmInfoFragment context, FragmentAlarmInfoBinding binding) {
        this.context = context;
        this.binding = binding;
        alarm = Parcels.unwrap(Bundler.copyOf(context.getArguments()).get().getParcelable(EXTRA.ALARM));
        updateSelectedTime(alarm);
        RxTextView.textChanges(binding.etName).skip(1).subscribe(charSequence -> {
            alarmName = charSequence.toString();
        });

    }


    /***
     * @param view
     */
    public void onTimeClick(View view) {
        new TimePickerDialog(context.getActivity(), (view1, hourOfDay, minute) -> {
            Log.d("test", "test");
            alarm.setHour(hourOfDay);
            alarm.setMinute(minute);
            updateSelectedTime(alarm);
        }, alarm.getHour(), alarm.getMinute(), true).show();
    }

    /***
     * @param alarm
     */
    private void updateSelectedTime(Alarm alarm) {
        alarmSelectedTime = String.format("%d H %02d", alarm.getHour(), alarm.getMinute());
        notifyPropertyChanged(BR.selectedTime);
    }

    @Bindable
    public String getSelectedTime() {
        return alarmSelectedTime;
    }

    @Bindable
    public String getAlarmName() {
        return alarmName;
    }


    @Override
    public void onDestroy() {

    }
}
