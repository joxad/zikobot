package com.startogamu.musicalarm.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.databinding.FragmentLocalMusicBinding;
import com.startogamu.musicalarm.module.alarm.AlarmTrack;
import com.startogamu.musicalarm.module.alarm.LocalTrack;
import com.startogamu.musicalarm.viewmodel.fragment.LocalMusicViewModel;

import org.parceler.Parcels;

/**
 * Created by josh on 26/03/16.
 */
public class LocalMusicFragment extends Fragment {

    public static final String TAG = LocalMusicFragment.class.getSimpleName();
    LocalMusicViewModel localMusicViewModel;

    FragmentLocalMusicBinding binding;


    public static LocalMusicFragment newInstance() {
        LocalMusicFragment fragment = new LocalMusicFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_local_music, container, false);
        localMusicViewModel = new LocalMusicViewModel(this, binding);
        binding.setLocalMusicViewModel(localMusicViewModel);
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();
    }

    /***
     * We create an alarm track using the localtrack model
     * @param track
     */
    public void selectTrack(LocalTrack track) {
        AlarmTrack alarmTrack = new AlarmTrack();
        alarmTrack.setType(AlarmTrack.TYPE.LOCAL);
        alarmTrack.setRef(track.getData());
        alarmTrack.setImageUrl(track.getArtPath());
        alarmTrack.setArtistName(track.getArtist());
        alarmTrack.setName(track.getTitle());
        Intent intent = new Intent();
        intent.putExtra(EXTRA.TRACK, Parcels.wrap(alarmTrack));
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    public void loadMusic() {
        localMusicViewModel.loadLocalMusic();
    }
}
