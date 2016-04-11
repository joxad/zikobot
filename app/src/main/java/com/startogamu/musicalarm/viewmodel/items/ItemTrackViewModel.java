package com.startogamu.musicalarm.viewmodel.items;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.startogamu.musicalarm.databinding.FragmentAlarmTracksBinding;
import com.startogamu.musicalarm.module.alarm.AlarmTrack;
import com.startogamu.musicalarm.view.fragment.AlarmTracksFragment;
import com.startogamu.musicalarm.viewmodel.ViewModel;

import lombok.Getter;

/***
 * ViewModel that will show an {@link AlarmTrack}
 */
public class ItemTrackViewModel extends BaseObservable implements ViewModel {

    @Getter
    private AlarmTrack alarmTrack;

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

    /***
     * Set the model
     *
     * @param alarmTrack
     */
    public void setAlarmTrack(AlarmTrack alarmTrack) {
        this.alarmTrack = alarmTrack;
        notifyChange();
    }

    @Bindable
    public String getName() {
        return alarmTrack.getName();
    }


    @Bindable
    public String getImageUrl() {
        return alarmTrack.getImageUrl();
    }



    @Override
    public void onDestroy() {

    }

}
