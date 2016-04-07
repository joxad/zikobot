package com.startogamu.musicalarm.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.pixplicity.easyprefs.library.Prefs;
import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.di.manager.AlarmManager;
import com.startogamu.musicalarm.di.manager.spotify_auth.SpotifyAuthManager;
import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.core.utils.SpotifyPrefs;
import com.startogamu.musicalarm.view.Henson;
import com.startogamu.musicalarm.viewmodel.items.ItemAlarmViewModel;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by josh on 09/03/16.
 */
public class ActivityAlarmsViewModel extends BaseObservable implements ViewModel {

    private static final String TAG = ActivityAlarmsViewModel.class.getSimpleName();
    private final Context context;

    Subscription subscription;
    @Inject
    SpotifyAuthManager spotifyAuthManager;
    private ObservableArrayList<ItemAlarmViewModel> observableArrayList;

    public ActivityAlarmsViewModel(Context context) {
        this.context = context;
        MusicAlarmApplication.get(context).netComponent.inject(this);
        observableArrayList = new ObservableArrayList<>();
        if (Prefs.contains(SpotifyPrefs.ACCESS_CODE)) {
            try {
                refreshAccessToken();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadAlarms() {
        observableArrayList.clear();
        AlarmManager.loadAlarms().subscribe(new Subscriber<List<Alarm>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Alarm> alarms) {
                observableArrayList.clear();
                for (Alarm alarm : alarms) {
                    observableArrayList.add(new ItemAlarmViewModel(context, alarm));
                }

            }
        });
    }

    private void refreshAccessToken() throws UnsupportedEncodingException {
        spotifyAuthManager.refreshToken(context, () -> {

        });
    }


    @Bindable
    public ObservableArrayList<ItemAlarmViewModel> getItemViewModels() {
        return observableArrayList;
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
