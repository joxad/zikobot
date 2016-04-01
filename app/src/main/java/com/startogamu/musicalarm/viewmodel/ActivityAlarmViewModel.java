package com.startogamu.musicalarm.viewmodel;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.startogamu.musicalarm.MusicAlarmApplication;
import com.startogamu.musicalarm.R;
import com.startogamu.musicalarm.databinding.ActivityAlarmBinding;
import com.startogamu.musicalarm.di.manager.AlarmManager;
import com.startogamu.musicalarm.di.manager.spotify_api.SpotifyAPIManager;
import com.startogamu.musicalarm.model.Alarm;
import com.startogamu.musicalarm.model.AlarmTrack;
import com.startogamu.musicalarm.model.spotify.SpotifyPlaylistItem;
import com.startogamu.musicalarm.model.spotify.SpotifyPlaylistWithTrack;
import com.startogamu.musicalarm.receiver.AlarmReceiver;
import com.startogamu.musicalarm.utils.EXTRA;
import com.startogamu.musicalarm.view.activity.Henson;
import com.startogamu.musicalarm.view.adapter.ViewPagerAdapter;
import com.startogamu.musicalarm.view.fragment.AlarmInfoFragment;
import com.startogamu.musicalarm.view.fragment.AlarmTracksFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by josh on 08/03/16.
 */
public class ActivityAlarmViewModel extends BaseObservable implements ViewModel {
    private static String TAG = ActivityAlarmViewModel.class.getSimpleName();
    private static final int REQUEST_CODE = 1337;
    @Inject
    SpotifyAPIManager spotifyAPIManager;

    private AppCompatActivity context;
    private ActivityAlarmBinding binding;
    private Alarm alarm;
    private String alarmName = "";

    List<AlarmTrack> alarmTrackList;

    private android.app.AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    ViewPagerAdapter viewPagerAdapter;

    /***
     * Copy the alarm from an existing one
     *
     * @param context
     * @param binding
     * @param alarmId
     */
    public ActivityAlarmViewModel(final AppCompatActivity context, final ActivityAlarmBinding binding, final long alarmId) {
        init(context, binding);
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
                        AlarmInfoFragment.newInstance(),
                        AlarmTracksFragment.newInstance()
                },
                new CharSequence[]{
                        "Mon alarme", "Mes chansons"
                });
        binding.slidingTabLayout.setDistributeEvenly(true);
        binding.slidingTabLayout.setSelectedIndicatorColors(android.R.color.white);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.slidingTabLayout.setViewPager(binding.viewPager);
        AlarmManager.getAlarmById(alarmId).subscribe(new Subscriber<Alarm>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
               // alarm = Alarm.builder().hour(8).minute(0).build();
            }

            @Override
            public void onNext(Alarm alarm) {
                ActivityAlarmViewModel.this.alarm = alarm;
                alarmName = alarm.getName();
            }
        });

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
        this.binding = binding;
        MusicAlarmApplication.get(context).netComponent.inject(this);

    }

    /***
     * Use this method to save the data
     */
    public void onValidateClick() {

        alarm.setName(alarmName);
        AlarmManager.saveAlarm(alarm, alarmTrackList).subscribe(new Subscriber<Alarm>() {
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
        //canel previous alarm intent
        alarmMgr.cancel(alarmIntent);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, this.alarm.getHour());
        calendar.set(Calendar.MINUTE, this.alarm.getMinute());

        alarmMgr.setRepeating(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60, alarmIntent);// test every 60 scs
    }


    /***
     * @param view
     */
    public void onAddTrackClick(View view) {
        context.startActivityForResult(Henson.with(context).gotoMusicActivity().build(), REQUEST_CODE);
    }


    /***
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                String playlistId = data.getStringExtra(EXTRA.PLAYLIST_ID);
                spotifyAPIManager.getPlaylistTracks(playlistId, new Subscriber<SpotifyPlaylistWithTrack>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SpotifyPlaylistWithTrack spotifyPlaylistWithTrack) {
                        alarmTrackList = new ArrayList<AlarmTrack>();
                        for (SpotifyPlaylistItem item : spotifyPlaylistWithTrack.getItems()) {
                           // alarmTrackList.add(AlarmTrack.builder().ref(item.getTrack().getId())
                                   // .type(AlarmTrack.TYPE.SPOTIFY).build());
                        }
                    }
                });
            }
        }
    }


    @Override
    public void onDestroy() {

    }

}
