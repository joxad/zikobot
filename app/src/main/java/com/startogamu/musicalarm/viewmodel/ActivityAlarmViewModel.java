package com.startogamu.musicalarm.viewmodel;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.databinding.ActivityAlarmBinding;
import com.startogamu.musicalarm.di.manager.AlarmManager;
import com.startogamu.musicalarm.di.manager.spotify_api.SpotifyAPIManager;
import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.utils.EXTRA;
import com.startogamu.musicalarm.view.activity.Henson;

import javax.inject.Inject;

/**
 * Created by josh on 08/03/16.
 */
public class ActivityAlarmViewModel extends BaseObservable implements ViewModel {
    private static String TAG = ActivityAlarmViewModel.class.getSimpleName();
    private static final int REQUEST_CODE = 1337;
    @Inject
    SpotifyAPIManager spotifyAPIManager;
    @Inject
    AlarmManager alarmManager;
    private AppCompatActivity context;

    private Alarm alarm;
    private String alarmName = "";
    private String alarmSelectedTime = "";

    /***
     * @param context
     * @param binding
     */
    public ActivityAlarmViewModel(final AppCompatActivity context, final ActivityAlarmBinding binding) {
        init(context, binding);
        alarm = new Alarm();
        alarm.setId(-1);
        alarm.setHour(8);
        alarm.setMinute(0);
        updateSelectedTime(alarm);
    }

    /***
     * Copy the alarm from an existing one
     *
     * @param context
     * @param binding
     * @param alarm
     */
    public ActivityAlarmViewModel(final AppCompatActivity context, final ActivityAlarmBinding binding, final Alarm alarm) {
        init(context, binding);
        this.alarm = alarm;
        alarmName = alarm.getName();
        updateSelectedTime(alarm);
    }

    /***
     * Init of the context and the DB
     * Create the observables methods of the views
     *
     * @param context
     * @param binding
     */
    public void init(final AppCompatActivity context, final ActivityAlarmBinding binding) {
        this.context = context;
        MusicAlarmApplication.get(context).netComponent.inject(this);

        RxTextView.textChanges(binding.etName).skip(1).subscribe(charSequence -> {
            alarmName = charSequence.toString();
        });

    }

    /***
     * @param view
     */
    public void onValidateClick(View view) {
        alarm.setName(alarmName);
        alarmManager.saveAlarm(alarm);
        context.finish();
    }


    public void onMusicClick(View view) {
        context.startActivityForResult(Henson.with(context).gotoMusicActivity().build(), REQUEST_CODE);
    }


    public void onTimeClick(View view) {
        new TimePickerDialog(context, (view1, hourOfDay, minute) -> {
            alarm.setHour(hourOfDay);
            alarm.setMinute(minute);
            updateSelectedTime(alarm);
        }, alarm.getHour(), alarm.getMinute(), true).show();
    }

    /***
     * @param alarm
     */
    private void updateSelectedTime(Alarm alarm) {
        alarmSelectedTime = String.format("%d H %02d", alarm.getHour(), alarm.getMinute());
        notifyPropertyChanged(BR.selectedTime);
    }

    @Bindable
    public String getSelectedTime() {
        return alarmSelectedTime;
    }

    @Bindable
    public String getAlarmName() {
        return alarmName;
    }

    @Override
    public void onDestroy() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                alarm.setPlaylistId(data.getStringExtra(EXTRA.PLAYLIST_ID));
            }
        }
    }
}
