package com.startogamu.musicalarm.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.pixplicity.easyprefs.library.Prefs;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.utils.AppPrefs;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.alarm.manager.AlarmManager;
import com.startogamu.musicalarm.module.component.Injector;
import com.startogamu.musicalarm.view.Henson;
import com.startogamu.musicalarm.viewmodel.items.ItemAlarmViewModel;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import java.io.UnsupportedEncodingException;
import java.util.List;

import rx.Subscriber;

/**
 * Created by josh on 09/03/16.
 */
public class ActivityAlarmsViewModel extends BaseObservable implements ViewModel {

    private static final String TAG = ActivityAlarmsViewModel.class.getSimpleName();
    private final Context context;

    private ObservableArrayList<ItemAlarmViewModel> itemsVM;

    public ActivityAlarmsViewModel(Context context) {
        this.context = context;
        Injector.INSTANCE.spotifyAuth().inject(this);
        itemsVM = new ObservableArrayList<>();
        if (Prefs.contains(AppPrefs.ACCESS_CODE)) {
            try {
                refreshAccessToken();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadAlarms() {
        itemsVM.clear();
        AlarmManager.loadAlarms().subscribe(new Subscriber<List<Alarm>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Alarm> alarms) {
                itemsVM.clear();
                for (Alarm alarm : alarms) {
                    itemsVM.add(new ItemAlarmViewModel(context, alarm));
                }

            }
        });
    }

    private void refreshAccessToken() throws UnsupportedEncodingException {
        Injector.INSTANCE.spotifyAuth().manager().refreshToken(context, () -> {

        });
    }


    @Bindable
    public ObservableArrayList<ItemAlarmViewModel> getItemViewModels() {
        return itemsVM;
    }

    public void addAlarm(View view) {
        context.startActivity(Henson.with(context)
                .gotoAlarmActivity().alarm(new Alarm()).build());
    }

    public ItemBinder<ItemAlarmViewModel> itemViewBinder() {
        return new ItemBinderBase<>(BR.itemAlarmViewModel, R.layout.item_alarm);
    }

    @Override
    public void onDestroy() {

    }
}
