package com.startogamu.zikobot.viewmodel.activity;

import android.content.Context;
import android.media.AudioManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.android.databinding.library.baseAdapters.BR;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.analytics.AnalyticsManager;
import com.startogamu.zikobot.core.utils.SimpleSeekBarListener;
import com.startogamu.zikobot.databinding.ActivityAlarmBinding;
import com.startogamu.zikobot.module.zikobot.manager.AlarmTrackManager;
import com.startogamu.zikobot.module.zikobot.model.Alarm;
import com.startogamu.zikobot.module.zikobot.model.Track;
import com.startogamu.zikobot.view.Henson;
import com.startogamu.zikobot.view.activity.ActivityAlarm;
import com.startogamu.zikobot.viewmodel.base.AlarmVM;

import java.util.Calendar;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 29/05/16.
 */
public class ActivityAlarmVM extends ActivityBaseVM<ActivityAlarm, ActivityAlarmBinding> {
    private static String TAG = ActivityAlarmVM.class.getSimpleName();


    public AlarmVM alarmVM;
    @InjectExtra
    Alarm alarm;
    private AudioManager am;

    /***
     * @param activity
     * @param binding
     */
    public ActivityAlarmVM(ActivityAlarm activity, ActivityAlarmBinding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {
        am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        Dart.inject(this, activity);
        activity.setSupportActionBar(binding.toolbar);
        activity.setTitle(alarm.getName());
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(listener -> activity.onBackPressed());
        initMenu();
        initViews();
        AlarmTrackManager.clear();

    }

    private void initMenu() {
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    delete();
                    break;
                case R.id.action_save:

                    save().subscribe(alarm1 -> {
                        prepareAlarm(alarm1);
                        AlarmTrackManager.clear();
                        activity.finish();
                    }, throwable -> {
                        Snackbar.make(binding.getRoot(), throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                    });


                    break;
                case R.id.action_play:
                    if (alarmVM.hasTracks()) {
                        activity.startActivity(Henson.with(activity).gotoActivityWakeUp().alarm(alarm).build());
                    } else {
                        Snackbar.make(binding.getRoot(), R.string.add_tracks, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
            }
            return false;
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        alarmVM.refreshTracks();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /***
     *
     */
    private void initViews() {
        alarmVM = new AlarmVM(activity, alarm) {
            @Override
            public ItemView itemView() {
                return ItemView.of(BR.trackVM, R.layout.item_alarm_track_no_cb);
            }
        };
        initAlarmVM();
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                alarmVM.removeTrack(viewHolder.getAdapterPosition());
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(binding.viewAlarm.rvTracks);


    }

    private void initAlarmVM() {
        alarmVM.initModel();
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        binding.viewAlarm.seekBarVolume.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        binding.viewAlarm.seekBarVolume.setProgress(alarm.getVolume() == -1 ? ((int) (max * 0.5f)) : alarm.getVolume());
        binding.viewAlarm.swRepeat.setChecked(alarm.getRepeated() == 1);
        binding.viewAlarm.swRandom.setChecked(alarm.getRandomTrack() == 1);
        initHour();
        initSelectedDays();
        RxCompoundButton.checkedChanges(binding.viewAlarm.swRepeat).subscribe(aBoolean -> {
            alarmVM.updateRepeated(aBoolean);
        });
        RxCompoundButton.checkedChanges(binding.viewAlarm.swRandom).subscribe(aBoolean -> {
            alarmVM.updateRandom(aBoolean);
        });
        RxTextView.textChanges(binding.viewAlarm.etName).skip(1).subscribe(charSequence -> {
            alarmVM.updateName(charSequence);
        });

        RxView.clicks(binding.viewAlarm.cbMonday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbMonday, Calendar.MONDAY);
        });

        RxView.clicks(binding.viewAlarm.cbTuesday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbTuesday, Calendar.TUESDAY);
        });
        RxView.clicks(binding.viewAlarm.cbWednesday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbWednesday, Calendar.WEDNESDAY);
        });
        RxView.clicks(binding.viewAlarm.cbThursday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbThursday, Calendar.THURSDAY);
        });
        RxView.clicks(binding.viewAlarm.cbFriday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbFriday, Calendar.FRIDAY);
        });
        RxView.clicks(binding.viewAlarm.cbSaturday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbSaturday, Calendar.SATURDAY);
        });
        RxView.clicks(binding.viewAlarm.cbSunday).subscribe(aVoid -> {
            alarmVM.handleTextClickDay(binding.viewAlarm.cbSunday, Calendar.SUNDAY);
        });
        binding.viewAlarm.seekBarVolume.setOnSeekBarChangeListener(new SimpleSeekBarListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    alarmVM.updateVolume(progress);
            }
        });
    }


    private void initHour() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            binding.viewAlarm.timePicker.setMinute(alarmVM.getMinute());
            binding.viewAlarm.timePicker.setHour(alarmVM.getHour());

        } else {
            binding.viewAlarm.timePicker.setCurrentHour(Integer.valueOf(alarmVM.getHour()));
            binding.viewAlarm.timePicker.setCurrentMinute(Integer.valueOf(alarmVM.getMinute()));

        }
    }

    /***
     * Check the activated days
     */
    private void initSelectedDays() {
        binding.viewAlarm.cbMonday.setSelected(alarmVM.isDayActive(Calendar.MONDAY));
        binding.viewAlarm.cbTuesday.setSelected(alarmVM.isDayActive(Calendar.TUESDAY));
        binding.viewAlarm.cbWednesday.setSelected(alarmVM.isDayActive(Calendar.WEDNESDAY));
        binding.viewAlarm.cbThursday.setSelected(alarmVM.isDayActive(Calendar.THURSDAY));
        binding.viewAlarm.cbFriday.setSelected(alarmVM.isDayActive(Calendar.FRIDAY));
        binding.viewAlarm.cbSaturday.setSelected(alarmVM.isDayActive(Calendar.SATURDAY));
        binding.viewAlarm.cbSunday.setSelected(alarmVM.isDayActive(Calendar.SUNDAY));
    }


    /***
     * Use this method to save the data
     */
    public rx.Observable<Alarm> save() {

        int min = 0;
        int hour = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = binding.viewAlarm.timePicker.getHour();
            min = binding.viewAlarm.timePicker.getMinute();

        } else {
            hour = binding.viewAlarm.timePicker.getCurrentHour();
            min = binding.viewAlarm.timePicker.getCurrentMinute();

        }
        Log.d(AlarmVM.class.getSimpleName(), "hours " + hour + "minu" + min);
        alarmVM.updateTimeSelected(hour, min);
        alarmVM.updateRepeated(binding.viewAlarm.swRepeat.isChecked());
        AnalyticsManager.logCreateAlarm(alarm, true);
        alarmVM.updateStatus(alarmVM.hasTracks());
        return alarmVM.save();
    }

    /***
     * Delete the alarm
     */
    private void delete() {
        alarmVM.delete();
        activity.finish();
    }

    /***
     * Use this method to schedule the alarm
     *
     * @param alarm
     */
    private void prepareAlarm(Alarm alarm) {
        com.startogamu.zikobot.module.zikobot.manager.AlarmManager.prepareAlarm(activity, alarm);
    }


    /***
     * @param view
     */
    public void onAddTrackClick(View view) {
        activity.startActivity(Henson.with(activity).gotoActivityMusic().alarm(alarm).build());
    }


    @Override
    public void destroy() {

    }


}
