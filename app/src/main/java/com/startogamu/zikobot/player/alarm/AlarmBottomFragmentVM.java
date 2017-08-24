package com.startogamu.zikobot.player.alarm;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.SeekBar;

import com.joxad.easydatabinding.bottomsheet.DialogBottomSheetBaseVM;
import com.joxad.zikobot.data.db.AlarmManager;
import com.joxad.zikobot.data.db.model.ZikoAlarm;
import com.startogamu.zikobot.Constants;
import com.startogamu.zikobot.databinding.AlarmFragmentBinding;

/**
 * Created by Jocelyn on 22/08/2017.
 */

public class AlarmBottomFragmentVM extends DialogBottomSheetBaseVM<AlarmBottomFragment, AlarmFragmentBinding> {

    public AlarmVM alarmVM;
    private AudioManager am;

    /***
     * @param fragment
     * @param binding
     */
    public AlarmBottomFragmentVM(AlarmBottomFragment fragment, AlarmFragmentBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void onCreate() {
        ZikoAlarm alarm = AlarmManager.INSTANCE.getAlarmByPlaylistId(fragment.getArguments().getLong(Constants.Extra.INSTANCE.getPLAYLIST_ID()));
        alarmVM = new AlarmVM(fragment.getContext(), alarm);
        am = (AudioManager) fragment.getActivity().getSystemService(Context.AUDIO_SERVICE);
        init();
    }


    private void init() {
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        binding.seekBarVolume.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        binding.seekBarVolume.setProgress(alarmVM.getVolume() == -1 ? ((int) (max * 0.5f)) : alarmVM.getVolume());
        binding.swRandom.setChecked(alarmVM.getRandomTrack() == 1);
        binding.timePicker.setIs24HourView(true);
        initHour();

        binding.swRandom.setOnCheckedChangeListener((compoundButton, b) -> alarmVM.updateRandom(b));

        binding.seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b)
                    alarmVM.updateVolume(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initHour() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            binding.timePicker.setMinute(alarmVM.getMinute());
            binding.timePicker.setHour(alarmVM.getHour());
        } else {
            binding.timePicker.setCurrentHour(alarmVM.getHour());
            binding.timePicker.setCurrentMinute(alarmVM.getMinute());
        }
    }

    @Deprecated
    @Override
    public void onCreate(@Nullable Bundle savedInstance) {

    }
}
