package com.startogamu.musicalarm.viewmodel.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.easydatabinding.activity.IResult;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.receiver.AlarmReceiver;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.core.utils.REQUEST;
import com.startogamu.musicalarm.databinding.ActivityAlarmBinding;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.alarm.object.AlarmTrack;
import com.startogamu.musicalarm.view.Henson;
import com.startogamu.musicalarm.view.activity.ActivityAlarm;
import com.startogamu.musicalarm.viewmodel.base.AlarmVM;

import org.parceler.Parcels;

import java.util.Calendar;

/**
 * Created by josh on 29/05/16.
 */
public class ActivityAlarmVM extends ActivityBaseVM<ActivityAlarm, ActivityAlarmBinding> implements IResult {
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
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.setTitle(alarm.getName());
        binding.toolbar.setNavigationOnClickListener(listener -> activity.finish());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    delete();
                    break;
                case R.id.action_save:
                    save();
                    break;
                case R.id.action_play:
                    activity.startActivity(Henson.with(activity).gotoActivityWakeUp().alarm(alarm).build());
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

        initHour();
        initSelectedDays();
        RxTextView.textChanges(binding.viewAlarm.etName).skip(1).subscribe(charSequence -> {
            alarmVM.updateName(charSequence);
        });
        RxCompoundButton.checkedChanges(binding.viewAlarm.cbMonday).subscribe(aBoolean -> {
            alarmVM.activeDay(Calendar.MONDAY, aBoolean);
        });
        RxCompoundButton.checkedChanges(binding.viewAlarm.cbTuesday).subscribe(aBoolean -> {
            alarmVM.activeDay(Calendar.TUESDAY, aBoolean);
        });
        RxCompoundButton.checkedChanges(binding.viewAlarm.cbWednesday).subscribe(aBoolean -> {
            alarmVM.activeDay(Calendar.WEDNESDAY, aBoolean);
        });
        RxCompoundButton.checkedChanges(binding.viewAlarm.cbThursday).subscribe(aBoolean -> {
            alarmVM.activeDay(Calendar.THURSDAY, aBoolean);
        });
        RxCompoundButton.checkedChanges(binding.viewAlarm.cbFriday).subscribe(aBoolean -> {
            alarmVM.activeDay(Calendar.FRIDAY, aBoolean);
        });

        RxCompoundButton.checkedChanges(binding.viewAlarm.cbSaturday).subscribe(aBoolean -> {
            alarmVM.activeDay(Calendar.SATURDAY, aBoolean);
        });
        RxCompoundButton.checkedChanges(binding.viewAlarm.cbSunday).subscribe(aBoolean -> {
            alarmVM.activeDay(Calendar.SUNDAY, aBoolean);
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
        binding.viewAlarm.cbMonday.setChecked(alarmVM.isDayActive(Calendar.MONDAY));
        binding.viewAlarm.cbTuesday.setChecked(alarmVM.isDayActive(Calendar.TUESDAY));
        binding.viewAlarm.cbWednesday.setChecked(alarmVM.isDayActive(Calendar.WEDNESDAY));
        binding.viewAlarm.cbThursday.setChecked(alarmVM.isDayActive(Calendar.THURSDAY));
        binding.viewAlarm.cbFriday.setChecked(alarmVM.isDayActive(Calendar.FRIDAY));
        binding.viewAlarm.cbSaturday.setChecked(alarmVM.isDayActive(Calendar.SATURDAY));
        binding.viewAlarm.cbSunday.setChecked(alarmVM.isDayActive(Calendar.SUNDAY));
    }


    /***
     * Use this method to save the data
     */
    public void save() {

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

        alarmVM.save().subscribe(alarm1 -> {
            prepareAlarm(alarm1);
        }, throwable -> {
            Snackbar.make(binding.getRoot(), throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
        });
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
        calendar.set(Calendar.HOUR_OF_DAY, this.alarm.getHour());
        calendar.set(Calendar.MINUTE, this.alarm.getMinute());
        calendar.set(Calendar.MILLISECOND, 0);
        alarmMgr.setRepeating(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);// test every 60 scs

        activity.finish();
    }


    /***
     * @param view
     */
    public void onAddTrackClick(View view) {
        activity.startActivityForResult(Henson.with(activity).gotoActivityMusic().build(), REQUEST.CODE_TRACK);
    }


    /***
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO manage the type of data in the intent => list of alarm track/playlist/etc..
        if (requestCode == REQUEST.CODE_TRACK) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                AlarmTrack alarmTrack = Parcels.unwrap(data.getParcelableExtra(EXTRA.TRACK));
                alarmVM.addTrack(alarmTrack);
            }
        }
    }


    @Override
    public void destroy() {

    }


}
