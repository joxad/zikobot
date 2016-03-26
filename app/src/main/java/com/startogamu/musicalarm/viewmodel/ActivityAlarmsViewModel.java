package com.startogamu.musicalarm.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.view.activity.Henson;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import io.realm.Realm;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by josh on 09/03/16.
 */
public class ActivityAlarmsViewModel extends BaseObservable implements ViewModel {

    private static final String TAG = ActivityAlarmsViewModel.class.getSimpleName();
    private final Context context;
    Realm realm;
    Subscription subscription;
    private ObservableArrayList<ItemAlarmViewModel> observableArrayList;

    public ActivityAlarmsViewModel(Context context) {
        this.context = context;
        observableArrayList = new ObservableArrayList<>();
        realm = Realm.getDefaultInstance();
        loadAlarms();
    }

    /***
     * Load the alarms using realm
     */
    public void loadAlarms() {
        subscription = realm.where(Alarm.class).findAllAsync().asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(alarms -> {
                    for (Alarm alarm : alarms) {
                        ItemAlarmViewModel itemAlarmViewModel = new ItemAlarmViewModel(context, alarm);
                        observableArrayList.add(itemAlarmViewModel);
                    }

                });
    }

    @Override
    public void onDestroy() {
        realm.close();
    }

    @Bindable
    public ObservableArrayList<ItemAlarmViewModel> getItemViewModels() {
        return observableArrayList;
    }

    public void addAlarm(View view) {
        context.startActivity(Henson.with(context)
                .gotoAlarmActivity().alarm(null).build());
    }

    public ItemBinder<ItemAlarmViewModel> itemViewBinder() {
        return new ItemBinderBase<>(BR.itemAlarmViewModel, R.layout.item_alarm);
    }

}
