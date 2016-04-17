package com.startogamu.musicalarm.viewmodel;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.joxad.easydatabinding.activity.IResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.core.receiver.AlarmReceiver;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.core.utils.REQUEST;
import com.startogamu.musicalarm.databinding.ActivityAlarmBinding;
import com.startogamu.musicalarm.module.alarm.manager.AlarmManager;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.alarm.object.AlarmTrack;
import com.startogamu.musicalarm.module.alarm.object.Alarm_Table;
import com.startogamu.musicalarm.view.Henson;
import com.startogamu.musicalarm.view.activity.ActivityAlarm;
import com.startogamu.musicalarm.view.adapter.ViewPagerAdapter;
import com.startogamu.musicalarm.view.fragment.AlarmInfoFragment;
import com.startogamu.musicalarm.view.fragment.AlarmTracksFragment;

import org.parceler.Parcels;

import java.util.Calendar;

import rx.Subscriber;

/**
 * Created by josh on 08/03/16.
 */
public class ActivityAlarmVM extends ActivityBaseVM<ActivityAlarm, ActivityAlarmBinding> implements IResult {
    private static String TAG = ActivityAlarmVM.class.getSimpleName();

    AlarmTracksFragment alarmTracksFragment;
    AlarmInfoFragment alarmInfoFragment;

    private android.app.AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    ViewPagerAdapter viewPagerAdapter;

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


    /***
     *
     */
    private void initViews() {
        alarmInfoFragment = AlarmInfoFragment.newInstance(alarm);
        alarmTracksFragment = AlarmTracksFragment.newInstance(alarm);

        viewPagerAdapter = new ViewPagerAdapter(activity.getSupportFragmentManager(),
                new Fragment[]{
                        alarmInfoFragment,
                        alarmTracksFragment
                },
                new CharSequence[]{
                        "Mon alarme", "Mes chansons"
                });

        binding.slidingTabLayout.setDistributeEvenly(true);
        binding.slidingTabLayout.setSelectedIndicatorColors(android.R.color.white);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.slidingTabLayout.setViewPager(binding.viewPager);
    }


    /***
     * Use this method to save the data
     */
    public void save() {
        AlarmManager.saveAlarm(alarmInfoFragment.getAlarm(), alarmTracksFragment.getTracks()).subscribe(new Subscriber<Alarm>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Alarm alarm) {
                prepareAlarm(alarm);
                activity.finish();
            }
        });

    }

    /***
     * Delete the alarm
     */
    private void delete() {
        SQLite.delete(Alarm.class).where(Alarm_Table.id.is(alarm.getId())).query();
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
                alarmTracksFragment.add(alarmTrack);
            }
        }
    }

    @Override
    public void destroy() {

    }


}
