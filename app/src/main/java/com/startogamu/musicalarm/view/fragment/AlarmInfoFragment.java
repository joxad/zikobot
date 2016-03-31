package com.startogamu.musicalarm.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentAlarmInfoBinding;
import com.startogamu.musicalarm.viewmodel.fragment.AlarmInfoViewModel;

/**
 * Created by josh on 31/03/16.
 */
public class AlarmInfoFragment extends Fragment {


    AlarmInfoViewModel alarmInfoViewModel;
    FragmentAlarmInfoBinding binding;

    public static AlarmInfoFragment newInstance() {
        AlarmInfoFragment fragment = new AlarmInfoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_info, container, false);
        alarmInfoViewModel = new AlarmInfoViewModel(this, binding);
        binding.setAlarmInfoViewModel(alarmInfoViewModel);
        return binding.getRoot();

    }
}
