package com.startogamu.musicalarm.viewmodel;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivityAlarmBinding;
import com.startogamu.musicalarm.module.alarm.manager.AlarmManager;
import com.startogamu.musicalarm.module.alarm.object.Alarm;
import com.startogamu.musicalarm.module.alarm.object.AlarmTrack;
import com.startogamu.musicalarm.core.receiver.AlarmReceiver;
import com.startogamu.musicalarm.core.utils.EXTRA;
import com.startogamu.musicalarm.core.utils.REQUEST;
import com.startogamu.musicalarm.view.Henson;
import com.startogamu.musicalarm.view.activity.AlarmActivity;
import com.startogamu.musicalarm.view.adapter.ViewPagerAdapter;
import com.startogamu.musicalarm.view.fragment.AlarmInfoFragment;
import com.startogamu.musicalarm.view.fragment.AlarmTracksFragment;

import org.parceler.Parcels;

import java.util.Calendar;

import rx.Subscriber;

/**
 * Created by josh on 08/03/16.
 */
public class ActivityAlarmViewModel extends BaseObservable implements ViewModel {
    private static String TAG = ActivityAlarmViewModel.class.getSimpleName();


    private AlarmActivity context;
    private ActivityAlarmBinding binding;
    AlarmTracksFragment alarmTracksFragment;
    AlarmInfoFragment alarmInfoFragment;

    private android.app.AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    ViewPagerAdapter viewPagerAdapter;

    @InjectExtra
    Alarm alarm;
    /***
     * Copy the alarm from an existing one
     *
     * @param context
     * @param binding
     */
    public ActivityAlarmViewModel(final AlarmActivity context, final ActivityAlarmBinding binding) {
        this.context = context;
        this.binding = binding;
        Dart.inject(this, context);
        initViews();
    }

    private void initViews() {
        alarmInfoFragment = AlarmInfoFragment.newInstance(alarm);
        alarmTracksFragment = AlarmTracksFragment.newInstance(alarm);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    //TODO delete
                    context.finish();
                    break;
                case R.id.action_save:
                    //TODO save;
                    onValidateClick();
                    break;
            }
            return false;
        });
        viewPagerAdapter = new ViewPagerAdapter(context.getFragmentManager(),
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
    public void onValidateClick() {
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
                context.finish();
            }
        });

    }

    /***
     * Use this method to schedule the alarm
     *
     * @param alarm
     */
    private void prepareAlarm(Alarm alarm) {

        alarmMgr = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA.ALARM_ID, alarm.getId());
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmMgr.cancel(alarmIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, this.alarm.getHour());
        calendar.set(Calendar.MINUTE, this.alarm.getMinute());
        alarmMgr.setRepeating(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 15 * 60, alarmIntent);// test every 60 scs
    }


    /***
     * @param view
     */
    public void onAddTrackClick(View view) {
        context.startActivityForResult(Henson.with(context).gotoMusicActivity().build(), REQUEST.CODE_TRACK);
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
    public void onDestroy() {

    }

}
