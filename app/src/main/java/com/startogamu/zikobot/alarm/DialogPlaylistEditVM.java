package com.startogamu.zikobot.alarm;

import android.content.Context;
import android.databinding.Bindable;
import android.media.AudioManager;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.joxad.easydatabinding.bottomsheet.DialogBottomSheetBaseVM;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.core.utils.SimpleSeekBarListener;
import com.startogamu.zikobot.databinding.DialogPlaylistEditBinding;
import com.startogamu.zikobot.core.model.Alarm;

import org.parceler.Parcels;

import java.util.Calendar;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 16/08/16.
 */
public class DialogPlaylistEditVM extends DialogBottomSheetBaseVM<DialogPlaylistEdit, DialogPlaylistEditBinding> {

    Alarm alarm;
    public AlarmVM alarmVM;
    private AudioManager am;

    /***
     * @param fragment
     * @param binding
     */
    public DialogPlaylistEditVM(DialogPlaylistEdit fragment, DialogPlaylistEditBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void init() {
        am = (AudioManager) fragment.getActivity().getSystemService(Context.AUDIO_SERVICE);

        alarm = Parcels.unwrap(fragment.getArguments().getParcelable(EXTRA.ALARM));
        alarmVM = new AlarmVM(fragment.getContext(), alarm) {
            @Override
            public ItemView itemView() {
                return null;
            }
        };
        initAlarmVM();
    }


    private void initAlarmVM() {
        alarmVM.initModel();
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        binding.viewAlarm.seekBarVolume.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        binding.viewAlarm.seekBarVolume.setProgress(alarm.getVolume() == -1 ? ((int) (max * 0.5f)) : alarm.getVolume());
        binding.viewAlarm.swRepeat.setChecked(alarm.getRepeated() == 1);
        binding.viewAlarm.swRandom.setChecked(alarm.getRandomTrack() == 1);
        initHour();
        initSelectedDays();
        RxCompoundButton.checkedChanges(binding.viewAlarm.swRepeat).subscribe(aBoolean -> {
            alarmVM.updateRepeated(aBoolean);
        });
        RxCompoundButton.checkedChanges(binding.viewAlarm.swRandom).subscribe(aBoolean -> {
            alarmVM.updateRandom(aBoolean);
        });
        RxView.clicks(binding.viewAlarm.cbMonday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbMonday, Calendar.MONDAY);
        });
        RxView.clicks(binding.viewAlarm.cbTuesday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbTuesday, Calendar.TUESDAY);
        });
        RxView.clicks(binding.viewAlarm.cbWednesday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbWednesday, Calendar.WEDNESDAY);
        });
        RxView.clicks(binding.viewAlarm.cbThursday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbThursday, Calendar.THURSDAY);
        });
        RxView.clicks(binding.viewAlarm.cbFriday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbFriday, Calendar.FRIDAY);
        });
        RxView.clicks(binding.viewAlarm.cbSaturday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbSaturday, Calendar.SATURDAY);
        });
        RxView.clicks(binding.viewAlarm.cbSunday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbSunday, Calendar.SUNDAY);
        });

        binding.viewAlarm.seekBarVolume.setOnSeekBarChangeListener(new SimpleSeekBarListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    alarmVM.updateVolume(progress);
            }
        });
    }


    private void initHour() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            binding.viewAlarm.timePicker.setMinute(alarmVM.getMinute());
            binding.viewAlarm.timePicker.setHour(alarmVM.getHour());
        } else {
            binding.viewAlarm.timePicker.setCurrentHour(Integer.valueOf(alarmVM.getHour()));
            binding.viewAlarm.timePicker.setCurrentMinute(Integer.valueOf(alarmVM.getMinute()));
        }
    }


    /***
     * Check the activated days
     */
    private void initSelectedDays() {
        binding.viewAlarm.cbMonday.setSelected(alarmVM.isDayActive(Calendar.MONDAY));
        binding.viewAlarm.cbTuesday.setSelected(alarmVM.isDayActive(Calendar.TUESDAY));
        binding.viewAlarm.cbWednesday.setSelected(alarmVM.isDayActive(Calendar.WEDNESDAY));
        binding.viewAlarm.cbThursday.setSelected(alarmVM.isDayActive(Calendar.THURSDAY));
        binding.viewAlarm.cbFriday.setSelected(alarmVM.isDayActive(Calendar.FRIDAY));
        binding.viewAlarm.cbSaturday.setSelected(alarmVM.isDayActive(Calendar.SATURDAY));
        binding.viewAlarm.cbSunday.setSelected(alarmVM.isDayActive(Calendar.SUNDAY));
    }

    public void onSave(View view) {
        save().subscribe(alarm1 -> {
            fragment.dismiss();
        }, throwable -> {
            fragment.dismiss();
        });
    }

    @Bindable
    public String getAlarmName() {
        return alarm.getName();
    }

    @Override
    public void destroy() {

    }


    /***
     * Use this method to save the data
     */
    public rx.Observable<Alarm> save() {

        int min = 0;
        int hour = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = binding.viewAlarm.timePicker.getHour();
            min = binding.viewAlarm.timePicker.getMinute();

        } else {
            hour = binding.viewAlarm.timePicker.getCurrentHour();
            min = binding.viewAlarm.timePicker.getCurrentMinute();

        }
        Log.d(AlarmVM.class.getSimpleName(), "hours " + hour + "minu" + min);
        alarmVM.updateName(binding.etName.getText().toString());
        alarmVM.updateTimeSelected(hour, min);
        alarmVM.updateRepeated(binding.viewAlarm.swRepeat.isChecked());
        alarmVM.updateStatus(alarmVM.isActivated());
        return alarmVM.save();
    }
}
