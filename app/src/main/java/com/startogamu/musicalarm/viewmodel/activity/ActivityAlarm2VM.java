package com.startogamu.musicalarm.viewmodel.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.easydatabinding.activity.IResult;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.receiver.AlarmReceiver;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.core.utils.REQUEST;
import com.startogamu.musicalarm.databinding.ActivityAlarm2Binding;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.alarm.object.AlarmTrack;
import com.startogamu.musicalarm.view.Henson;
import com.startogamu.musicalarm.view.activity.ActivityAlarm2;
import com.startogamu.musicalarm.viewmodel.base.AlarmVM;

import org.parceler.Parcels;

import java.util.Calendar;

/**
 * Created by josh on 29/05/16.
 */
public class ActivityAlarm2VM extends ActivityBaseVM<ActivityAlarm2, ActivityAlarm2Binding> implements IResult {
    private static String TAG = ActivityAlarm2VM.class.getSimpleName();

    private android.app.AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public AlarmVM alarmVM;
    @InjectExtra
    Alarm alarm;

    /***
     * @param activity
     * @param binding
     */
    public ActivityAlarm2VM(ActivityAlarm2 activity, ActivityAlarm2Binding binding) {
        super(activity, binding);
    }

    @Override
    public void init() {
        Dart.inject(this, activity);
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(listener -> activity.finish());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    delete();
                    break;
                case R.id.action_save:
                    save();
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
        alarmVM.initBinding(binding.viewAlarm);
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


    /***
     * Use this method to save the data
     */
    public void save() {
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
        alarmMgr = (android.app.AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, AlarmReceiver.class);
        intent.putExtra(EXTRA.ALARM_ID, alarm.getId());
        alarmIntent = PendingIntent.getBroadcast(activity, 0, intent, 0);
        alarmMgr.cancel(alarmIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, this.alarm.getHour());
        calendar.set(Calendar.MINUTE, this.alarm.getMinute());
        alarmMgr.setRepeating(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 60 * 24, alarmIntent);// test every 60 scs

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
