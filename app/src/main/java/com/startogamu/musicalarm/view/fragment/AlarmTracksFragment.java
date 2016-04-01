package com.startogamu.musicalarm.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f2prateek.dart.henson.Bundler;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentAlarmTracksBinding;
import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.utils.EXTRA;
import com.startogamu.musicalarm.viewmodel.fragment.AlarmTracksViewModel;

import org.parceler.Parcels;

/**
 * Created by josh on 31/03/16.
 */
public class AlarmTracksFragment extends Fragment {

    /***
     *
     */
    private FragmentAlarmTracksBinding binding;
    AlarmTracksViewModel alarmTracksViewModel;

    /***
     * @return
     * @param alarm
     */
    public static AlarmTracksFragment newInstance(Alarm alarm) {
        AlarmTracksFragment fragment = new AlarmTracksFragment();
       fragment.setArguments(Bundler.create().put(EXTRA.ALARM, Parcels.wrap(alarm)).get());

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_tracks, container, false);
        alarmTracksViewModel = new AlarmTracksViewModel(this, binding);
        binding.setAlarmTracksViewModel(alarmTracksViewModel);
        return binding.getRoot();
    }
}
