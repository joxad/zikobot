package com.startogamu.zikobot.player.alarm;

import android.os.Bundle;
import android.support.annotation.Nullable;

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
    }

    @Deprecated
    @Override
    public void onCreate(@Nullable Bundle savedInstance) {

    }
}
