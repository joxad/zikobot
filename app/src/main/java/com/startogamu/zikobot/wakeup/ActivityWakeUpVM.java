package com.startogamu.zikobot.wakeup;

import android.animation.Animator;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.media.AudioManager;
import android.view.View;
import android.view.WindowManager;

import com.android.databinding.library.baseAdapters.BR;
import com.joxad.easydatabinding.activity.ActivityBaseVM;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.alarm.AlarmVM;
import com.startogamu.zikobot.core.event.player.EventAddTrackToPlayer;
import com.startogamu.zikobot.core.model.Alarm;
import com.startogamu.zikobot.core.model.Track;
import com.startogamu.zikobot.core.module.mock.Mock;
import com.startogamu.zikobot.core.utils.AnimationEndListener;
import com.startogamu.zikobot.core.utils.EXTRA;
import com.startogamu.zikobot.databinding.ActivityWakeUpBinding;
import com.startogamu.zikobot.localtracks.TrackVM;
import com.startogamu.zikobot.player.PlayerVM;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by josh on 31/05/16.
 */
public class ActivityWakeUpVM extends ActivityBaseVM<ActivityWakeUp, ActivityWakeUpBinding> {


    private static final int ROTATION = 2;
    private static final int DELAY = 10;//MS
    Alarm alarm;
    AudioManager am;
    PlayerVM playerVM;

    int rotation = ROTATION;
    public AlarmVM alarmVM;
    public TrackVM trackVM;


    /***
     * @param activity
     * @param binding
     */
    public ActivityWakeUpVM(ActivityWakeUp activity, ActivityWakeUpBinding binding) {
        super(activity, binding);

    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        playerVM = new PlayerVM(activity);
        am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//For Normal mode
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        alarm = Parcels.unwrap(activity.getIntent().getParcelableExtra(EXTRA.ALARM));
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(listener -> onBackPressed());
        activity.setTitle(alarm.getName());
        alarmVM = new AlarmVM(activity, alarm) {
            @Override
            public ItemView itemView() {
                return ItemView.of(BR.trackVM, R.layout.item_alarm_track);

            }
        };
        binding.setAlarmVM(alarmVM);
        if (!alarm.getTracks().isEmpty()) {
            trackVM = new TrackVM(activity, Mock.track(activity));
            binding.setAlarmTrackVM(trackVM);
        }
        am.setStreamVolume(AudioManager.STREAM_MUSIC, alarm.getVolume(), alarm.getVolume());

        startAlarm(alarm);
    }

    @Override
    public void onResume() {
        super.onResume();
        playerVM.onResume();
    }

    /**
     * @param alarm
     */
    private void startAlarm(Alarm alarm) {
        ObservableArrayList<TrackVM> trackVMs = new ObservableArrayList<>();
        for (Track track : alarm.getTracks()) {
            trackVMs.add(new TrackVM(activity, track));
        }
        EventBus.getDefault().post(new EventAddTrackToPlayer(trackVMs));
        rotateCD();
    }

    /**
     *
     */
    private void rotateCD() {
        binding.rlPlayer.animate().rotationBy(rotation).setDuration(50).setListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rotateCD();
            }
        });
    }

    /***
     * @param view
     */
    public void stop(View view) {
        activity.finish();
    }


    @Override
    protected boolean onBackPressed() {
        stop(null);
        return super.onBackPressed();

    }

    @Override
    public void onPause() {
        super.onPause();
        playerVM.onPause();
        EventBus.getDefault().unregister(this);
    }

}
