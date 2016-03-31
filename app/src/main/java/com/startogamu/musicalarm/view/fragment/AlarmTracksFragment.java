package com.startogamu.musicalarm.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentAlarmTracksBinding;
import com.startogamu.musicalarm.viewmodel.fragment.AlarmTracksViewModel;

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
     */
    public static AlarmTracksFragment newInstance() {
        AlarmTracksFragment fragment = new AlarmTracksFragment();
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
