package com.startogamu.musicalarm.view.fragment;

import com.f2prateek.dart.henson.Bundler;
import com.joxad.easydatabinding.fragment.FragmentBase;
import com.startogamu.musicalarm.BR;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.databinding.FragmentAlarmTracksBinding;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.alarm.object.AlarmTrack;
import com.startogamu.musicalarm.viewmodel.fragment.AlarmTracksVM;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by josh on 31/03/16.
 */
public class AlarmTracksFragment extends FragmentBase<FragmentAlarmTracksBinding, AlarmTracksVM> {

    /***
     * @param alarm
     * @return
     */
    public static AlarmTracksFragment newInstance(Alarm alarm) {
        AlarmTracksFragment fragment = new AlarmTracksFragment();
        fragment.setArguments(Bundler.create().put(EXTRA.ALARM, Parcels.wrap(alarm)).get());
        return fragment;
    }


    @Override
    public int data() {
        return BR.alarmTracksVM;
    }

    @Override
    public int layoutResources() {
        return R.layout.fragment_alarm_tracks;
    }

    @Override
    public AlarmTracksVM baseFragmentVM(FragmentAlarmTracksBinding binding) {
        return new AlarmTracksVM(this, binding);
    }

    public void add(AlarmTrack alarmTrack) {
        vm.add(alarmTrack);
    }

    public List<AlarmTrack> getTracks() {
        return vm.getAlarmTracks();
    }
}
