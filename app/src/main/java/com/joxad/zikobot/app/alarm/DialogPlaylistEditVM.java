package com.joxad.zikobot.app.alarm;

import android.content.Context;
import android.databinding.Bindable;
import android.media.AudioManager;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.joxad.easydatabinding.bottomsheet.DialogBottomSheetBaseVM;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.joxad.zikobot.app.core.utils.SimpleSeekBarListener;
import com.joxad.zikobot.app.databinding.DialogPlaylistEditBinding;
import com.joxad.zikobot.data.model.Alarm;

import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 16/08/16.
 */
public class DialogPlaylistEditVM extends DialogBottomSheetBaseVM<DialogPlaylistEdit, DialogPlaylistEditBinding> {

    public AlarmVM alarmVM;
    Alarm alarm;
    private AudioManager am;

    /***
     * @param fragment
     * @param binding
     */
    public DialogPlaylistEditVM(DialogPlaylistEdit fragment, DialogPlaylistEditBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void onCreate() {
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
        binding.viewAlarm.swRandom.setChecked(alarm.getRandomTrack() == 1);
        initHour();

        RxCompoundButton.checkedChanges(binding.viewAlarm.swRandom).subscribe(aBoolean -> {
            alarmVM.updateRandom(aBoolean);
        });

        binding.viewAlarm.seekBarVolume.setOnSeekBarChangeListener(new SimpleSeekBarListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    alarmVM.updateVolume(progress);
            }
        });
    }


    public void editAlarm(@SuppressWarnings("unused") View view) {
        alarmVM.updateStatus(!alarmVM.isActivated());
        notifyChange();
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
        alarmVM.updateStatus(alarmVM.isActivated());
        alarmVM.updateRepeated(true);
        return alarmVM.save();
    }
}
