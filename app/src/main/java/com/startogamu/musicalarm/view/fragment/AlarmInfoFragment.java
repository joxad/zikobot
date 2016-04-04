package com.startogamu.musicalarm.view.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f2prateek.dart.henson.Bundler;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentAlarmInfoBinding;
import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.utils.EXTRA;
import com.startogamu.musicalarm.viewmodel.fragment.AlarmInfoViewModel;

import org.parceler.Parcels;

/**
 * Created by josh on 31/03/16.
 */
public class AlarmInfoFragment extends Fragment {


    AlarmInfoViewModel alarmInfoViewModel;
    FragmentAlarmInfoBinding binding;

    public static AlarmInfoFragment newInstance(Alarm alarm) {
        AlarmInfoFragment fragment = new AlarmInfoFragment();
        fragment.setArguments(Bundler.create().put(EXTRA.ALARM, Parcels.wrap(alarm)).get());
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_info, container, false);
        alarmInfoViewModel = new AlarmInfoViewModel(this, binding);
        alarmInfoViewModel.setAlarm(Parcels.unwrap(getArguments().getParcelable(EXTRA.ALARM)));
        binding.setAlarmInfoViewModel(alarmInfoViewModel);
        return binding.getRoot();

    }

    public Alarm getAlarm() {
        return alarmInfoViewModel.getAlarm();
    }
}
