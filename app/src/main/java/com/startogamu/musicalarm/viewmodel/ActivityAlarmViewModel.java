package com.startogamu.musicalarm.viewmodel;

import android.content.Context;
import android.content.res.ColorStateList;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.text.TextWatcher;
import android.view.View;

import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.databinding.ActivityAlarmBinding;
import com.startogamu.musicalarm.di.manager.spotify_api.SpotifyAPIManager;
import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.utils.SimpleTextWatcher;
import com.startogamu.musicalarm.view.activity.Henson;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by josh on 08/03/16.
 */
public class ActivityAlarmViewModel extends BaseObservable implements ViewModel {

    @Inject
    SpotifyAPIManager spotifyAPIManager;

    private String alarmName = "";

    private static String TAG = ActivityAlarmViewModel.class.getSimpleName();
    Realm realm;
    private Alarm alarm;
    private Context context;

    /***
     * @param context
     * @param binding
     */
    public ActivityAlarmViewModel(final Context context, final ActivityAlarmBinding binding) {
        init(context, binding);
    }

    /***
     * Copy the alarm from an existing one
     *
     * @param context
     * @param binding
     * @param alarm
     */
    public ActivityAlarmViewModel(final Context context, final ActivityAlarmBinding binding, final Alarm alarm) {
        init(context, binding);
    }

    /***
     * Init of the context and the DB
     * Create the observables methods of the views
     *
     * @param context
     * @param binding
     */
    public void init(final Context context, final ActivityAlarmBinding binding) {
        this.context = context;
        realm = Realm.getDefaultInstance();
        MusicAlarmApplication.get(context).netComponent.inject(this);

    }

    /***
     * @param view
     */
    public void onAlarmValidated(View view) {
        createOrUpdate();
    }

    @Override
    public void onDestroy() {
        realm.close();
    }

    /***
     *
     */
    public void createOrUpdate() {
        realm.beginTransaction();
        Alarm alarm = new Alarm(alarmName);
        realm.copyToRealm(alarm);
        realm.commitTransaction();
    }


    public void onMusicClick(View view) {
        context.startActivity(Henson.with(context).gotoMusicActivity().build());
    }
    @Bindable
    public Alarm getAlarm() {
        return alarm;
    }

    public TextWatcher nameWatcher = new SimpleTextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            alarmName = s.toString();
        }

    };

}
