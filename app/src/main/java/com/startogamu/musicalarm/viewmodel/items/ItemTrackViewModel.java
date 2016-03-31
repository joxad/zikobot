package com.startogamu.musicalarm.viewmodel.items;

import android.databinding.BaseObservable;

import com.startogamu.musicalarm.databinding.FragmentAlarmTracksBinding;
import com.startogamu.musicalarm.view.fragment.AlarmTracksFragment;
import com.startogamu.musicalarm.viewmodel.ViewModel;

/***
 *
 */
public class ItemTrackViewModel extends BaseObservable implements ViewModel {


    private AlarmTracksFragment fragment;
    private FragmentAlarmTracksBinding binding;

    /***
     * @param fragment
     * @param binding
     */
    public ItemTrackViewModel(AlarmTracksFragment fragment, FragmentAlarmTracksBinding binding) {
        this.fragment = fragment;
        this.binding = binding;
    }


    @Override
    public void onDestroy() {

    }
}
