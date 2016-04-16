package com.startogamu.musicalarm.viewmodel.fragment;

import android.databinding.ObservableArrayList;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.f2prateek.dart.Dart;
import com.joxad.easydatabinding.fragment.FragmentBaseVM;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.databinding.FragmentAlarmTracksBinding;
import com.startogamu.musicalarm.module.alarm.manager.AlarmManager;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.alarm.object.AlarmTrack;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.view.fragment.AlarmTracksFragment;
import com.startogamu.musicalarm.viewmodel.items.ItemTrackViewModel;

import org.parceler.Parcels;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 31/03/16.
 */

/***
 * {@link AlarmTracksVM}  make the link between {@link AlarmTracksFragment} and {@link AlarmManager}
 */
public class AlarmTracksVM extends FragmentBaseVM<AlarmTracksFragment, FragmentAlarmTracksBinding> {

    public String TAG = AlarmTracksVM.class.getSimpleName();

    /***
     * The observable list of tracks selected for this alarm
     */
    public ObservableArrayList<ItemTrackViewModel> tracks;
    public ItemView itemTracksBinder = ItemView.of(BR.itemTrackViewModel, R.layout.item_track);

    Alarm alarm;

    /***
     * @param fragment
     * @param binding
     */
    public AlarmTracksVM(AlarmTracksFragment fragment, FragmentAlarmTracksBinding binding) {
        super(fragment, binding);
    }

    @Override
    public void init() {
        tracks = new ObservableArrayList<>();
        alarm = Parcels.unwrap(Dart.get(fragment.getArguments(), EXTRA.ALARM));
        Injector.INSTANCE.playerComponent().init(this);
        initModel();
    }

    @Override
    public void destroy() {

    }

    /***
     * Init the viewmodel with its alarm
     */
    public void initModel() {

        for (AlarmTrack alarmTrack : alarm.getTracks()) {
            ItemTrackViewModel itemTrackViewModel = new ItemTrackViewModel(fragment, binding);
            itemTrackViewModel.setAlarmTrack(alarmTrack);
            tracks.add(itemTrackViewModel);
        }
    }


    public void onPlayClick(View view) {
        Injector.INSTANCE.playerComponent().manager().startAlarm(alarm);

    }


    /**
     * @param alarmTrack
     */
    public void add(AlarmTrack alarmTrack) {
        ItemTrackViewModel itemTrackViewModel = new ItemTrackViewModel(fragment, binding);
        itemTrackViewModel.setAlarmTrack(alarmTrack);
        tracks.add(itemTrackViewModel);

        alarm.getTracks().add(alarmTrack);
    }

    public List<AlarmTrack> getTracks() {
        return alarm.getTracks();
    }
}
