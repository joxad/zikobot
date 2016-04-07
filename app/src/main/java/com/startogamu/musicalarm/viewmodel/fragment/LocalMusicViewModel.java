package com.startogamu.musicalarm.viewmodel.fragment;

import android.app.Fragment;
import android.databinding.BaseObservable;
import android.util.Log;

import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.databinding.FragmentLocalMusicBinding;
import com.startogamu.musicalarm.di.manager.LocalMusicManager;
import com.startogamu.musicalarm.model.LocalTrack;
import com.startogamu.musicalarm.viewmodel.ViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by josh on 26/03/16.
 */
public class LocalMusicViewModel extends BaseObservable implements ViewModel {

    private static final String TAG = LocalMusicViewModel.class.getSimpleName();
    private Fragment fragment;
    private FragmentLocalMusicBinding binding;

    @Inject
    LocalMusicManager localMusicManager;

    public LocalMusicViewModel(Fragment fragment, FragmentLocalMusicBinding binding) {

        this.fragment = fragment;
        this.binding = binding;
        MusicAlarmApplication.get(fragment.getActivity()).contentComponent.inject(this);
        loadLocalMusic();
    }

    private void loadLocalMusic() {
        localMusicManager.getLocalTracks().subscribe(new Subscriber<List<LocalTrack>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,""+e.getLocalizedMessage());

            }

            @Override
            public void onNext(List<LocalTrack> localTracks) {
                Log.d(TAG,""+localTracks.size());
            }
        });
    }

    @Override
    public void onDestroy() {

    }
}
