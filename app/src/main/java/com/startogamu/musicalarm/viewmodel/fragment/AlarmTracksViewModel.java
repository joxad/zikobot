package com.startogamu.musicalarm.viewmodel.fragment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.android.databinding.library.baseAdapters.BR;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.FragmentAlarmTracksBinding;
import com.startogamu.musicalarm.di.manager.AlarmManager;
import com.startogamu.musicalarm.view.fragment.AlarmTracksFragment;
import com.startogamu.musicalarm.viewmodel.ViewModel;
import com.startogamu.musicalarm.viewmodel.items.ItemTrackViewModel;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

/**
 * Created by josh on 31/03/16.
 */

/***
 * {@link AlarmTracksViewModel}  make the link between {@link AlarmTracksFragment} and {@link AlarmManager}
 */
public class AlarmTracksViewModel extends BaseObservable implements ViewModel {

    private final FragmentAlarmTracksBinding binding;
    public String TAG = AlarmTracksViewModel.class.getSimpleName();
    AlarmTracksFragment context;

    private ObservableArrayList<ItemTrackViewModel> tracks;

    /***
     * @param context
     * @param binding
     */
    public AlarmTracksViewModel(AlarmTracksFragment context, FragmentAlarmTracksBinding binding) {
        this.context = context;
        this.binding = binding;

    }

    public void onDestroy() {

    }

    @Bindable
    public ObservableArrayList<ItemTrackViewModel> getAlarmTracks() {
        return tracks;
    }

    public ItemBinder<ItemTrackViewModel> itemTracksBinder() {
        return new ItemBinderBase<>(BR.itemTrackViewModel, R.layout.item_track);
    }
}
