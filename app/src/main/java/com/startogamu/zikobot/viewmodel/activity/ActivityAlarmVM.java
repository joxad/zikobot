package com.startogamu.zikobot.viewmodel.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.receiver.AlarmReceiver;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.ActivityAlarmBinding;
import com.startogamu.zikobot.module.alarm.model.Alarm;
import com.startogamu.zikobot.view.Henson;
import com.startogamu.zikobot.view.activity.ActivityAlarm;
import com.startogamu.zikobot.viewmodel.base.AlarmVM;

import java.util.Calendar;

import rx.functions.Action1;

/**
 * Created by josh on 29/05/16.
 */
public class ActivityAlarmVM extends ActivityBaseVM<ActivityAlarm, ActivityAlarmBinding> {
    private static String TAG = ActivityAlarmVM.class.getSimpleName();

    private android.app.AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public AlarmVM alarmVM;
    @InjectExtra
    Alarm alarm;

    /***
     * @param activity
     * @param binding
     */
    public ActivityAlarmVM(ActivityAlarm activity, ActivityAlarmBinding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {
        Dart.inject(this, activity);
        alarmMgr = (android.app.AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        activity.setSupportActionBar(binding.toolbar);
        activity.setTitle(alarm.getName());
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(listener -> activity.onBackPressed());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    delete();
                    break;
                case R.id.action_save:
                    if (alarmVM.hasTracks()) {
                        save().subscribe(alarm1 -> {
                            prepareAlarm(alarm1);
                            activity.finish();
                        }, throwable -> {
                            Snackbar.make(binding.getRoot(), throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                        });
                    } else {
                        Snackbar.make(binding.getRoot(), R.string.add_tracks, Snackbar.LENGTH_SHORT).show();

                    }

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


        initViews();
    }


    @Override
    protected void onResume() {
        super.onResume();
        alarmVM.refreshTracks();
    }

    /***
     *
     */
    private void initViews() {
        alarmVM = new AlarmVM(activity, alarm);

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
        binding.viewAlarm.swRepeat.setChecked(alarm.getRepeated()==1);
        initHour();
        initSelectedDays();
        RxCompoundButton.checkedChanges(binding.viewAlarm.swRepeat).subscribe(aBoolean -> {
           alarmVM.updateRepeated(aBoolean);
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
        Intent intent = new Intent(activity, AlarmReceiver.class);
        intent.putExtra(EXTRA.ALARM_ID, alarm.getId());
        alarmIntent = PendingIntent.getBroadcast(activity, 0, intent, 0);
        alarmMgr.cancel(alarmIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (calendar.get(Calendar.HOUR_OF_DAY) > alarm.getHour()){
            calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR)+1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, this.alarm.getHour());
        calendar.set(Calendar.MINUTE, this.alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (binding.viewAlarm.swRepeat.isChecked()) {
            alarmMgr.setRepeating(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        } else {
            alarmMgr.set(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

        }
    }


    /***
     * @param view
     */
    public void onAddTrackClick(View view) {
        save().subscribe(alarm1 -> {
            activity.startActivity(Henson.with(activity).gotoActivityMusic().alarm(alarm1).build());

        });
    }


    @Override
    public void destroy() {

    }


}
