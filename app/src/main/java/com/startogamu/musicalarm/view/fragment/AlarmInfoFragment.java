package com.startogamu.musicalarm.view.fragment;

import com.f2prateek.dart.henson.Bundler;
import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.musicalarm.BR;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.databinding.FragmentAlarmInfoBinding;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.viewmodel.fragment.AlarmInfoVM;

import org.parceler.Parcels;

/**
 * Created by josh on 31/03/16.
 */
public class AlarmInfoFragment extends FragmentBase<FragmentAlarmInfoBinding, AlarmInfoVM> {

    public static AlarmInfoFragment newInstance(Alarm alarm) {
        AlarmInfoFragment fragment = new AlarmInfoFragment();
        fragment.setArguments(Bundler.create().put(EXTRA.ALARM, Parcels.wrap(alarm)).get());
        return fragment;
    }

    @Override
    public int data() {
        return BR.alarmInfoVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_alarm_info;
    }

    @Override
    public AlarmInfoVM baseFragmentVM(FragmentAlarmInfoBinding binding) {
        return new AlarmInfoVM(this, binding);
    }

    public Alarm getAlarm() {
        return vm.getAlarm();
    }
}
