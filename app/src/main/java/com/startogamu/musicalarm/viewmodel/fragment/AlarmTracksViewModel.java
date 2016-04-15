package com.startogamu.musicalarm.viewmodel.fragment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentAlarmTracksBinding;
import com.startogamu.musicalarm.module.alarm.manager.AlarmManager;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.alarm.object.AlarmTrack;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.view.fragment.AlarmTracksFragment;
import com.startogamu.musicalarm.viewmodel.ViewModel;
import com.startogamu.musicalarm.viewmodel.items.ItemTrackViewModel;

import java.util.ArrayList;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 31/03/16.
 */

/***
 * {@link AlarmTracksViewModel}  make the link between {@link AlarmTracksFragment} and {@link AlarmManager}
 */
public class AlarmTracksViewModel extends BaseObservable implements ViewModel {

    private final FragmentAlarmTracksBinding binding;
    public String TAG = AlarmTracksViewModel.class.getSimpleName();
    AlarmTracksFragment alarmTracksFragment;

    /***
     * The observable list of tracks selected for this alarm
     */
    private ObservableArrayList<ItemTrackViewModel> tracks = new ObservableArrayList<>();
    Alarm alarm;


    /***
     * @param context
     * @param binding
     */
    public AlarmTracksViewModel(AlarmTracksFragment context, FragmentAlarmTracksBinding binding) {
        this.alarmTracksFragment = context;
        this.binding = binding;
        Injector.INSTANCE.playerComponent().init(this);

    }

    /***
     * Init the viewmodel with its alarm
     *
     * @param alarm
     */
    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
        for (AlarmTrack alarmTrack : alarm.getTracks()) {
            ItemTrackViewModel itemTrackViewModel = new ItemTrackViewModel(alarmTracksFragment, binding);
            itemTrackViewModel.setAlarmTrack(alarmTrack);
            tracks.add(itemTrackViewModel);
        }
    }


    public void onPlayClick(View view) {
        Injector.INSTANCE.playerComponent().manager().startAlarm(alarm);

    }

    public void onDestroy() {

    }

    @Bindable
    public ObservableArrayList<ItemTrackViewModel> getAlarmTracks() {
        return tracks;
    }

    public ItemView itemTracksBinder = ItemView.of(BR.itemTrackViewModel, R.layout.item_track);

    /**
     * @param alarmTrack
     */
    public void add(AlarmTrack alarmTrack) {
        ItemTrackViewModel itemTrackViewModel = new ItemTrackViewModel(alarmTracksFragment, binding);
        itemTrackViewModel.setAlarmTrack(alarmTrack);
        tracks.add(itemTrackViewModel);
        notifyPropertyChanged(BR.alarmTracksViewModel);
    }

    public ArrayList<AlarmTrack> getTracks() {
        ArrayList<AlarmTrack> alarmTracks = new ArrayList<>();
        for (ItemTrackViewModel trackViewModel : tracks) {
            alarmTracks.add(trackViewModel.getAlarmTrack());
        }
        return alarmTracks;
    }
}
