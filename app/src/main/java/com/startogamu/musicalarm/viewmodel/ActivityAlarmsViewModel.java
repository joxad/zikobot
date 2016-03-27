package com.startogamu.musicalarm.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.pixplicity.easyprefs.library.Prefs;
import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.di.manager.spotify_auth.SpotifyAuthManager;
import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.model.spotify.SpotifyRequestToken;
import com.startogamu.musicalarm.model.spotify.SpotifyToken;
import com.startogamu.musicalarm.utils.SpotifyPrefs;
import com.startogamu.musicalarm.view.activity.Henson;
import com.startogamu.musicalarm.view.activity.MusicActivity;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Subscriber;
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
    @Inject
    SpotifyAuthManager spotifyAuthManager;

    private ObservableArrayList<ItemAlarmViewModel> observableArrayList;

    public ActivityAlarmsViewModel(Context context) {
        this.context = context;
        MusicAlarmApplication.get(context).netComponent.inject(this);
        observableArrayList = new ObservableArrayList<>();
        realm = Realm.getDefaultInstance();
        loadAlarms();

        if (Prefs.contains(SpotifyPrefs.SPOTIFY_CODE)) {
            try {
                refreshAccessToken(Prefs.getString(SpotifyPrefs.SPOTIFY_CODE, ""));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshAccessToken(final String accessCode) throws UnsupportedEncodingException {
        spotifyAuthManager.requestToken(
                new SpotifyRequestToken("authorization_code", accessCode,
                        context.getString(R.string.api_spotify_callback_musics),
                        context.getString(R.string.api_spotify_id),
                        context.getString(R.string.api_spotify_secret)),
                new Subscriber<SpotifyToken>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(SpotifyToken spotifyToken) {
                        Log.d(TAG, spotifyToken.toString());
                        Prefs.putString(SpotifyPrefs.SPOTIFY_TOKEN, spotifyToken.getAccessToken());
                    }
                });
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
